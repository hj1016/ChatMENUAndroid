package com.example.androidtest

import ChatGPTConnection
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TimeRecommendationActivity : AppCompatActivity() {

    // 변수 선언
    private var request_msg: String = "주어진 단어를 보고 그 시간대와 어울리는 음식 메뉴 3개 추천해줘. 각 음식에 대한 설명이 한 줄 이하가 되도록 해줘. 단, 주어진 사용자 정보를 고려하여 음식을 추천해줘."
    lateinit var button_time_confirm: Button
    lateinit var result_time: TextView
    lateinit var imageButton_myPage: ImageButton
    lateinit var button_time_morning: Button
    lateinit var button_time_lunch: Button
    lateinit var button_time_dinner: Button
    lateinit var button_time_night: Button
    lateinit var button_time_complete: Button

    var selectedButtonName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_recommendation)

        // 변수 연결
        imageButton_myPage = findViewById(R.id.imageButton_myPage)
        button_time_morning = findViewById(R.id.button_time_morning)
        button_time_lunch = findViewById(R.id.button_time_lunch)
        button_time_dinner = findViewById(R.id.button_time_dinner)
        button_time_night = findViewById(R.id.button_time_night)
        button_time_complete = findViewById((R.id.button_time_complete))

        // 완료 버튼의 클릭 리스너
        button_time_complete.setOnClickListener {
            // 클릭된 버튼이 없다면 넘어가지 않음
            if (selectedButtonName == "") {
                Toast.makeText(this, "입력된 시간이 없습니다", Toast.LENGTH_SHORT).show()
            }
            // 현재 클릭된 버튼의 이름을 넘기고 다음 액티비티로 전환
            else {
                lifecycleScope.launch {
                    Toast.makeText(this@TimeRecommendationActivity, "${selectedButtonName} 추천 결과 생성에 10-20초 정도 소요됩니다", Toast.LENGTH_SHORT).show()
                    resultPopup()
                }
            }
        }

        // 마이 페이지 버튼 클릭 리스너
        imageButton_myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }

    fun onButtonClick(view: View) {
        // 클릭된 버튼의 이름 추출 (아침, 점심, 저녁, 새벽)
        when (view.id) {
            R.id.button_time_morning -> selectedButtonName = button_time_morning.text.toString()
            R.id.button_time_lunch -> selectedButtonName = button_time_lunch.text.toString()
            R.id.button_time_dinner -> selectedButtonName = button_time_dinner.text.toString()
            R.id.button_time_night -> selectedButtonName = button_time_night.text.toString()
        }

        // 모든 버튼을 선택 해제
        button_time_morning.isSelected = false
        button_time_lunch.isSelected = false
        button_time_dinner.isSelected = false
        button_time_night.isSelected = false

        // 클릭한 버튼을 선택 상태로 변경
        view.isSelected = true
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

        button_time_confirm =view.findViewById<Button>(R.id.button_rr_confirm)
        result_time = view.findViewById<TextView>(R.id.textview_result)

        val intent = intent //전달할 데이터를 받을 Intent
        val user_info = intent.getStringExtra("user_info")
        result_time.text = chatGPTRequest(request_msg + " [단어] $selectedButtonName" + user_info)

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_time_confirm.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }
        // 팝업 띄우기
        dialog.show()

    }
}