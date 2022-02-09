package com.example.cureya.community.ui.fragment.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cureya.R
import com.example.cureya.community.models.TAG
import com.example.cureya.databinding.CommunityDashboardBinding
import com.google.firebase.auth.FirebaseAuth


class CommunityFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var navController: NavController
    private lateinit var postRecyclerAdapter: PostRecyclerAdapter

    private var _binding: CommunityDashboardBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

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
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        navController = findNavController()
        postRecyclerAdapter = PostRecyclerAdapter(
            {
                viewModel.likePost(it)
            }, {
                viewModel.unlikePost(it)
            }, {

            }, {
                val direction = CommunityFragmentDirections.actionDashboardFragmentToPostDetailFragment(it)
                navController.navigate(direction)
            },
            { view, post ->
                val popup = PopupMenu(requireContext(), view).apply {
                    if (post.userId == auth.uid!!)
                        menuInflater.inflate(R.menu.post_menu, menu)
                    else
                        menuInflater.inflate(R.menu.post_menu_public, menu)

                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.post_menu_share -> {

                            }
                            R.id.post_menu_delete -> {
                                AlertDialog.Builder(requireContext()).apply {
                                    setTitle(
                                        "Are you sure you want\n" +
                                                " to delete this post?"
                                    )
                                    setPositiveButton("Delete") { dialog, _ ->
                                        viewModel.deletePost(post)
                                        dialog.dismiss()
                                    }
                                    setNegativeButton("Cancel") { dialog, id ->
                                        dialog.cancel()
                                    }
                                    setCancelable(false)
                                }.create().show()
                            }
                        }
                        true
                    }
                }
                popup.show()
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
        binding.tagPicker.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            Log.d(DTAG, "setClickListeners: ${group.checkedChipIds} ${checkedId}")
            when (checkedId) {
                binding.tagPicker.chipAnxiety.id -> viewModel.filter.value = TAG.ANXIETY
                binding.tagPicker.chipDepression.id -> viewModel.filter.value = TAG.DEPRESSION
                binding.tagPicker.chipParanoia.id -> viewModel.filter.value = TAG.PARANOIA
                binding.tagPicker.chipPsychosis.id -> viewModel.filter.value = TAG.PSYCHOSIS
                binding.tagPicker.chipStress.id -> viewModel.filter.value = TAG.STRESS
                -1 -> viewModel.filter.value = null
            }
        }

    }

    private fun observeData() {
        viewModel.posts.observe(viewLifecycleOwner) {
            postRecyclerAdapter.updateData(it)
        }

        viewModel.filter.observe(viewLifecycleOwner) { tag ->
            if (viewModel.posts.value != null) {
                if (tag != null) {
                    postRecyclerAdapter.updateData(viewModel.posts.value!!.filter { it.tags[0] == tag })
                } else {
                    postRecyclerAdapter.updateData(viewModel.posts.value!!)
                }
            }
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

    private val postMenuListener = View.OnClickListener {

    }

    companion object {
        const val DTAG = "COMMUNITY_FRAGMENT"
    }
}