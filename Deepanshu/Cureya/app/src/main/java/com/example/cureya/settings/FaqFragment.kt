package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentSettingsFaqsBinding

class FaqFragment : Fragment() {

    private lateinit var binding: FragmentSettingsFaqsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsFaqsBinding.inflate(inflater, container, false)
        return binding.root
    }
}