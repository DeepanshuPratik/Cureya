package com.example.cureya.profile.personal

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cureya.R
import com.example.cureya.databinding.CreatePostFragmentBinding
import com.example.cureya.databinding.ProfileFragmentBinding

class PersonalProfile : Fragment() {

    companion object {
        fun newInstance() = PersonalProfile()
    }

    private lateinit var viewModel: PersonalProfileViewModel
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= ProfileFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this).get(PersonalProfileViewModel::class.java)
    }

}