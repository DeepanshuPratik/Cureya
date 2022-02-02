package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
}