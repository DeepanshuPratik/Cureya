package com.example.cureya.chat.ui.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cureya.R
import com.example.cureya.chat.data.models.User
import com.example.cureya.chat.ui.adapters.ChatRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var user: User
    private val chatViewModel: ChatViewModel by viewModels()

    private lateinit var messageEditText: EditText
    private lateinit var sendButton: ImageView
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var chatRecyclerAdapter: ChatRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_one_on_one_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers(view)
        chatViewModel.loadChats(user.userId!!)
        setupListeners(view)
        setupRecycler()
        observeData()
        scrollToBottom()
    }

    private fun initMembers(view: View) {
        dbReference =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        auth = FirebaseAuth.getInstance()
        user = ChatFragmentArgs.fromBundle(requireArguments()).user
        view.findViewById<TextView>(R.id.user_name).text = user.name
        Glide.with(this).load(user.photoUrl).into(view.findViewById(R.id.profile))
        messageEditText = view.findViewById(R.id.chatbar)
        sendButton = view.findViewById(R.id.send_button)
        chatRecyclerView = view.findViewById(R.id.chat_recycler)
        chatRecyclerAdapter = ChatRecyclerAdapter(auth.uid!!)
    }

    private fun setupRecycler() {
        chatRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        chatRecyclerView.adapter = chatRecyclerAdapter
    }

    private fun setupListeners(view: View) {
        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            findNavController().popBackStack()
        }
        messageEditText.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                sendButton.visibility = View.VISIBLE
            } else {
                sendButton.visibility = View.GONE
            }
        }
        sendButton.setOnClickListener { sendMessage() }
    }

    private fun sendMessage() {
        lifecycleScope.launch {
            val text = messageEditText.text.toString()
            if (text.isEmpty() || text.isBlank()) {
                Toast.makeText(requireContext(), "Please provide a message", Toast.LENGTH_SHORT)
                    .show()
            } else {
                chatViewModel.sendMessage(text, user.userId!!)
            }
            messageEditText.text.clear()
            scrollToBottom()
        }
    }

    private fun scrollToBottom() =
        chatRecyclerView.scrollToPosition(chatRecyclerAdapter.itemCount - 1)

    private fun observeData() {
        chatViewModel.getChats().observe(viewLifecycleOwner) {
            chatRecyclerAdapter.updateData(it.messages)
        }
    }

    companion object {
        private const val TAG = "CHAT_FRAGMENT"
    }
}