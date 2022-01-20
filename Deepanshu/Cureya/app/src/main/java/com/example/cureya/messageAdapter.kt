package com.example.cureya

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsListAdapter(): RecyclerView.Adapter<MessageViewHolder>() {

    private val items: ArrayList<messageInfo> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_layout, parent, false)
        val viewHolder = MessageViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentItem= items[position]
        holder.author.text= currentItem.sender
        holder.message.text = currentItem.message
        if(currentItem.message.isEmpty()) {
            currentItem.message = " "
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

}
class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val author: TextView = itemView.findViewById(R.id.username)
    val message : TextView = itemView.findViewById(R.id.message)
}
