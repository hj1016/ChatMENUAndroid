package com.example.androidtest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.zip.Inflater

class IngredientRecommendationActivity : AppCompatActivity() {
    // UI 요소 선언
    private lateinit var button_ir_plus : Button
    private lateinit var button_ir_complete : Button
    private lateinit var editText_ir_name : EditText
    private lateinit var button_ir_confirm : Button
    private lateinit var button_ir_cancel : Button
    private lateinit var listview_ir: ListView
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredient_recommendation)

        // UI 요소 초기화
        button_ir_plus = findViewById(R.id.button_ir_plus)
        button_ir_complete = findViewById(R.id.button_ir_complete)


        //리스트뷰와 어댑터 초기화
        listview_ir = findViewById<ListView>(R.id.listview_ir)
        val ingredientsList = ArrayList<String>()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ingredientsList)
        listview_ir.adapter = adapter

        // 리스트뷰 항목 터치시 해당 항목 삭제


        // 추가 버튼 터치시 동작
        try{
            button_ir_plus.setOnClickListener {
                // 재료 이름을 입력하는 레이어 팝업
                enterIngredientNamePopup()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }


    }
    // 재료 이름을 입력하는 다이얼로그 팝업
    private fun enterIngredientNamePopup() {

        //레이어 팝업 UI 초기화
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_ingredient_name, null)

        editText_ir_name = view.findViewById<EditText>(R.id.editText_ir_name)
        button_ir_confirm =view.findViewById<Button>(R.id.button_ir_confirm)
        button_ir_cancel = view.findViewById<Button>(R.id.button_ir_cancel)

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_ir_confirm.setOnClickListener {
            val ingredientName = editText_ir_name.text.toString()
            // 이름을 사용하여 버튼 생성
            addIngredient(ingredientName)
            dialog.dismiss() // 팝업 닫기
        }

        button_ir_cancel.setOnClickListener {
           dialog.dismiss() // 팝업 닫기
       }
        // 팝업 띄우기
        dialog.show()
    }

    //재료 리스트뷰에 추가
    private fun addIngredient(ingredientName: String) {
        // 입력 재료 이름 가져오기
        try{
            val ingredientName = editText_ir_name.text.toString()
            if (ingredientName.isNotEmpty()) {
                // 리스트에 항목 추가
                adapter.add(ingredientName)
                // 입력창 초기화
                editText_ir_name.text.clear()
            }
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    }

