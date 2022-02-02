package com.example.cureya.chat.ui.fragments.chatList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.example.cureya.chat.data.models.ChatUser
import com.example.cureya.chat.data.models.User
import com.example.cureya.chat.ui.adapters.AllUsersRecyclerAdapter
import com.example.cureya.chat.ui.adapters.ChatUsersRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ChatListFragment : Fragment() {

    private var allUsers: List<User> = listOf()
    private var chatUsers: List<ChatUser> = listOf()

    private lateinit var allUsersRecycler: RecyclerView
    private lateinit var chatUsersRecycler: RecyclerView

    private lateinit var allUsersAdapter: AllUsersRecyclerAdapter
    private lateinit var chatUsersAdapter: ChatUsersRecyclerAdapter

    private val chatListViewModel: ChatListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.chat_list_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers(view)
        setLayoutManager()
        setAdapters()
        observeData()
    }

    private fun initMembers(view: View) {
        allUsersRecycler = view.findViewById(R.id.all_users_recycler)
        chatUsersRecycler = view.findViewById(R.id.chat_users_recycler)
        allUsersAdapter = AllUsersRecyclerAdapter(requireContext()) {

        }
    }

    private fun observeData() {
        chatListViewModel.getAllUsers().observe(viewLifecycleOwner) {
            allUsersAdapter.updateData(it)
        }
    }

    private fun setAdapters() {
        allUsersRecycler.adapter = allUsersAdapter
//        chatUsersRecycler.adapter = chatUsersAdapter
    }

    private fun setLayoutManager() {
        allUsersRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        private const val TAG = "CHAT_LIST_FRAGMENT"
    }

}