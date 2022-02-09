package com.example.cureya.community.ui.fragment.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cureya.R
import com.example.cureya.community.models.Comment
import com.example.cureya.community.utils.toDateString

class CommentRecyclerAdapter : RecyclerView.Adapter<CommentRecyclerAdapter.CommentsViewHolder>() {

    private val comments = mutableListOf<Comment>()
    fun updateData(comments: List<Comment>) {
        this.comments.clear()
        this.comments.addAll(comments)
        notifyDataSetChanged()
    }

    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.comment_user_image)
        val userName: TextView = itemView.findViewById(R.id.comment_user_name)
        val time: TextView = itemView.findViewById(R.id.comment_time)
        val text: TextView = itemView.findViewById(R.id.comment_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val comment = comments[position]
        holder.apply {
            userImage.load(comment.photoUrl)
            userName.text = comment.userName
            time.text = comment.createdAt.toDateString()
            text.text = comment.text
        }
    }

    override fun getItemCount(): Int = this.comments.size
}