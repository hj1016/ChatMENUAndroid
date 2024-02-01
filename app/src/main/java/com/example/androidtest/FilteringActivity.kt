package com.example.androidtest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var dbHelper: DBHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtering)

        //UI 요소 초기화
        button_ft_plus = findViewById(R.id.button_ft_plus)
        button_ft_complete = findViewById(R.id.button_ft_complete)

        //리사이클러뷰와 어댑터 초기화
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView_filtering)
        buttonAdapter = ButtonAdapter(buttonNames)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = buttonAdapter

        // dbHelper 초기화
        dbHelper = DBHelper(this)
//        dbHelper.updateAllergy(emptyArray())
//        Log.d("UpdatedAllergies", dbHelper.updateAllergy().joinToString())
//        val getAllergies = dbHelper.getAllergy()
//        Log.d("Retrived Allergies", getAllergies.joinToString())

        val radioGroupPreFood = findViewById<RadioGroup>(R.id.radioGroupPreFood)
        val radioGroupFlavour = findViewById<RadioGroup>(R.id.radioGroupFlavour)
        val editTextPeople = findViewById<EditText>(R.id.editText_ft_peoplenum)
        val radioGroupDiet = findViewById<RadioGroup>(R.id.radioGroupDiet)
        val radioGroupVegan = findViewById<RadioGroup>(R.id.radioGroupVegan)


        // 선호하는 음식
        radioGroupPreFood.setOnCheckedChangeListener { _, checkedId ->
            val selectedFood = when (checkedId) {
                R.id.button_ft_korea -> "한식"
                R.id.button_ft_japan -> "일식"
                R.id.button_ft_western -> "양식"
                R.id.button_ft_china -> "중식"
                else -> "" // 다른 라디오 버튼을 고려할 경우
            }

            // 선택된 음식 정보를 DB에 업데이트
            dbHelper.updatePreFood(selectedFood)
        }

        // 액티비티가 시작될 때 DB에서 이전에 선택한 음식을 불러와서 라디오 버튼을 선택해 주는 코드
        val selectedFood = dbHelper.getPreFood()
        when (selectedFood) {
            "한식" -> radioGroupPreFood.check(R.id.button_ft_korea)
            "일식" -> radioGroupPreFood.check(R.id.button_ft_japan)
            "양식" -> radioGroupPreFood.check(R.id.button_ft_western)
            "중식" -> radioGroupPreFood.check(R.id.button_ft_china)
        }

        // 선호하는 맛
        radioGroupFlavour.setOnCheckedChangeListener { _, checkedId ->
            val selectedFlavour = when (checkedId) {
                R.id.button_ft_sweet -> "단맛"
                R.id.button_ft_spicy -> "매운맛"
                R.id.button_ft_salty -> "짠맛"
                R.id.button_ft_sour -> "신맛"
                else -> "" // 다른 라디오 버튼을 고려할 경우
            }

            // 선택된 음식 정보를 DB에 업데이트
            dbHelper.updateFlavour(selectedFlavour)
        }

        // 액티비티가 시작될 때 DB에서 이전에 선택한 맛을 불러와서 라디오 버튼을 선택해 주는 코드
        val selectedFlavour = dbHelper.getFlavour()
        when (selectedFlavour) {
            "단맛" -> radioGroupFlavour.check(R.id.button_ft_sweet)
            "매운맛" -> radioGroupFlavour.check(R.id.button_ft_spicy)
            "짠맛" -> radioGroupFlavour.check(R.id.button_ft_salty)
            "신맛" -> radioGroupFlavour.check(R.id.button_ft_sour)
        }


        // 식사 인원
        editTextPeople.setOnClickListener {
            val peopleNumberValue = editTextPeople.text.toString().toIntOrNull() ?: 0

            // 선택된 인원수 정보를 DB에 업데이트
            dbHelper.updatePeopleNum(peopleNumberValue)
        }


        // 다이어트 여부
        radioGroupDiet.setOnCheckedChangeListener { _, checkedId ->
            val selectedDiet = when (checkedId) {
                R.id.button_ft_diet -> "해당"
                R.id.button_ft_nodiet -> "해당 없음"
                else -> "" // 다른 라디오 버튼을 고려할 경우
            }

            // 선택된 다이어트 여부 정보를 DB에 업데이트
            dbHelper.updateDiet(selectedDiet)
        }

        // 액티비티가 시작될 때 DB에서 이전에 선택한 다이어트 여부를 불러와서 라디오 버튼을 선택해 주는 코드
        val selectedDiet = dbHelper.getDiet()
        when (selectedDiet) {
            "해당" -> radioGroupDiet.check(R.id.button_ft_diet)
            "해당 없음" -> radioGroupDiet.check(R.id.button_ft_nodiet)
        }


        // 비건 선택
        radioGroupVegan.setOnCheckedChangeListener { _, checkedId ->
            val selectedVegan = when (checkedId) {
                R.id.button_ft_flex -> "플렉시테리언"
                R.id.button_ft_polo -> "폴로베지테리언"
                R.id.button_ft_pesco -> "페스코베지테리언"
                R.id.button_ft_lactoobo -> "락토오브베지테리언"
                R.id.button_ft_obo -> "오보베지테리언"
                R.id.button_ft_lacto -> "락토베지테리언"
                R.id.button_ft_vegan -> "비건"
                R.id.button_ft_fru -> "프루테리언"
                R.id.button_ft_non -> "논비건"
                else -> "" // 다른 라디오 버튼을 고려할 경우
            }

            // 비건 정보를 DB에 업데이트
            dbHelper.updateVegan(selectedVegan)
        }

        // 액티비티가 시작될 때 DB에서 이전에 비건 정보를 불러와서 라디오 버튼을 선택해 주는 코드
        val selectedVegan = dbHelper.getVegan()
        when (selectedVegan) {
            "플렉시테리언" -> radioGroupVegan.check(R.id.button_ft_flex)
            "폴로베지테리언" -> radioGroupVegan.check(R.id.button_ft_polo)
            "페스코베지테리언" -> radioGroupVegan.check(R.id.button_ft_pesco)
            "락토오브베지테리언" -> radioGroupVegan.check(R.id.button_ft_lactoobo)
            "오보베지테리언" -> radioGroupVegan.check(R.id.button_ft_obo)
            "락토베지테리언" -> radioGroupVegan.check(R.id.button_ft_lacto)
            "비건" -> radioGroupVegan.check(R.id.button_ft_vegan)
            "프루테리언" -> radioGroupVegan.check(R.id.button_ft_fru)
            "논비건" -> radioGroupVegan.check(R.id.button_ft_non)
        }


        // 리스트뷰 항목 터치시 해당 항목 삭제

        // 추가 버튼 터치시 동작
        try {
            button_ft_plus.setOnClickListener {
                // 알러지 이름을 입력하는 레이어 팝업
                enterAllergyNamePopup()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // 완료 버튼 클릭 시 이전(마이페이지) 화면으로
        button_ft_complete.setOnClickListener {
            finish()
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
