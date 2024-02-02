package com.example.androidtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ButtonAdapter(private val buttonNames: MutableList<String>) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {
//
//    // 클릭 이벤트 리스너 인터페이스
//    interface OnItemClickListener {
//        fun onItemClick(position: Int)
//    }
//
//    // 리스너 변수
//    private var itemClickListener: OnItemClickListener? = null
//
//    // 외부에서 리스너 설정을 위한 메서드
//    fun setOnItemClickListener(listener: OnItemClickListener) {
//        itemClickListener = listener
//    }
//
//    // DBHelper 변수 추가
//    private lateinit var dbHelper: DBHelper
//
//    // 생성자에서 DBHelper 초기화
//    init {
//        this.dbHelper = dbHelper
//    }
//
//   // 버튼 삭제하면 뷰, DB에서 모두 삭제
//    fun deleteButton(position: Int) {
//        val deletedItem = buttonNames[position]
//
//        // DB에서 삭제
//
//        // 리스트에서 삭제
//        buttonNames.removeAt(position)
//        notifyItemRemoved(position)
//        notifyItemRangeChanged(position, itemCount)
//    }
//

    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
        return ButtonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.button.text = buttonNames[position]

//        // RecyclerView 항목 클릭 이벤트 처리
//        holder.itemView.setOnClickListener {
//            // 클릭된 항목의 위치를 인터페이스를 통해 외부로 전달
//            itemClickListener?.onItemClick(position)
//        }
    }

    override fun getItemCount(): Int {
        return buttonNames.size
    }

}