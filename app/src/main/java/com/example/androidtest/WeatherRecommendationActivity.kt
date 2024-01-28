package com.example.androidtest

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import android.widget.TextView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


class WeatherRecommendationActivity : AppCompatActivity() {

    // 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 37.5665
    private var longitude: Double = 126.9780

    // 2
    private val apiKey = "6a83eb4b37a279a7d643253d454ff40e"
    private val apiUrl = "https://api.openweathermap.org/data/2.5/weather"

    lateinit var cityNameView: TextView
    lateinit var tempView: TextView
    lateinit var humidityView: TextView
    lateinit var descriptionView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_recommendation)

        // 1. GPS 정보를 가져온다. => 위도, 경도
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (checkLocationPermission()) {
            requestLocationUpdates()
        }

        // 2. api 를 이용해서 날씨 정보를 가져온다.=> 도시이름, 날씨(온도, 습도, 눈, 비,....)
        getWeatherData(apiUrl, latitude, longitude, apiKey)

        // 3. 그래픽과 텍스트로 현재 날씨를 화면에 표시
        // 4. 완료 버튼을 누르면 현재 날씨 정보를 포함해서 음식추천 화면으로 전환한다.
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
                    var temp = jsonObject.getJSONObject("main").getDouble("temp")
                    var humidity = jsonObject.getJSONObject("main").getInt("humidity")
                    val cityName = jsonObject.getString("name")

                    cityNameView = findViewById(R.id.cityName)
                    tempView = findViewById(R.id.temp)
                    humidityView = findViewById(R.id.humidity)
                    descriptionView = findViewById(R.id.description)

                    runOnUiThread {
                        var tempString = temp.toString()+"℃"
                        var humidityString = "습도 "+humidity.toString()+"%"
                        cityNameView.text = cityName
                        tempView.text = tempString
                        humidityView.text = humidityString
                        descriptionView.text = description
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
}