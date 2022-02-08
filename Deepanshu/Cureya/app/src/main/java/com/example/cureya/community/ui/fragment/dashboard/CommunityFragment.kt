package com.example.cureya.community.ui.fragment.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cureya.R
import com.example.cureya.community.ui.adapters.PostRecyclerAdapter
import com.example.cureya.databinding.CommunityDashboardBinding


class CommunityFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var navController: NavController
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    private var _binding: CommunityDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CommunityDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        initUi()
        setClickListeners()
        observeData()
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)
        navController = findNavController()
        postRecyclerAdapter = PostRecyclerAdapter(
            {
            viewModel.likePost(it)
            }, {
               viewModel.unlikePost(it)
            }, {

            }, {

            }

        )
    }

    private fun initUi() {
        binding.postRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.postRecyclerView.adapter = postRecyclerAdapter
    }

    private fun setClickListeners() {
        binding.dashboardHeader.dashboardMenu
            .setOnClickListener(dashboardMenuClickListener)
    }

    private fun observeData() {
        viewModel.getPosts().observe(viewLifecycleOwner) {
            postRecyclerAdapter.updateData(it)
        }

        viewModel.filter.observe(viewLifecycleOwner) {
            postRecyclerAdapter.filterPosts(it)
        }
    }

    private val dashboardMenuClickListener = View.OnClickListener {
        val popup = PopupMenu(requireContext(), it).apply {
            menuInflater.inflate(R.menu.community_menu, menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.community_menu_post -> {
                        navController.navigate(R.id.action_dashboardFragment_to_createPost)
                    }
                    R.id.community_menu_report -> {

                    }
                }
                true
            }
        }
        popup.show()
    }
}