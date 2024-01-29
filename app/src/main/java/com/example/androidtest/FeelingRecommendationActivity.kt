package com.example.androidtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class FeelingRecommendationActivity : AppCompatActivity() {
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
                Log.v("testLog", "입력된 감정이 없습니다")
            }
            // 클릭된 버튼의 이름을 넘기고 다음 액티비티
            else {
                Log.v("testLog", "클릭하신 감정은 " + selectedButtonName + " 입니다")
            }

        }

        // 마이 페이지 버튼 클릭 리스너
        imageButton_myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

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


    }
}