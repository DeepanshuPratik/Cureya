package com.example.cureya.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {

    val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val _profile = MutableLiveData<Profile>();
    val profile: LiveData<Profile> get() = _profile
    private val storageRef = Firebase.storage.reference

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> get() = _error


    fun loadData(profileId: String) {
        listenForProfileDataChange(userId = profileId)
    }

    private fun listenForProfileDataChange(userId: String) {
        val profileValueListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val profile =
                        dataSnapshot.getValue(Profile::class.java)!!
                    profile.userId = FirebaseAuth.getInstance().uid!!
                    _profile.value = profile
                } else {

                }
            }
        }
        database.child("users").child(userId).addValueEventListener(profileValueListener)
    }

    fun editProfile(
        profilePicture: Uri? = null,
        about: String? = null,
        gender: GENDER? = null,
        email: String? = null
    ) {
        viewModelScope.launch {
            try {
                profilePicture?.let {
                    val imageRef =
                        storageRef.child("users/${profile.value!!.userId}/profilePictures/IMG${System.currentTimeMillis()}")
                    imageRef.putFile(it).await()
                    val url = imageRef.downloadUrl.await()
                    database.child(profile.value!!.userId).child("photoUrl").setValue(url).await()
                }
                about?.let {
                    database.child("users").child(profile.value!!.userId).child("about")
                        .setValue(it).await()
                }
                email?.let {
                    database.child("users").child(profile.value!!.userId).child("email")
                        .setValue(it).await()
                }
                gender?.let { database.child("users").child(profile.value!!.userId).child("gender")
                    .setValue(it).await() }
            } catch (e: Exception) {
                Log.e(TAG, "editPhoto: ", e)
            }
        }
    }

    companion object {
        private const val TAG = "PROFILE_VIEW_MODEL"
    }

    enum class GENDER {
        MALE, FEMALE, LGBTQ
    }
}