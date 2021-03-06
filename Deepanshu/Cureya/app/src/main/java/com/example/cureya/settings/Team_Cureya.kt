package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.TeamCureyaBinding

class Team_Cureya: Fragment() {
    private lateinit var binding: TeamCureyaBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TeamCureyaBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dashboardBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_team_Cureya_to_informationFragment)
            }
        }

    }
}