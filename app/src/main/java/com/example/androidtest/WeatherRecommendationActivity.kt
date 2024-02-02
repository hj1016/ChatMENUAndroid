package com.example.androidtest

import ChatGPTConnection
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// API에서 오늘의 날씨 정보를 불러와 날씨 기반으로 음식 추천하는 기능
class WeatherRecommendationActivity : AppCompatActivity() {

    // 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 37.5665
    private var longitude: Double = 126.9780

    // 2
    private val apiKey = "6a83eb4b37a279a7d643253d454ff40e"
    private val apiUrl = "https://api.openweathermap.org/data/2.5/weather"
    private var request_msg: String = "날씨를 참고하여 계절과 온도에 맞는 음식메뉴 3개 추천해줘. 각 음식에 대한 설명이 한 줄 이하가 되도록 해줘. 단, 주어진 사용자 정보를 고려하여 음식을 추천해줘."
    lateinit var button_wr_complete: Button
    lateinit var button_wr_confirm: Button
    lateinit var result_wr: TextView
    lateinit var imageButton_weather_myPage: ImageButton
    lateinit var imageButton_weather_back: ImageButton
    lateinit var cityNameView: TextView
    lateinit var tempView: TextView
    lateinit var humidityView: TextView
    lateinit var descriptionView: TextView
    lateinit var iconView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_recommendation)

        // ImageView 찾기
        iconView = findViewById<ImageView>(R.id.icon_view)


        imageButton_weather_myPage = findViewById(R.id.imageButton_weather_myPage)
        imageButton_weather_back = findViewById(R.id.imageButton_weather_back)
        button_wr_complete = findViewById(R.id.button_weather_complete)

        // 1. GPS 정보를 가져온다. => 위도, 경도
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            requestLocationUpdates()
        }

        // 2. api 를 이용해서 날씨 정보를 가져온다.=> 도시 이름, 날씨(온도, 습도, 눈, 비,....)
        getWeatherData(apiUrl, latitude, longitude, apiKey)

        // 3. 그래픽과 텍스트로 현재 날씨를 화면에 표시 (그래픽은 나중에 여유 있으면 추가, 텍스트까지 완료)
        // 4. 완료 버튼을 누르면 현재 날씨 정보를 포함해서 음식 추천 화면으로 전환한다.

        // 마이 페이지 버튼 클릭 리스너
        imageButton_weather_myPage.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
        // 뒤로 가기 버튼 클릭 리스너
        imageButton_weather_back.setOnClickListener {
            finish()
        }
        // 완료 버튼 클릭 리스너
        button_wr_complete.setOnClickListener {
            lifecycleScope.launch {
                Toast.makeText(this@WeatherRecommendationActivity, "추천 결과 생성에 10-20초 정도 소요됩니다", Toast.LENGTH_SHORT).show()
                resultPopup()
            }
        }
    }


    // 1
    private fun checkLocationPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val fineLocationPermission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission =
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
            ) {
                return true
            } else {
                // 권한이 없으면 사용자에게 요청
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    1
                )
                return false
            }
        }
        return false
    }
    private fun requestLocationUpdates() {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // 위치 정보 성공적으로 가져오면 처리
                    if (location != null) {
                        latitude = location.latitude
                        longitude = location.longitude

                    } else {
                        Log.d("location", "Location not available")
                    }
                }
                .addOnFailureListener { e ->
                    // 위치 정보 가져오기 실패시 처리
                    Log.d("location_error", "Error getting location: ${e.message}")
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    // 2
    private fun getWeatherData(apiUrl: String, lat: Double, lon: Double, apiKey: String) {
        val url = "$apiUrl?lat=$lat&lon=$lon&appid=$apiKey&units=metric&lang=kr"

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        // 3
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseData = response.body?.string()
                if (responseData != null) {
                    val jsonObject = JSONObject(responseData)
                    var main =  jsonObject.getJSONArray("weather").getJSONObject(0).getString("main")
                    var description =  jsonObject.getJSONArray("weather").getJSONObject(0).getString("description")
                    var iconCode =  jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon")
                    var temp = jsonObject.getJSONObject("main").getDouble("temp")
                    var humidity = jsonObject.getJSONObject("main").getInt("humidity")
                    val cityName = jsonObject.getString("name")
                    var iconUrl = "https://openweathermap.org/img/wn/$iconCode.png"
                    Log.d("iconUrl", iconUrl)

                    cityNameView = findViewById(R.id.cityName)
                    tempView = findViewById(R.id.temp)
                    humidityView = findViewById(R.id.humidity)
                    descriptionView = findViewById(R.id.description)
                    iconView = findViewById(R.id.icon_view)


                    runOnUiThread {
                        var tempString = temp.toString()+"℃"
                        var humidityString = "습도 "+humidity.toString()+"%"
                        cityNameView.text = cityName
                        tempView.text = tempString
                        humidityView.text = humidityString
                        descriptionView.text = description
                        Glide.with(iconView.context)
                            .load(iconUrl)
                            .into(iconView)
                    }

                    Log.d("main", main)
                    Log.d("description", description)
                    Log.d("temp", temp.toString())
                    Log.d("humidity", humidity.toString())
                    Log.d("cityName", cityName)

                } else {
                    // 응답 데이터가 없을 경우 처리
                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                // 통신 실패 처리
                e.printStackTrace()
            }
        })
    }
    private suspend fun chatGPTRequest(msg: String): String {
        return suspendCoroutine { continuation ->
            lifecycleScope.launch {
                val chatGPTConnection = ChatGPTConnection()
                val result = chatGPTConnection.sendChatRequest(msg)
                continuation.resume(result)
            }
        }
    }
    private suspend fun resultPopup() {
        //다이얼로그 팝업 UI 초기화
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.popup_recommendation_result, null)

        button_wr_confirm =view.findViewById<Button>(R.id.button_rr_confirm)
        result_wr = view.findViewById<TextView>(R.id.textview_result)

        val temp = tempView.text.toString()
        val humidity = humidityView.text.toString()
        val description = descriptionView.text.toString()

        val intent = intent //전달할 데이터를 받을 Intent
        val userInfo = intent.getStringExtra("user_info")
        Log.d("filtering_test2",userInfo.toString())
        result_wr.text = chatGPTRequest(request_msg + " 오늘의 날씨 정보는 다음과 같아. 온도:$temp 습도:$humidity 기타정보:$description "+userInfo.toString())

        // 팝업 생성
        builder.setView(view)
        val dialog = builder.create()

        button_wr_confirm.setOnClickListener {
            dialog.dismiss() // 팝업 닫기
        }
        // 팝업 띄우기
        dialog.show()

    }
}