package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


class LoginSuccessActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginSuccessBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

    }
}