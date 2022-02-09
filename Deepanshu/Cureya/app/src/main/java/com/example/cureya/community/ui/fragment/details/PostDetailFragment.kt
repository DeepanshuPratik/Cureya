package com.example.cureya.community.ui.fragment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.cureya.R
import com.example.cureya.chat.utils.toDateString
import com.example.cureya.databinding.PostDetailFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class PostDetailFragment : Fragment() {

    private lateinit var viewModel: PostDetailViewModel
    private lateinit var commentRecyclerAdapter: CommentRecyclerAdapter

    private var _binding: PostDetailFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PostDetailFragmentBinding.inflate(inflater, container, false)
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
        commentRecyclerAdapter = CommentRecyclerAdapter()
        viewModel = ViewModelProvider(this)[PostDetailViewModel::class.java]
        val post = PostDetailFragmentArgs.fromBundle(requireArguments()).post
        viewModel.initData(post)
    }

    private fun setClickListeners() {
        binding.sendCommentButton.setOnClickListener {
            val comment = binding.commentInput.text.toString()
            if (comment.isBlank()) {
                Toast.makeText(requireContext(), "Please provide some text!", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.postComment(comment)
                binding.commentInput.text.clear()
            }
        }
        binding.postCard.postLike.setOnClickListener {
            val isLiked = viewModel.post.value!!.likes.contains(auth.uid!!)
            if(isLiked) {
                viewModel.unlikePost()
            } else {
                viewModel.likePost()
            }
        }
    }

    private fun initUi() {
        binding.userImage.load(auth.currentUser!!.photoUrl)
        binding.postCard.postCardMenuButton.visibility = GONE
        binding.commentsRecycler.apply {
            adapter = commentRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        viewModel.comment.observe(viewLifecycleOwner) {
            this.commentRecyclerAdapter.updateData(it)
        }
        viewModel.post.observe(viewLifecycleOwner) { post ->
            val isLiked = post.likes.contains(auth.uid!!)
            binding.postCard.apply {
                postUserImage.load(post.profilePhoto)
                postCardUsername.text = post.userName
                postCardPhoto.load(post.photoUrl)
                postCardCaption.text = post.caption
                postCardUserprofession.text = post.tags[0].name
                postCardTime.text = post.createdAt.toDateString()
                if (isLiked) postLike.setImageResource(R.drawable.id_like_red) else postLike.setImageResource(
                    R.drawable.asset_like
                )
                postCardLikeCount.text = post.likes.size.toString()
                postCardCommentCount.text = post.commentCount.toString()
            }
        }
    }

}