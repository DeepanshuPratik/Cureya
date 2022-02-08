package com.example.cureya.relaxation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentRelaxationMusicBinding
import com.example.cureya.databinding.FragmentRelaxationYogaBinding

class MusicFragment : Fragment() {

    private lateinit var binding: FragmentRelaxationMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationMusicBinding.inflate(inflater, container, false)
        return binding.root
    }
}