package com.example.cureya.profile.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cureya.community.utils.toDateString
import com.example.cureya.databinding.ProfileFragmentBinding

class PersonalProfile : Fragment() {

    companion object {
        fun newInstance() = PersonalProfile()
    }

    private lateinit var viewModel: PersonalProfileViewModel
    private lateinit var navController: NavController
    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setClickListeners()
        observeData()
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this)[PersonalProfileViewModel::class.java]
        val profileId = PersonalProfileArgs.fromBundle(requireArguments()).userId
        navController=findNavController()
        viewModel.loadData(profileId)
    }

    private fun setClickListeners() {
        binding.apply {
            binding.btnBack.setOnClickListener { navController.popBackStack() }
        }
    }

    private fun observeData() {
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.apply {
                binding.profilePhoto.load(profile.photoUrl)
                profileEmail.text = profile.email
                profileJoined.text = profile.joinedCureya.toDateString()
                profileGender.text = profile.gender
                profileAbout.text = profile.about
                textJoinedGroups.text = profile.joinedGroups.toString()
            }
        }
    }

}