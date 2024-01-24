package com.example.androidtest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView

class ChatRecommendationActivity : AppCompatActivity() {

    lateinit var listView_chat_list: ListView

    lateinit var button_chat_send: Button // 전송 버튼
    lateinit var editText_chat_input: EditText // 채팅 입력 칸

    var chat_list = arrayListOf<String>() // 내 채팅 내역을 담을 ArrayList
    val chatListAdapter = ChatListAdapter(this, chat_list) // 어댑터 초기화

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_recommendation)

        listView_chat_list = findViewById(R.id.listView_chat_list)
        button_chat_send = findViewById(R.id.button_chat_send)
        editText_chat_input = findViewById(R.id.editText_chat_input)

        listView_chat_list.adapter = chatListAdapter // 리스트 뷰와 어댑터를 연결

        // 전송 버튼 클릭 리스너
        button_chat_send.setOnClickListener {
            addBubbleRight()
            editText_chat_input.setText("") // 에디트 텍스트 초기화
        }
    }

    // 내 메시지 말풍선 텍스트 뷰 동적 생성
    fun addBubbleRight() {
        // 텍스트 뷰에 들어갈 String 생성
        val text = editText_chat_input.text.toString()

        // 채팅 리스트에 추가
        chat_list.add(text)

        // 리스트가 변동됨을 Adapter에 알림
        chatListAdapter.notifyDataSetChanged()
    }

    // ChatGPT의 메시지 말풍선 텍스트 뷰 동적 생성
    fun addBubbleLeft() {
        // 텍스트 뷰에 들어갈 String 생성
        // val text = ChatGPT의 메시지

        // 채팅 리스트에 추가
        // chat_list.add(text)

        // 리스트가 변동됨을 Adapter에 알림
        // chatListAdapter.notifyDataSetChanged()
    }
}

class ChatListAdapter (val context: Context, val chat_list: ArrayList<String>): BaseAdapter() {
    override fun getCount(): Int {
        return chat_list.size
    }

    override fun getItem(p0: Int): Any {
        return chat_list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        // LayoutInflater: item을 Adapter에서 사용할 View로 inflate
        val view: View = LayoutInflater.from(context).inflate(R.layout.textview_chat_right, null)

        // 위에서 생성된 view를 textView_chat_right.xml 파일의 view와 연결
        val textView_chat_right = view.findViewById<TextView>(R.id.textView_chat_right)

        // chat_list의 변수의 데이터를 TextView에 저장
        val chat = chat_list[p0]
        textView_chat_right.text = chat

        return view
    }
}