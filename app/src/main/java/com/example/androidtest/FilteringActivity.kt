package com.example.androidtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FilteringActivity: AppCompatActivity() {

    private lateinit var button_ft_complete : Button
    private lateinit var editText_ft_name : EditText
    private lateinit var button_ft_confirm : Button
    private lateinit var button_ft_cancel : Button
    private lateinit var button_ft_plus : Button
    private lateinit var recyclerView : RecyclerView
    private val buttonNames = mutableListOf<String>()
    private lateinit var buttonAdapter: ButtonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtering)
        //UI 요소 초기화
        button_ft_plus = findViewById(R.id.button_ft_plus)

        //리사이클러뷰와 어댑터 초기화
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_filtering)
        buttonAdapter = ButtonAdapter(buttonNames)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = buttonAdapter

        // 리스트뷰 항목 터치시 해당 항목 삭제

        // 추가 버튼 터치시 동작
        try{
            button_ft_plus.setOnClickListener {
                // 알러지 이름을 입력하는 레이어 팝업
                enterAllergyNamePopup()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun enterAllergyNamePopup() {

        //레이어 팝업 UI 초기화
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_allergy_name, null)

        editText_ft_name = view.findViewById<EditText>(R.id.editText_ft_name)
        button_ft_confirm =view.findViewById<Button>(R.id.button_ft_confirm)
        button_ft_cancel = view.findViewById<Button>(R.id.button_ft_cancel)

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_ft_confirm.setOnClickListener {
            val allergyName = editText_ft_name.text.toString()
            // 이름을 사용하여 버튼 생성
            addNewButton(allergyName)
            dialog.dismiss() // 팝업 닫기
        }
        button_ft_cancel.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }
        // 팝업 띄우기
        dialog.show()
    }
    private fun addNewButton(name: String) {
        buttonNames.add(name)
        buttonAdapter.notifyItemInserted(buttonNames.size - 1)
    }


}
