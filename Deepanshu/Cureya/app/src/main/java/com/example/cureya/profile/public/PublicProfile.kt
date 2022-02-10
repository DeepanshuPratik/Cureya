package com.example.cureya.profile.public

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cureya.databinding.ProfileFragmentBinding

class PublicProfile : Fragment() {

    private lateinit var viewModel: PublicProfileViewModel

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this).get(PublicProfileViewModel::class.java)
    }


}