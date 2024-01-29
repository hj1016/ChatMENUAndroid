package com.example.androidtest

import android.os.AsyncTask
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONObject

class ChatGPTConnection : AsyncTask<String, Void, String>(){
    // Completions 엔드포인트
    //URL 형식: https://api.openai.com/v1/engines/{engine_id}/completions
    //엔진 식별자 ({engine_id}): 특정 GPT 모델의 엔진 식별자를 사용합니다. 예를 들어, text-davinci-003
    //메서드: POST
    //용도: 주어진 텍스트를 기반으로 모델이 다음 단어 또는 문장을 예측하여 생성
    private val apiKey = "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"
    val url = "https://api.openai.com/v1/engines/gpt-3.5-turbo-1106/completions"

    override fun doInBackground(vararg params: String?): String {
        val prompt = params[0]
        val json = JSONObject()
        //prompt: 모델에 전달할 텍스트 입력 또는 프롬프트입니다. 모델은 이를 기반으로 다음 단어나 문장을 예측하여 생성합니다.
        json.put("prompt", prompt)
        //max_tokens: 생성된 텍스트의 최대 토큰 수. 이를 통해 생성된 텍스트의 길이를 제어할 수 있습니다.
        json.put("max_tokens", 60)

        val requestBody = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        return response.body?.string() ?: ""

    }

    override fun onPostExecute(result: String) {
        // 결과 처리
        // result에 API 응답이 JSON 형식으로 포함되고, Log에 test 출력합니다
        Log.d("chatGPT_test", result)
    }
}

