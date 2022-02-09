package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentSettingsFaqsBinding

class FaqFragment : Fragment() {

    private lateinit var binding: FragmentSettingsFaqsBinding
    private var buttonClickCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsFaqsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.columnAccessButton.setOnClickListener {
            if (buttonClickCount % 2 != 0) {
                binding.answerTextView.visibility = View.VISIBLE
            } else {
                binding.answerTextView.visibility = View.GONE
            }
            buttonClickCount++
        }
    }*/
}