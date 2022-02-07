package com.example.cureya.relaxation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentRelaxationVideoBinding

class VideoFragment : Fragment() {

    private lateinit var binding: FragmentRelaxationVideoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationVideoBinding.inflate(inflater, container, false)
        return binding.root
    }
}