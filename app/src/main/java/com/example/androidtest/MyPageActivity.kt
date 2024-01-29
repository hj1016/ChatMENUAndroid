package com.example.androidtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MyPageActivity : AppCompatActivity() {

    lateinit var textView_myPage_id: TextView
    lateinit var button_myPage_filtering: Button
    lateinit var button_myPage_logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        textView_myPage_id = findViewById(R.id.textView_myPage_id)
        button_myPage_filtering = findViewById(R.id.button_myPage_filtering)
        button_myPage_logout = findViewById(R.id.button_myPage_logout)

        // 상세 정보 입력 버튼
        button_myPage_filtering.setOnClickListener {
            val intent = Intent(this, FilteringActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼
        button_myPage_logout.setOnClickListener {

        }
    }
}