package com.example.androidtest

import ChatGPTConnection
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// 냉장고 안에 있는 재료들로 만들어 먹을 수 있는 음식 추천 받는 기능
class IngredientRecommendationActivity : AppCompatActivity() {
    // UI 요소 선언
    private var request_msg: String = "냉장고 안에 있는 재료 목록을 알려줄테니까 이것들을 활용해서 만들 수 있는 요리 3가지 알려줘. 레시피는 각 3줄이하로 요약해서 알려줘. 단, 주어진 사용자 정보를 고려하여 음식을 추천해줘."
    private lateinit var button_ir_result_confirm : Button
    private lateinit var result_ir: TextView
    private lateinit var imageButton_ir_myPage: ImageButton
    private lateinit var imageButton_ir_back: ImageButton
    private lateinit var button_ir_plus : Button
    private lateinit var button_ir_complete : Button
    private lateinit var editText_ir_name : EditText
    private lateinit var button_ir_confirm : Button
    private lateinit var button_ir_cancel : Button
    private lateinit var listview_ir: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_recommendation)

        // UI 요소 초기화
        button_ir_plus = findViewById(R.id.button_ir_plus)
        button_ir_complete = findViewById(R.id.button_ir_complete)
        imageButton_ir_myPage = findViewById(R.id.imageButton_ir_myPage)
        imageButton_ir_back = findViewById(R.id.imageButton_ir_back)

        //리스트뷰와 어댑터 초기화
        listview_ir = findViewById<ListView>(R.id.listview_ir)
        val ingredientsList = ArrayList<String>()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsList)
        listview_ir.adapter = adapter

        // 리스트뷰 항목 터치시 해당 항목 삭제
        listview_ir.setOnItemClickListener { _, _, position, _ ->
            // 클릭된 항목 삭제
            adapter.remove(adapter.getItem(position))
            // 어댑터 갱신
            adapter.notifyDataSetChanged()
        }

        // 추가 버튼 터치시 동작
        try{
            button_ir_plus.setOnClickListener {
                // 재료 이름을 입력하는 레이어 팝업
                enterIngredientNamePopup()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

        // 마이 페이지 버튼 클릭 리스너
        imageButton_ir_myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 뒤로 가기 버튼 클릭 리스너
        imageButton_ir_back.setOnClickListener {
            finish()
        }

        // 완료 버튼 클릭 리스너
        button_ir_complete.setOnClickListener {
            lifecycleScope.launch {
                Toast.makeText(this@IngredientRecommendationActivity, "추천 결과 생성에 10-20초 정도 소요됩니다", Toast.LENGTH_SHORT).show()
                resultPopup()
            }
        }
    }
    // 재료 이름을 입력하는 다이얼로그 팝업
    private fun enterIngredientNamePopup() {

        //레이어 팝업 UI 초기화
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_ingredient_name, null)

        editText_ir_name = view.findViewById<EditText>(R.id.editText_ir_name)
        button_ir_confirm =view.findViewById<Button>(R.id.button_ir_confirm)
        button_ir_cancel = view.findViewById<Button>(R.id.button_ir_cancel)

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_ir_confirm.setOnClickListener {
            val ingredientName = editText_ir_name.text.toString()
            // 이름을 사용하여 버튼 생성
            addIngredient(ingredientName)
            dialog.dismiss() // 팝업 닫기
        }
        button_ir_cancel.setOnClickListener {
           dialog.dismiss() // 팝업 닫기
       }
        // 팝업 띄우기
        dialog.show()
    }

    //재료 리스트뷰에 추가
    private fun addIngredient(ingredientName: String) {
        // 입력 재료 이름 가져오기
        try{
            val ingredientName = editText_ir_name.text.toString()
            if (ingredientName.isNotEmpty()) {
                // 리스트에 항목 추가
                adapter.add(ingredientName)
                // 입력창 초기화
                editText_ir_name.text.clear()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }
    private suspend fun chatGPTRequest(msg: String): String {
        return suspendCoroutine { continuation ->
            lifecycleScope.launch {
                val chatGPTConnection = ChatGPTConnection()
                val result = chatGPTConnection.sendChatRequest(msg)
                continuation.resume(result)
            }
        }
    }
    private suspend fun resultPopup() {
        //다이얼로그 팝업 UI 초기화
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_recommendation_result, null)

        button_ir_result_confirm =view.findViewById<Button>(R.id.button_rr_confirm)
        result_ir = view.findViewById<TextView>(R.id.textview_result)

        // 리스트뷰 항목의 이름들을 문자열로 가져오기
        val itemNames: String = (0 until adapter.count).map { adapter.getItem(it)!! }.joinToString()
        val intent = intent //전달할 데이터를 받을 Intent
        val userInfo = intent.getStringExtra("user_info")
        Log.d("filtering_test2",userInfo.toString())
        result_ir.text = chatGPTRequest(request_msg + " [재료목록] $itemNames "+userInfo.toString())

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_ir_result_confirm.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }
        // 팝업 띄우기
        dialog.show()

    }

    }

