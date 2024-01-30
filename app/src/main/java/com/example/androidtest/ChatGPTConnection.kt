import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ChatGPTConnection {

    private val apiKey = "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"
    private val url = "https://api.openai.com/v1/chat/completions"

    suspend fun sendChatRequest(prompt: String): String {
        return withContext(Dispatchers.IO) {
            executeRequest(prompt)
        }
    }

    private fun executeRequest(prompt: String): String {
        val json = JSONObject()
        json.put("model", "gpt-3.5-turbo")

        val messagesArray = JSONArray()
        messagesArray.put(JSONObject(mapOf("role" to "user", "content" to prompt)))
        json.put("messages", messagesArray)

        json.put("temperature", 0.7)

        //Log.d("ChapGPT_TEST1",json.toString())

        val requestBody = json.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(url)
            .header("Authorization", "Bearer $apiKey")
            .header("Content-Type", "application/json")
            .post(requestBody)
            .build()

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val response = client.newCall(request).execute()
        val responseBody = response.body?.string() ?: ""

        // JSON 파싱
        // JSON 파싱
        val jsonResponse = JSONObject(responseBody)
        val choicesArrayFromResponse = jsonResponse.optJSONArray("choices")
        val contentValue = choicesArrayFromResponse?.getJSONObject(0)?.getJSONObject("message")?.optString("content","")
        return contentValue ?: ""
    }
}
