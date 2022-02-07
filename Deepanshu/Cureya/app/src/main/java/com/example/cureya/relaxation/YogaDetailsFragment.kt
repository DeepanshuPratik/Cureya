package com.example.cureya.relaxation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentRelaxationYogaDetailsBinding

class YogaDetailsFragment : Fragment() {

    private lateinit var binding: FragmentRelaxationYogaDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationYogaDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
}