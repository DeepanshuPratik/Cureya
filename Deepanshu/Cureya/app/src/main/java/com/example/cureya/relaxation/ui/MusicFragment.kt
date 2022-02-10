package com.example.cureya.relaxation.ui

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import coil.load
import com.bumptech.glide.disklrucache.DiskLruCache
import com.example.cureya.R
import com.example.cureya.databinding.CardMusicBinding
import com.example.cureya.databinding.FragmentRelaxationMusicBinding
import com.example.cureya.model.Content
import com.example.cureya.relaxation.ui.MusicVideoFragment.Companion.MUSIC_LIST
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException
import java.util.*
import kotlin.math.abs

class MusicFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var binding: FragmentRelaxationMusicBinding
    private lateinit var mediaPlayer: MediaPlayer

    private val navArgument: MusicFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database

        val url = navArgument.contentUrl
        val title = navArgument.title
        val thumbnailUrl = navArgument.thumbnailUrl

        binding.musicImage.load(thumbnailUrl)
        binding.musicHeading.text = title

        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }

        binding.musicSeekbar.max = mediaPlayer.duration

        Timer().scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                try {
                    binding.musicSeekbar.progress = mediaPlayer.currentPosition
                } catch (e: IllegalStateException) {}
            }
        }, 0, 900)

        binding.musicSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) { mediaPlayer.seekTo(p1) }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.musicPlay.setOnClickListener { handleMediaPlayer() }
    }

    private fun handleMediaPlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.musicPlay.load(R.drawable.ic_play)
        } else {
            mediaPlayer.start()
            binding.musicPlay.load(R.drawable.asset_relaxation_music_playing)
        }
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }

    /* private lateinit var adapter: FirebaseRecyclerAdapter<Content, CardMusicViewHolder>
    private lateinit var binding: FragmentRelaxationMusicBinding
    private lateinit var db: FirebaseDatabase
    private var mediaPlayer: MediaPlayer? = null

    private lateinit var contentList: MutableList<Content>
    private var position = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRelaxationMusicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = Firebase.database

        val musicRef = db.reference.child(MUSIC_LIST)

        val musicList = FirebaseRecyclerOptions.Builder<Content>()
            .setQuery(musicRef, Content::class.java)
            .build()

        adapter = object: FirebaseRecyclerAdapter<Content, CardMusicViewHolder>(musicList) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardMusicViewHolder {
                val layoutView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.card_music, parent, false)
                return CardMusicViewHolder(
                    CardMusicBinding.bind(layoutView)
                )
            }
            override fun onBindViewHolder(holder: CardMusicViewHolder, position: Int, model: Content) {
                holder.bind(model)
            }
        }
        // creating a sliding list with 3 visible items
        binding.musicListViewPager.adapter = adapter
        binding.musicListViewPager.offscreenPageLimit = 3
        binding.musicListViewPager.clipChildren = false
        binding.musicListViewPager.clipToPadding = false

        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85F + r * 0.14F
            }
        }
        binding.musicListViewPager.setPageTransformer(transformer)

        binding.musicPlay.setOnClickListener { handleMusicState() }
    }

    private fun playMusicWithUrl(url: String) {
        mediaPlayer?.apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setDataSource(url)
            prepare() // might take long! (for buffering, etc)
            start()
        }
    }

    private fun handleMusicState() {
        if (mediaPlayer?.isPlaying!!) {
            mediaPlayer?.pause()
            binding.musicPlay.setImageResource(R.drawable.ic_play)
        } else {
            mediaPlayer?.start()
            binding.musicPlay.setImageResource(R.drawable.asset_relaxation_music_playing)
        }
    }

    private fun setMusicTitle(title: String) { binding.musicTitle.text = title }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    inner class CardMusicViewHolder(private val binding: CardMusicBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content) {
            binding.musicThumbnail.load(item.thumbnailUrl)

            playMusicWithUrl(item.contentUrl!!)
            // title is not part of the view holder as to configure a sliding list
            setMusicTitle(item.title!!)
        }
    } */
}