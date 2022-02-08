package com.example.cureya.community.ui.fragment.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PostDetailViewModel : ViewModel() {

    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

    private val auth = FirebaseAuth.getInstance()
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

    companion object {
        private const val TAG = "POST_DETAILS_VIEW_MODEL"
    }
}