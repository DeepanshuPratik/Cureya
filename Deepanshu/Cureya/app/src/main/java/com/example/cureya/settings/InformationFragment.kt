package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.FragmentSettingsInformationBinding

class InformationFragment : Fragment() {

    private lateinit var binding: FragmentSettingsInformationBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsInformationBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ourAim.setOnClickListener {
            navController.navigate(R.id.action_our_Aim_to_informationFragment)
        }
        binding.aboutUs.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_about_Us)
        }
        binding.ourMission.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_our_Mission)
        }
        binding.ourVision.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_our_Vision)
        }
        binding.ourAchievements.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_our_Achievements)
        }
        binding.ourCollabs.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_our_Collaborations)
        }
        binding.ourValueProposition.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_our_Value_Proposition)
        }
        binding.teamCureya.setOnClickListener {
            navController.navigate(R.id.action_informationFragment_to_team_Cureya)
        }

    }
}