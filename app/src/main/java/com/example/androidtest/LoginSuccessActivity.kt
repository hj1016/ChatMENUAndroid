package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.androidtest.databinding.ActivityLoginSuccessBinding


class LoginSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val intent = intent

        // 버튼 클릭 이벤트 설정
        binding.buttonGoToAnotherActivity.setOnClickListener {
            // 다른 화면으로 전환하는 Intent 생성
            val intent = Intent(this, LoginSuccessActivity::class.java)

            // 필요한 경우 다른 액티비티에 데이터 전달
            intent.putExtra("key", "value")

            // 액티비티 시작
            startActivity(intent)
        }

    }
}