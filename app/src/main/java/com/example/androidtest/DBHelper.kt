package com.example.androidtest

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class DBHelper(
    val context: Context?,
): SQLiteOpenHelper(context, Database_Name, null, Database_Version) {
    companion object{
        const val Database_Name = "USER_AUTH"
        const val Database_Version = 1

        // TABLE
        const val TableName = "USER"
        const val UID = "UID"
        const val ColId = "ID"
        const val ColPw = "PW"
        const val ColAllergy = "ALLERGY"

        // 알러지, 선호하는 음식 등 추가
    }

    override fun onCreate(db: SQLiteDatabase) {
        val sql: String = "Create table if not exists " +
                "$TableName ($UID integer primary key autoincrement, " +
                "$ColId text, $ColPw text);"
        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion : Int, newVersion: Int) {
        val sql = "Drop table if exists $TableName"

        db.execSQL(sql)
        onCreate(db)
    }
    val allUsers : List<User>
        @SuppressLint("Range")
        get() {
            val users = ArrayList<User>()
            val selectQueryHandler = "SELECT * FROM $TableName"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQueryHandler, null)
            if(cursor.moveToFirst()){
                do {
                    val user = User()
                    user.id = cursor.getString(cursor.getColumnIndex(ColId))
                    user.pw = cursor.getString(cursor.getColumnIndex(ColPw))

                    users.add(user)
                }while (cursor.moveToNext())
            }
            db.close()
            return users
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
        return cursor.count > 0
    }

    // 유저 정보 업데이트 메소드
    fun updateUser(user: User): Int{
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ColId, user.id)
        values.put(ColPw, user.pw)

        return db.update(TableName, values, "$ColId = ?", arrayOf(user.id))
    }
}