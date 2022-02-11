package com.example.cureya.relaxation.ui

import android.os.Bundle
import android.util.Log
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
import com.example.cureya.relaxation.viewholder.MusicViewHolder
import com.example.cureya.relaxation.viewholder.VideoViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MusicVideoFragment : Fragment() {

    private lateinit var videoAdapter: FirebaseRecyclerAdapter<Content, VideoViewHolder>
    private lateinit var musicAdapter: FirebaseRecyclerAdapter<Content, MusicViewHolder>
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
        val musicRef = db.reference.child(MUSIC_LIST)

        val musicList = FirebaseRecyclerOptions.Builder<Content>()
            .setQuery(musicRef, Content::class.java)
            .build()

        binding.label.text = getString(R.string.music)
        binding.heading.text = getString(R.string.we_recommend_you_favourite_music)

        musicAdapter = object : FirebaseRecyclerAdapter<Content, MusicViewHolder>(musicList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
                val layoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_music_and_video, parent, false)
                return MusicViewHolder(
                    CardMusicAndVideoBinding.bind(layoutView),
                    this@MusicVideoFragment,
                    binding.progressBar
                )
            }
            override fun onBindViewHolder(holder: MusicViewHolder, position: Int, model: Content) {
                holder.bind(model, position)
            }
        }
        binding.contentRecyclerView.adapter = musicAdapter
        binding.contentRecyclerView.itemAnimator = null
    }

    override fun onStart() {
        super.onStart()
        try {
            videoAdapter.startListening()
        } catch (e: UninitializedPropertyAccessException) {
            Log.w(TAG, "Music adapter called")
        }
        try {
            musicAdapter.startListening()
        } catch (e: UninitializedPropertyAccessException) {
            Log.w(TAG, "Video adapter called")
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            videoAdapter.stopListening()
        } catch (e: UninitializedPropertyAccessException) { }
        try {
            musicAdapter.stopListening()
        } catch (e: UninitializedPropertyAccessException) { }
    }

    fun goToVideoFragment(videoUrl: String) = findNavController().navigate(
        MusicVideoFragmentDirections.actionMusicVideoFragmentToVideoFragment(
            videoUrl
        )
    )

    fun goToMusicFragment(position: Int) = findNavController().navigate(
        MusicVideoFragmentDirections.actionMusicVideoFragmentToMusicFragment(position)
    )

    companion object {
        private const val TAG = "MusicVideoFragment"
        const val VIDEO_LIST = "videos"
        const val MUSIC_LIST = "music"
    }
}