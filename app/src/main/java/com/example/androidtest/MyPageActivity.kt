package com.example.androidtest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    lateinit var imageButton_myPage_back: ImageButton
    lateinit var button_myPage_filtering: Button
    lateinit var button_myPage_logout: Button
    lateinit var db: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)
        db = DBHelper(this)

        imageButton_myPage_back = findViewById(R.id.imageButton_myPage_back)
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
            db.logout()
            Toast.makeText(this, "로그아웃 성공!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}