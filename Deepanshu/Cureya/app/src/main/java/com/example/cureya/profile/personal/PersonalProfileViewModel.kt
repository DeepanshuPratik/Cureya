package com.example.cureya.profile.personal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cureya.profile.Profile
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PersonalProfileViewModel : ViewModel() {

    val database =
        FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
    private val _profile = MutableLiveData<Profile>();
    val profile: LiveData<Profile> get() = _profile

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
                    _profile.value = profile
                } else {

                }
            }
        }
        database.child("users").child(userId).addValueEventListener(profileValueListener)
    }
}