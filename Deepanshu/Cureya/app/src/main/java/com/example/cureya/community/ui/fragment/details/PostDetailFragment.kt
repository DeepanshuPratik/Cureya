package com.example.cureya.community.ui.fragment.details

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cureya.R

class PostDetailFragment : Fragment() {

    companion object {
        fun newInstance() = PostDetailFragment()
    }

    private lateinit var viewModel: PostDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.post_detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PostDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}