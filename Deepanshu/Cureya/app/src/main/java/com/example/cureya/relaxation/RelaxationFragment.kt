package com.example.cureya.relaxation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.FragmentRelaxationBinding

class RelaxationFragment : Fragment() {

    private lateinit var binding: FragmentRelaxationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.yogaFrame.setOnClickListener {
            findNavController().navigate(R.id.action_relaxationFragment_to_yogaFragment)
        }
        binding.musicFrame.setOnClickListener {
            findNavController().navigate(R.id.action_relaxationFragment_to_musicFragment)
        }
        binding.videoFrame.setOnClickListener {
            findNavController().navigate(R.id.action_relaxationFragment_to_videoFragment)
        }
    }
}