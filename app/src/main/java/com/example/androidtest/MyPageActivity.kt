package com.example.androidtest

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    lateinit var imageButton_myPage_back: ImageButton
    lateinit var button_myPage_filtering: Button
    lateinit var button_myPage_logout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("here", "hi")
        setContentView(R.layout.activity_my_page)

        imageButton_myPage_back = findViewById(R.id.imageButton_myPage)
        button_myPage_filtering = findViewById(R.id.button_myPage_filtering)
        button_myPage_logout = findViewById(R.id.button_myPage_logout)

        imageButton_myPage_back.setOnClickListener {
            finish()
        }

        // 상세 정보 입력 버튼
        button_myPage_filtering.setOnClickListener {
            val intent = Intent(this, FilteringActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼
        button_myPage_logout.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("userID", "")
            Toast.makeText(this, "로그아웃 성공!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}