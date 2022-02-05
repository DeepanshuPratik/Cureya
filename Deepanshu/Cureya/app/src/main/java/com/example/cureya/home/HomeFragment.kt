package com.example.cureya.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.homeContextualMenu.setOnClickListener { showMenuPopUp(it) }
    }

    private fun showMenuPopUp(view: View) {
        PopupMenu(context, view).apply {
            setOnMenuItemClickListener { p0 ->
                when (p0?.itemId) {
                    R.id.settings -> {
                        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }
                    R.id.about_us -> {
                        findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
                        true
                    }
                    R.id.log_out -> {
                        auth.signOut()
                        findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
                        true
                    }
                    R.id.profile -> false
                    R.id.moods -> false
                    R.id.contact_us -> false
                    R.id.report -> false
                    else -> false
                }
            }
            inflate(R.menu.home_contextual_menu)
            show()
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
        }
    }
}