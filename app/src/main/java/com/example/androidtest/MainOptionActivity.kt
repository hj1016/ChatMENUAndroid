package com.example.androidtest

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var dbHelper: DBHelper
    private var userInfo:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_option)

        Log.d("event", "onCreate called")

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
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }
        button_main_chat.setOnClickListener {
            val intent = Intent(this, ChatRecommendationActivity::class.java)
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }
        button_main_time.setOnClickListener {
            val intent = Intent(this, TimeRecommendationActivity::class.java)
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }
        button_main_weather.setOnClickListener {
            val intent = Intent(this,WeatherRecommendationActivity::class.java)
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }
        button_main_feeling.setOnClickListener {
            val intent = Intent(this, FeelingRecommendationActivity::class.java)
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }
        button_main_filtering.setOnClickListener {
            val intent = Intent(this, FilteringActivity::class.java)
            intent.putExtra("user_info", userInfo)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("event", "onResume called")
        dbHelper = DBHelper(this)
        var allergies:String = dbHelper.getAllergy().joinToString()
        var preFoods:String = dbHelper.getPreFood()
        var flavour:String = dbHelper.getFlavour()
        var peopleNum:String = dbHelper.getPeopleNum().toString()
        var diet:String = dbHelper.getDiet()
        var vegan:String = dbHelper.getVegan()
        userInfo = "[사용자 정보] 알러지: $allergies / 선호하는 음식 종류: $preFoods / "
        userInfo = userInfo + " 선호하는 맛: $flavour / 식사 인원: $peopleNum / 다이어트 여부: $diet / 비건 여부: $vegan"
        Log.d("userInfo", userInfo)
    }

}