package com.example.cureya.community.ui.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cureya.R
import com.example.cureya.community.models.Post
import com.example.cureya.community.models.TAG
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class PostRecyclerAdapter(
    val likePost: (String) -> Unit,
    val unlikePost: (String) -> Unit,
    val share: (Post) -> Unit,
    val onPostClick: (Post) -> Unit,
) : RecyclerView.Adapter<PostRecyclerAdapter.PostViewHolder>() {

    private val posts: MutableList<Post> = mutableListOf()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val originalPosts = mutableListOf<Post>()

    fun updateData(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        originalPosts.clear()
        originalPosts.addAll(posts)
        notifyDataSetChanged()
    }

    fun filterPosts(tag: TAG?) {
        this.posts.clear()
        if (tag != null) {
            this.posts.addAll(originalPosts.filter { it.tags[0] == tag })
        } else {
            this.posts.addAll(originalPosts)
        }
    }


    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profilePhoto: CircleImageView = itemView.findViewById(R.id.post_user_image)
        val userName: TextView = itemView.findViewById(R.id.post_card_username)
        val postTime: TextView = itemView.findViewById(R.id.post_card_time)
        val profession: TextView = itemView.findViewById(R.id.post_card_userprofession)
        val caption: TextView = itemView.findViewById(R.id.post_card_caption)
        val postImage: ImageView = itemView.findViewById(R.id.post_card_photo)
        val like: ImageView = itemView.findViewById(R.id.post_like)
        val likeCount: TextView = itemView.findViewById(R.id.post_card_like_count)
        val love: ImageView = itemView.findViewById(R.id.post_love)
        val loveCount: TextView = itemView.findViewById(R.id.post_card_love_count)
        val comment: ImageView = itemView.findViewById(R.id.post_comment)
        val menu: ImageView = itemView.findViewById(R.id.post_card_menu_button)
        val commentCount: TextView = itemView.findViewById(R.id.post_card_comment_count)
        val share: ImageView = itemView.findViewById(R.id.post_share)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return (PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.community_post_card, parent, false)
        ))
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        val isLiked = post.likes.contains(auth.uid!!)
        holder.apply {
            profilePhoto.load(post.profilePhoto)
            userName.text = post.userName
            postImage.load(post.photoUrl)
            Log.d(TAG, "onBindViewHolder: ${post.photoUrl}")
            caption.text = post.caption
            likeCount.text = post.likes.size.toString()
            commentCount.text = post.comments.size.toString()
            if(isLiked) like.setColorFilter(0xFF0000)
            share.setOnClickListener {
                share(post)
            }
            menu.setOnClickListener {

            }

            like.setOnClickListener {
                Log.d(TAG, "onBindViewHolder: ${post.postId}")
                if (isLiked) unlikePost(post.postId) else likePost(post.postId)
            }
            comment.setOnClickListener {

            }
            profilePhoto.setOnClickListener {

            }
        }
    }

    override fun getItemCount(): Int = this.posts.size

    companion object {
        private const val TAG = "POST_RECYCLER_ADAPTER"
    }
}