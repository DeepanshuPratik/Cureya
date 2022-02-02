package com.example.cureya.chat.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.example.cureya.chat.data.models.Chat
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.models.User

class ChatRecyclerAdapter(
    private val senderId: String
) : RecyclerView.Adapter<ChatRecyclerAdapter.ChatMessageViewHolder>() {
    private val messages: MutableList<Message> = mutableListOf()
    fun updateData(messages: List<Message>) {
        this.messages.clear()
        notifyDataSetChanged()
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }


    inner class ChatMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderMessageView: TextView = itemView.findViewById(R.id.sender_message)
        val receiverMessageView: TextView = itemView.findViewById(R.id.receiver_message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.message_item, parent, false)
        return ChatMessageViewHolder(v)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val message = messages[position]
        if (message.senderId == senderId) {
            holder.receiverMessageView.visibility = View.GONE
            holder.senderMessageView.text = message.text
        } else {
            holder.senderMessageView.visibility = View.GONE
            holder.receiverMessageView.text = message.text
        }
    }

    override fun getItemCount(): Int {
        return this.messages.size
    }
}