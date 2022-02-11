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
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsInformationBinding.inflate(inflater, container, false)

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            ourAim.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Aim)
            }
            aboutUs.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_about_Us)
            }
            ourMission.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Mission)
            }
            ourVision.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Vision)
            }
            ourAchievements.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Achievements)
            }
            ourCollabs.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Collaborations)
            }
            ourValueProposition.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_our_Value_Proposition)
            }
            teamCureya.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_team_Cureya)
            }
            dashboardBackButton.setOnClickListener {
                this@InformationFragment.findNavController().navigate(R.id.action_informationFragment_to_settingsFragment)
            }
        }


    }
}