package com.example.cureya.community.ui.fragment.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.community.models.Comment
import com.example.cureya.community.models.Post
import com.example.cureya.community.models.User
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PostDetailViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val auth = FirebaseAuth.getInstance()

    private val _comments = MutableLiveData<List<Comment>>()
    val comment: LiveData<List<Comment>> get() = _comments

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> get() = _post

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        loadUser()
    }

    fun initData(post: Post) {
        _post.value = post
        listenForPostValueChange(postId = post.postId)
        loadComment(postId = post.postId)
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val user =
                    database.child("users").child(auth.uid!!).get().await()
                        .getValue(User::class.java)!!
                user.userId = auth.uid!!
                Log.e(TAG, "loadUser: ${user}", )
                _currentUser.value = user
            } catch (e: Exception) {
                Log.e(TAG, "loadUser:", e)
            }

        }
    }

    private fun listenForPostValueChange(postId: String) {
        val postsValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val currentPost = dataSnapshot.getValue(Post::class.java)!!
                    _post.value = currentPost
                } else {

                }
            }
        }
        database.child("community").child("posts").child(postId)
            .addValueEventListener(postsValueListener)
    }

    fun postComment(commentText: String) {
        viewModelScope.launch {
            val postId = post.value!!.postId
            try {
                val comment = Comment(
                    userId = auth.uid!!,
                    text = commentText,
                    photoUrl = _currentUser.value!!.photoUrl,
                    userName = _currentUser.value!!.name
                )
                val cc = database.child("community").child("posts")
                    .child(postId).child("commentCount").get().await().getValue(Int::class.java)
                    ?: 0
                database.child("community").child("posts")
                    .child(postId).child("commentCount").setValue(cc + 1)
                database.child("community").child("posts").child(postId)
                    .child("comments").push().setValue(comment)
            } catch (e: Exception) {
                Log.e(TAG, "postComment: ", e)
            }

        }
    }

    private fun loadComment(postId: String) {
        viewModelScope.launch {
            val commentList =
                database.child("community").child("posts").child(postId).child("comments")
                    .get().await().children.map { it.getValue(Comment::class.java)!! }.reversed()
            _comments.value = commentList
            listenToCommentChanges(postId)
        }
    }

    private fun listenToCommentChanges(postId: String) {
        val postsValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val commentList =
                        dataSnapshot.children.map { it.getValue(Comment::class.java)!! }.reversed()
                    _comments.value = commentList
                } else {

                }
            }
        }
        database.child("community").child("posts").child(postId).child("comments")
            .addValueEventListener(postsValueListener)
    }


    fun likePost() {
        viewModelScope.launch {
            try {
                val postId = post.value!!.postId
                val t: GenericTypeIndicator<MutableList<String>> =
                    object : GenericTypeIndicator<MutableList<String>>() {}
                val likes = database.child("community").child("posts").child(postId).child("likes")
                    .get().await().getValue(t)
                    ?: mutableListOf()
                likes.add(auth.uid!!)
                database.child("community").child("posts").child(postId).child("likes")
                    .setValue(likes)
            } catch (e: FirebaseException) {
                Log.e(TAG, "likePost: ", e)
            }
        }
    }

    fun unlikePost() {
        viewModelScope.launch {
            try {
                val postId = post.value!!.postId
                val t: GenericTypeIndicator<MutableList<String>> =
                    object : GenericTypeIndicator<MutableList<String>>() {}
                val likes = database.child("community").child("posts").child(postId).child("likes")
                    .get().await().getValue(t)
                    ?: mutableListOf()
                likes.remove(auth.uid!!)
                database.child("community").child("posts").child(postId).child("likes")
                    .setValue(likes)
            } catch (e: FirebaseException) {
                Log.e(TAG, "unlikePost: ", e)
            }
        }
    }

    companion object {
        private const val TAG = "POST_DETAILS_VIEW_MODEL"
    }
}