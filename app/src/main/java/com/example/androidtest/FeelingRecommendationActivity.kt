package com.example.androidtest

import ChatGPTConnection
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// 사용자의 기분에 공감하며, 기분에 어울리는 음식 추천하는 기능
class FeelingRecommendationActivity : AppCompatActivity() {

    private var request_msg: String = "주어진 단어에서 기분과 관련된 단어를 추출해서 그 기분과 어울리는 음식메뉴 3개 추천해줘. 각 음식에 대한 설명이 한 줄 이하가 되도록 해줘. 단, 그 감정에 공감하는 말을 먼저 해주고 그 다음에 추천해줘."
    lateinit var button_feeling_confirm: Button
    lateinit var result_feeling: TextView
    lateinit var imageButton_myPage: ImageButton
    lateinit var button_feeling_happy: Button
    lateinit var button_feeling_sad: Button
    lateinit var button_feeling_angry: Button
    lateinit var button_feeling_depressed: Button
    lateinit var button_feeling_excited: Button
    lateinit var button_feeling_complete: Button

    var selectedButtonName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feeling_recommendation)

        //변수연결
        imageButton_myPage = findViewById(R.id.imageButton_myPage)
        button_feeling_happy = findViewById(R.id.button_feeling_happy)
        button_feeling_sad = findViewById(R.id.button_feeling_sad)
        button_feeling_angry = findViewById(R.id.button_feeling_angry)
        button_feeling_depressed = findViewById(R.id.button_feeling_depressed)
        button_feeling_excited = findViewById(R.id.button_feeling_excited)
        button_feeling_complete = findViewById(R.id.button_feeling_complete)

        //완료 버튼 클릭
        button_feeling_complete.setOnClickListener {
            // 클릭된 버튼이 없다면
            if (selectedButtonName == "") {
                Toast.makeText(this, "입력된 감정이 없습니다", Toast.LENGTH_SHORT).show()
            }
            // 클릭된 버튼의 이름을 넘기고 다음 액티비티
            else {
                lifecycleScope.launch {
                    Toast.makeText(this@FeelingRecommendationActivity, "추천 결과 생성에 10-20초 정도 소요됩니다", Toast.LENGTH_SHORT).show()
                    resultPopup()
                }
            }

        }

        // 마이 페이지 버튼 클릭 리스너
        imageButton_myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        } // onCreate 메소드 종료
        fun onButtonClick(view: View) {
        // 클릭한 버튼의 이름 추출
            when (view.id) {
            R.id.button_feeling_happy -> selectedButtonName = button_feeling_happy.text.toString()
            R.id.button_feeling_sad -> selectedButtonName = button_feeling_sad.text.toString()
            R.id.button_feeling_angry -> selectedButtonName = button_feeling_angry.text.toString()
            R.id.button_feeling_depressed -> selectedButtonName = button_feeling_depressed.text.toString()
            R.id.button_feeling_excited -> selectedButtonName = button_feeling_excited.text.toString()
        }
            // 모든 버튼을 선택 해제
            button_feeling_happy.isSelected = false
            button_feeling_sad.isSelected = false
            button_feeling_angry.isSelected = false
            button_feeling_depressed.isSelected = false
            button_feeling_excited.isSelected = false

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

        button_feeling_confirm =view.findViewById<Button>(R.id.button_rr_confirm)
        result_feeling = view.findViewById<TextView>(R.id.textview_result)

        result_feeling.text = chatGPTRequest(request_msg + " 단어: $selectedButtonName")

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_feeling_confirm.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }
        // 팝업 띄우기
        dialog.show()

    }
}