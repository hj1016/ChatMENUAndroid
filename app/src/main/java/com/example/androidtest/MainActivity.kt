package com.example.androidtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidtest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var db : DBHelper
    var users = ArrayList<User>()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = DBHelper(this)
        // db.dropTable();

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

                        /*
                         * === 로그아웃
                         *  val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                         *  val editor = sharedPreferences.edit()
                         *  editor.putString("userID", "")
                         * === 로그인 여부 확인
                         * val userID = sharedPreferences.getString("userID", "")
                         * userID 가 비었다면 로그인 안 되어있음, 값이 있으면 로그인 상태
                         */


                        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainOptionActivity::class.java)
                        intent.putExtra("ID", binding.idEditText.text.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "로그인 실패!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "정보를 모두 입력해 주세요", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    fun createUser(): User? {
        val id = binding.idEditText.text.toString()
        val pw = binding.pwEditText.text.toString()

        return if (id.isNotEmpty() && pw.isNotEmpty()) {
            User(id, pw)  // 실제 User 객체를 반환하도록 수정
        } else {
            null
        }
    }
}
