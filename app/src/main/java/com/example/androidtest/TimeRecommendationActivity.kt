package com.example.androidtest

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class TimeRecommendationActivity : AppCompatActivity() {

    // 변수 선언
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
        button_time_morning = findViewById(R.id.button_time_morning)
        button_time_lunch = findViewById(R.id.button_time_lunch)
        button_time_dinner = findViewById(R.id.button_time_dinner)
        button_time_night = findViewById(R.id.button_time_night)
        button_time_complete = findViewById((R.id.button_time_complete))

        // 완료 버튼의 클릭 리스너
        button_time_complete.setOnClickListener {
            // 클릭된 버튼이 없다면 넘어가지 않음
            if (selectedButtonName == "") {
                Log.v("testLog", "클릭된 버튼이 없음")
            }
            // 현재 클릭된 버튼의 이름을 넘기고 다음 액티비티로 전환
            else {
                Log.v("testLog", "클릭된 버튼의 이름은 " + selectedButtonName)
            }
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

}