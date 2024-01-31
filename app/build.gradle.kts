plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.example.androidtest"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.androidtest"
        minSdk = 33
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    // databinding 혹은 viewbinding을 사용하기 위해 추가 (둘 중 가능한 것으로 하나 채택할 예정)
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //기본
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.0")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // WeatherRecommendationActivity에서 아래 두 implementation 사용
    implementation("com.google.android.gms:play-services-location:21.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // DB Helper에서 사용
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")





}