package com.example.cureya.community.ui.fragment.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.community.models.Post
import com.example.cureya.community.models.TAG
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class DashboardViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    private val auth = FirebaseAuth.getInstance()
    private val posts = MutableLiveData<List<Post>>();
    private val _filter = MutableLiveData<TAG?>(null)
    val filter: LiveData<TAG?> get() = _filter

    fun getPosts(): LiveData<List<Post>> = posts;

    init {
        loadPosts()
    }

    fun likePost(postId: String) {
        viewModelScope.launch {
            try {
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

    fun unlikePost(postId: String) {
        viewModelScope.launch {
            try {
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

    private fun loadPosts() {
        viewModelScope.launch {
            database.child("community").child("posts")
                .get().await().children.map { it.getValue(Post::class.java)!! }.reversed()
                .let {
                    posts.value = it
                }
            listenForPostsValueChange()
        }
    }


    private fun listenForPostsValueChange() {
        val postsValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val postList =
                        dataSnapshot.children.map { it.getValue(Post::class.java)!! }.reversed()
                    posts.value = postList
                } else {

                }
            }
        }
        database.child("community").child("posts").addValueEventListener(postsValueListener)
    }

    companion object {
        private const val TAG = "DASHBOARD_VIEWMODEL"
    }

}