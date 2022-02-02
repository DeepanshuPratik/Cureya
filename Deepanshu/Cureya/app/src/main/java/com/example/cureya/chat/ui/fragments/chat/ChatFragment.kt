package com.example.cureya.chat.ui.fragments.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.cureya.R
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.repository.ChatRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbReference =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
    }

    companion object {
        private const val TAG = "CHAT_FRAGMENT"
    }
}