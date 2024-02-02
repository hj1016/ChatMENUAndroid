package com.example.androidtest

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class DBHelper(
    val context: Context?,
): SQLiteOpenHelper(context, Database_Name, null, Database_Version) {
    companion object{
        const val Database_Name = "USER_AUTH"
        const val Database_Version = 3

        // TABLE
        const val TableName = "USER"
        const val UID = "UID"
        const val ColId = "ID"
        const val ColPw = "PW"
        const val ColAllergy = "ALLERGY" // 배열, 텍스트
        const val ColPreFood = "PREFOOD" // 배열
        const val ColFlavour = "FLAVOUR" // 배열
        const val ColPeopleNumber = "PEOPLENUMBER" // Int
        const val ColDiet = "Diet" // bool
        const val ColVegan = "Vegan" // 배열
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql: String = "Create table if not exists " +
                "$TableName ($UID integer primary key autoincrement, " +
                "$ColId text, $ColPw text, $ColAllergy text, $ColPreFood text," +
                "$ColFlavour text, $ColPeopleNumber int, $ColDiet text, $ColVegan text);"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion : Int, newVersion: Int) {
        val sql = "Drop table if exists $TableName"

        db.execSQL(sql)
        onCreate(db)
    }
    fun checkIdExist(id: String): Boolean{
        val db = this.readableDatabase

        // 리턴 받고자 하는 컬럼 값의 array
        val projection = arrayOf(UID)

        // where "id" = id and "password" = password 구문 적용하는 부분
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(id)

        // 정렬 조건 지정
        val cursor = db.query(
            TableName,
            projection, // 리턴 받고자 하는 컬럼
            selection, // where 조건
            selectionArgs, // where 조건에 해당하는 값의 배열
            null,
            null,
            null
        )
        // 리턴된 cursor 값이 존재하면 아이디 중복으로 true, 존재하지 않으면 아이디 생성 가능으로 false
        return cursor.count > 0
    }

    // 회원가입 db에 유저 추가 메소드
    fun addUser(user: User){
        if(checkIdExist(user.id)) {
            Toast.makeText(this.context, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColId, user.id)
        values.put(ColPw, user.pw)

        db.insert(TableName, null, values)
        db.close()
        Toast.makeText(this.context, "회원가입 성공", Toast.LENGTH_SHORT).show()
    }

    // 로그인 메소드
    fun login(user: User) : Boolean{
        val db = this.readableDatabase

        val projection = arrayOf(UID)

        val selection = "$ColId = ? AND $ColPw = ?"
        val selectionArgs = arrayOf(user.id, user.pw)

        val cursor = db.query(
            TableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        if (cursor.count > 0) { // 진짜 로그인 : 로컬 스토리지에 유저 ID의 값이 저장되면 로그인 상태로 간주
            val sharedPreferences = context!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userID", user.id)
            editor.apply()
        }

        return cursor.count > 0
    }

    fun logout() {
        val sharedPreferences = context!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userID", "")
    }

    fun getUserID(): String? {
        val sharedPreferences = context!!.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userID = sharedPreferences.getString("userID", "")
        return userID
    }

    // 알러지 업데이트
    fun updateAllergy(allergies: Array<String>): Int {
        var userID = this.getUserID()
        Log.d("userID",userID.toString())
        Log.d("allergies",allergies.toString())

        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColAllergy, Json.encodeToString(allergies))
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }

    fun getAllergy():Array<String> {
        val db = this.readableDatabase
        var userID = this.getUserID()
        // 리턴 받고자 하는 컬럼 값의 array
        val projection = arrayOf(ColAllergy)

        // where "id" = id and "password" = password 구문 적용하는 부분
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)

        // 정렬 조건 지정
        val cursor = db.query(
            TableName,
            projection, // 리턴 받고자 하는 컬럼
            selection, // where 조건
            selectionArgs, // where 조건에 해당하는 값의 배열
            null,
            null,
            null
        )

        var jsonData: String? = null
        var result:Array<String> = emptyArray()
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColAllergy)
            if (columnIndex != -1) {
                Log.d("columnIndex", columnIndex.toString())
                jsonData = cursor.getString(columnIndex)

                if (jsonData != null) {
                    result = Json.decodeFromString<Array<String>>(jsonData)
                }

            } else {
                Log.e("TAG", "ColAllergy column does not exist.")
            }
        }
        Log.d("result", result!!.joinToString())

        return result
    }


    // 선호하는 음식 업데이트
    fun updatePreFood(selectedFood: String): Int {
        val userID = getUserID()
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColPreFood, selectedFood)
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }
    // 선호하는 음식 불러오기
    fun getPreFood(): String {
        val userID = getUserID()
        val db = this.readableDatabase
        val projection = arrayOf(ColPreFood)
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = db.query(TableName, projection, selection, selectionArgs, null, null, null)
        var result: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColPreFood)
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex)
            } else {
                Log.e("TAG", "ColPreFood column does not exist.")
            }
        }
        Log.d("result", result ?: "Empty Result")
        return result ?: ""
    }


    // 선호하는 맛 업데이트
    fun updateFlavour(selectedFood: String): Int {
        val userID = getUserID()
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColFlavour, selectedFood)
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }
    // 선호하는 맛 불러오기
    fun getFlavour(): String {
        val userID = getUserID()
        val db = this.readableDatabase
        val projection = arrayOf(ColFlavour)
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = db.query(TableName, projection, selection, selectionArgs, null, null, null)
        var result: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColFlavour)
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex)
            } else {
                Log.e("TAG", "ColFlavour column does not exist.")
            }
        }
        Log.d("result", result ?: "Empty Result")
        return result ?: ""
    }


    // 식사 인원 업데이트
    fun updatePeopleNum(value: Int): Int {
        val userID = getUserID()
        Log.d("userID", userID.toString())
        Log.d("value", value.toString())

        val db = writableDatabase
        val values = ContentValues()
        values.put(ColPeopleNumber, value)
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }

    // 식사 인원 불러오기
    fun getPeopleNum(): Int {
        val db = readableDatabase
        val userID = getUserID()

        val projection = arrayOf(ColPeopleNumber)
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)

        val cursor = db.query(
            TableName,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var result: Int = 1
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColPeopleNumber)
            if (columnIndex != -1) {
                result = cursor.getInt(columnIndex)
            } else {
                Log.e("TAG", "$ColPeopleNumber 컬럼이 존재하지 않습니다.")
            }
        }

        Log.d("result", result.toString())
        return result
    }


    // 다이어트 여부 업데이트
    fun updateDiet(selectedDiet: String): Int {
        val userID = getUserID()
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColDiet, selectedDiet)
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }
    // 다이어트 여부 불러오기
    fun getDiet(): String {
        val userID = getUserID()
        val db = this.readableDatabase
        val projection = arrayOf(ColDiet)
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = db.query(TableName, projection, selection, selectionArgs, null, null, null)
        var result: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColDiet)
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex)
            } else {
                Log.e("TAG", "ColDiet column does not exist.")
            }
        }
        Log.d("result", result ?: "Empty Result")
        return result ?: ""
    }


    // 비건 선택 업데이트
    fun updateVegan(selectedVegan: String): Int {
        val userID = getUserID()
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColVegan, selectedVegan)
        return db.update(TableName, values, "$ColId = ?", arrayOf(userID))
    }

    // 비건 선택 불러오기
    fun getVegan(): String {
        val userID = getUserID()
        val db = this.readableDatabase
        val projection = arrayOf(ColVegan)
        val selection = "$ColId = ?"
        val selectionArgs = arrayOf(userID)
        val cursor = db.query(TableName, projection, selection, selectionArgs, null, null, null)
        var result: String? = null
        if (cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(ColVegan)
            if (columnIndex != -1) {
                result = cursor.getString(columnIndex)
            } else {
                Log.e("TAG", "ColVegan column does not exist.")
            }
        }
        Log.d("result", result ?: "Empty Result")
        return result ?: ""
    }

}