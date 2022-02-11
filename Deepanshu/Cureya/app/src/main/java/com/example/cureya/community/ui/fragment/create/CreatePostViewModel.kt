package com.example.cureya.community.ui.fragment.create

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cureya.community.models.Post
import com.example.cureya.community.models.TAG
import com.example.cureya.community.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class CreatePostViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val storageRef = Firebase.storage.reference

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error

    private val _tag = MutableLiveData<TAG?>()
    val tag: LiveData<TAG?> get() = _tag

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            try {
                val user =
                    database.child("users").child(auth.uid!!).get().await()
                        .getValue(User::class.java)!!
                user.userId = auth.uid!!
                _currentUser.value = user
            } catch (e: Exception) {
                Log.e(TAG, "loadUser:", e)
            }
        }
    }


    fun setTag(tag: TAG) {
        _tag.value = tag
    }

    fun createPost(imageUri: Uri, caption: String, tags: List<TAG>) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val postId = "${auth.uid!!}${System.currentTimeMillis()}"
                val imageRef =
                    storageRef.child("community/post/${auth.uid!!}/IMG000${System.currentTimeMillis()}")
                imageRef.putFile(imageUri).await()
                val url = imageRef.downloadUrl.await()
                val post = Post(
                    postId = postId,
                    caption = caption,
                    userId = auth.uid!!,
                    likes = listOf(),
                    commentCount = 0,
                    shares = 0,
                    createdAt = Date(),
                    userName = auth.currentUser!!.displayName!!,
                    photoUrl = url.toString(),
                    profilePhoto = auth.currentUser!!.photoUrl!!.toString(),
                    tags = tags
                )
                database.child("community").child("posts")
                    .child(postId)
                    .setValue(post).await()
            } catch (e: Exception) {
                _error.value = e.message
                Log.e(TAG, "createPost: ", e)
            }
            _isLoading.value = false
        }
    }

    companion object {
        private const val TAG = "CREATE_POST_VIEW_MODEL"
    }
}