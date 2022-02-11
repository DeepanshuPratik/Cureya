package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.OurCollaborationsBinding

class Our_Collaborations:Fragment() {
    private lateinit var binding: OurCollaborationsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = OurCollaborationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            dashboardBackButton.setOnClickListener {
                findNavController().navigate(R.id.action_our_Collaborations_to_informationFragment)
            }
        }

    }
}