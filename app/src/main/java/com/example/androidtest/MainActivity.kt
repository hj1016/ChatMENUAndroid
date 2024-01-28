package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidtest.DBManage

// MainActivity에 있던 코드는 MainOptionActivity로 옮겼습니다.
// activity_main.xml도 activity_main_option으로 이동.

class MainActivity : AppCompatActivity() {
    lateinit var db : DBManage
    var users = ArrayList<User>()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBManage(this)

        binding.registerButton.setOnClickListener {
            createUser().let {
                if (it != null) {
                    db.addUser(it)
                }
            }
        }

        binding.loginButton.setOnClickListener {
            createUser().let {
                if (it != null) {
                    if (db.login(it)) {
                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginSuccessActivity::class.java)
                        intent.putExtra("name", binding.nameEditText.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "로그인 실패!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }

        fun createUser(): User? {
            val id = binding.idEditText.text.toString()
            val pw = binding.pwEditText.text.toString()
            if(id == "" || pw == "")
                return null
        }
    }
}