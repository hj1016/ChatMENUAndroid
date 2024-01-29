package com.example.androidtest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainOptionActivity : AppCompatActivity() {
    //ui 요소 선언
    lateinit var button_main_chat : Button
    lateinit var button_main_ir : Button
    lateinit var button_main_time : Button
    lateinit var button_main_weather : Button
    lateinit var button_main_feeling : Button
    lateinit var button_main_filtering : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_option)

        //ui 요소 초기화
        button_main_ir = findViewById(R.id.button_main_ir) //IngredientRecommendationActiviy로 이동하는 버튼
        button_main_chat = findViewById(R.id.button_main_chat)
        button_main_time = findViewById(R.id.button_main_time)
        button_main_weather = findViewById(R.id.button_main_weather)
        button_main_feeling = findViewById(R.id.button_main_feeling)
        button_main_filtering = findViewById(R.id.button_main_filtering)


        //화면 전환
        button_main_ir.setOnClickListener {
            val intent = Intent(this, IngredientRecommendationActivity::class.java)
            startActivity(intent)
        }
        button_main_chat.setOnClickListener {
            val intent = Intent(this, ChatRecommendationActivity::class.java)
            startActivity(intent)
        }
        button_main_time.setOnClickListener {
            val intent = Intent(this, TimeRecommendationActivity::class.java)
            startActivity(intent)
        }
        button_main_weather.setOnClickListener {
            val intent = Intent(this,WeatherRecommendationActivity::class.java)
            startActivity(intent)
        }
        button_main_feeling.setOnClickListener {
            val intent = Intent(this, FeelingRecommendationActivity::class.java)
            startActivity(intent)
        }
        button_main_filtering.setOnClickListener {
            val intent = Intent(this, FilteringActivity::class.java)
            startActivity(intent)
        }

    }

}