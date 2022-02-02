package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.ktx.Firebase

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.apply {
            myAccountFrame.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_accountFragment)
            }
            informationFrame.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_informationFragment)
            }
            faqFrame.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_faqFragment)
            }
            reportFrame.setOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_reportFragment)
            }
            signOutFrame.setOnClickListener {
                auth.signOut()
                findNavController().navigateUp()
            }
        }
    }
}