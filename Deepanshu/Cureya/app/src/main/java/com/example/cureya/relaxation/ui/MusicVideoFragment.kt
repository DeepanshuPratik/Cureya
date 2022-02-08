package com.example.cureya.relaxation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cureya.R
import com.example.cureya.databinding.CardMusicAndVideoBinding
import com.example.cureya.databinding.FragmentRelaxationMusicVideoBinding
import com.example.cureya.model.Content
import com.example.cureya.relaxation.ui.RelaxationFragment.Companion.CONTENT_TYPE_MUSIC
import com.example.cureya.relaxation.ui.RelaxationFragment.Companion.CONTENT_TYPE_VIDEO
import com.example.cureya.relaxation.viewholder.VideoViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MusicVideoFragment : Fragment() {

    private lateinit var videoAdapter: FirebaseRecyclerAdapter<Content, VideoViewHolder>
    private lateinit var binding: FragmentRelaxationMusicVideoBinding
    private lateinit var db: FirebaseDatabase

    private val navArgument: MusicVideoFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationMusicVideoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentType = navArgument.contentType

        db = Firebase.database

        if (contentType == CONTENT_TYPE_VIDEO) {
            showVideoList()
        } else if (contentType == CONTENT_TYPE_MUSIC) {
            showMusicList()
        }
    }

    private fun showVideoList() {
        val videoRef = db.reference.child(VIDEO_LIST)

        val videoList = FirebaseRecyclerOptions.Builder<Content>()
            .setQuery(videoRef, Content::class.java)
            .build()

        binding.label.text = getString(R.string.video)
        binding.heading.text = getString(R.string.we_recommend_you_favorite_video)

        videoAdapter = object : FirebaseRecyclerAdapter<Content, VideoViewHolder>(videoList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
                val layoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_music_and_video, parent, false)
                return VideoViewHolder(
                    CardMusicAndVideoBinding.bind(layoutView),
                    this@MusicVideoFragment,
                    binding.progressBar
                )
            }
            override fun onBindViewHolder(holder: VideoViewHolder, position: Int, model: Content) {
                holder.bind(model)
            }
        }
        binding.contentRecyclerView.adapter = videoAdapter
        binding.contentRecyclerView.itemAnimator = null
    }

    private fun showMusicList() {

    }

    override fun onStart() {
        super.onStart()
        videoAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        videoAdapter?.stopListening()
    }

    fun goToVideoFragment(videoUrl: String) = findNavController().navigate(
        MusicVideoFragmentDirections.actionMusicVideoFragmentToVideoFragment(
            videoUrl
        )
    )

    companion object {
        const val VIDEO_LIST = "videos"
    }
}