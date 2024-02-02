package com.example.androidtest

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ButtonAdapter(private val buttonNames: MutableList<String>, private val context: Context) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    private var dbHelper: DBHelper = DBHelper(context)
    class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_button, parent, false)
        return ButtonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.button.text = buttonNames[position]

        // RecyclerView 항목 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return buttonNames.size
    }
    fun addItem(item:String) {
        Log.d("buttonNames size", buttonNames.size.toString())
        buttonNames.add(item)
        notifyItemInserted(buttonNames.size - 1)
        dbHelper.updateAllergy(buttonNames.toTypedArray())
    }
     private fun removeItem(position: Int) {
         buttonNames.removeAt(position)
         notifyItemRemoved(position)
         notifyItemRangeChanged(position, buttonNames.size)
         dbHelper.updateAllergy(buttonNames.toTypedArray())
     }

}