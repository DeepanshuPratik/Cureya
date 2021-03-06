package com.example.cureya.chat.ui.adapters

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cureya.R
import com.example.cureya.chat.data.models.User
import com.example.cureya.chat.utils.toDateString

class AllUsersRecyclerAdapter(
    private val context: Context,
    private val onClick: (User) -> Unit,
    private val onProfileClick : (String)->Unit
) :
    RecyclerView.Adapter<AllUsersRecyclerAdapter.AllUsersViewHolder>() {
    private val users: MutableList<User> = mutableListOf()
    fun updateData(users: List<User>) {
        this.users.clear()
        notifyDataSetChanged()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    inner class AllUsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.profile_pic)
        val userName: TextView = itemView.findViewById(R.id.account_name)
        val message: TextView = itemView.findViewById(R.id.message_content)
        val messageTime: TextView = itemView.findViewById(R.id.message_time)
        val greenTick: FrameLayout = itemView.findViewById(R.id.account_verification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllUsersViewHolder {
        val v: View =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_vertical_recycler_view, parent, false)
        return AllUsersViewHolder(v)
    }

    override fun onBindViewHolder(holder: AllUsersViewHolder, position: Int) {
        holder.userName.text = users[position].name
        Glide.with(context).load(users[position].photoUrl).into(holder.userImage);
        holder.message.text = users[position].lastMessage?.text
        holder.messageTime.text = users[position].lastMessage?.createdAt?.toDateString()
        holder.greenTick.visibility = if (users[position].isCounselor) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onClick(users[position]) }
        holder.userImage.setOnClickListener {  }
    }

    override fun getItemCount(): Int {
        return this.users.size
    }

    companion object {
        private const val TAG = "AllUsersRecyclerAdapter"
    }

}