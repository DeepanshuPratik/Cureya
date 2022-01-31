package com.example.cureya.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import com.example.cureya.R
import com.example.cureya.chat.data.models.Message
import com.example.cureya.chat.data.repository.ChatRepository
import com.example.cureya.chat.data.repository.ChatRepositoryImpl
import com.example.cureya.chat.data.repository.UserRepositoryImpl
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbReference =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        val chatRepo = ChatRepositoryImpl(dbReference)
        val userRepo = UserRepositoryImpl(dbReference)

//        view.findViewById<ImageButton>(R.id.back_button).setOnClickListener {
//            lifecycleScope.launch {
//                repo.sendMessage(
//                    Message(
//                        text = "hello",
//                        senderId = "sender",
//                        receiverId = "receiver",
//                    )
//                )
//            }
//        }
    }
}