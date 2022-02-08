package com.example.cureya.community.ui.fragment.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import coil.load
import com.example.cureya.R
import com.example.cureya.chat.utils.toDateString
import com.example.cureya.community.models.Post
import com.example.cureya.databinding.PostDetailFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class PostDetailFragment : Fragment() {

    private lateinit var post: Post

    private lateinit var viewModel: PostDetailViewModel

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
        initData()
        initUi()
    }

    private fun initData() {
        post = PostDetailFragmentArgs.fromBundle(requireArguments()).post

    }

    private fun initUi() {
        val isLiked = post.likes.contains(auth.uid!!)
        binding.postCard.apply {
            postUserImage.load(post.profilePhoto)
            postCardUsername.text = post.userName
            postCardPhoto.load(post.photoUrl)
            postCardCaption.text = post.caption
            postCardUserprofession.text = post.tags[0].name
            postCardTime.text = post.createdAt.toDateString()
            if (isLiked) postLike.setImageResource(R.drawable.id_like_red) else postLike.setImageResource(R.drawable.asset_like)
            postCardLikeCount.text = post.likes.size.toString()
            postCardCommentCount.text = post.comments.size.toString()


        }
    }

}