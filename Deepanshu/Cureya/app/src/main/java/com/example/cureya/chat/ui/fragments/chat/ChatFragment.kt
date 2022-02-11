package com.example.cureya.chat.ui.fragments.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.cureya.chat.data.models.User
import com.example.cureya.chat.ui.adapters.ChatRecyclerAdapter
import com.example.cureya.databinding.FragmentOneOnOneChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference
    private lateinit var user: User
    private val chatViewModel: ChatViewModel by viewModels()

    private var _binding: FragmentOneOnOneChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatRecyclerAdapter: ChatRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOneOnOneChatBinding.inflate(inflater, container, false)
        return binding.root
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
        chatRecyclerAdapter = ChatRecyclerAdapter(auth.uid!!)
        binding.apply {
            userName.text = user.name
            profile.load(user.photoUrl)

        }
    }

    private fun setupRecycler() {
        binding.apply {
            chatRecycler.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            chatRecycler.adapter = chatRecyclerAdapter
        }
    }

    private fun setupListeners(view: View) {
        binding.apply {
            backButton.setOnClickListener {
                findNavController().popBackStack()
            }
            chatbar.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    sendButton.visibility = View.VISIBLE
                } else {
                    sendButton.visibility = View.GONE
                }
            }
            sendButton.setOnClickListener { sendMessage() }
        }
    }

    private fun sendMessage() {
        lifecycleScope.launch {
            val text = binding.chatbar.text.toString()
            if (text.isEmpty() || text.isBlank()) {
                Toast.makeText(requireContext(), "Please provide a message", Toast.LENGTH_SHORT)
                    .show()
            } else {
                chatViewModel.sendMessage(text, user.userId!!)
            }
            binding.chatbar.text.clear()
            scrollToBottom()
        }
    }

    private fun scrollToBottom() =
        binding.chatRecycler.scrollToPosition(chatRecyclerAdapter.itemCount - 1)

    private fun observeData() {
        chatViewModel.getChats().observe(viewLifecycleOwner) {
            chatRecyclerAdapter.updateData(it.messages)
        }
    }

    companion object {
        private const val TAG = "CHAT_FRAGMENT"
    }
}