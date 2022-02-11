package com.example.cureya.relaxation.ui

import android.annotation.SuppressLint
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.cureya.R
import com.example.cureya.databinding.CardMusicBinding
import com.example.cureya.databinding.FragmentRelaxationMusicBinding
import com.example.cureya.model.Content
import com.example.cureya.relaxation.ui.MusicVideoFragment.Companion.MUSIC_LIST
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.IllegalStateException
import java.time.Duration
import java.util.*
import kotlin.math.abs

class MusicFragment : Fragment() {

    private lateinit var adapter: FirebaseRecyclerAdapter<Content, CardMusicViewHolder>
    private lateinit var binding: FragmentRelaxationMusicBinding
    private lateinit var db: FirebaseDatabase

    private lateinit var mediaPlayer: MediaPlayer
    private val contentList = mutableListOf<Content>()
    private var position = 0

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
        position = navArgument.itemPosition

        createContentList()

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
        binding.musicListViewPager.currentItem = position

        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85F + r * 0.14F
            }
        }
        binding.musicListViewPager.setPageTransformer(transformer)

        binding.musicPlay.setOnClickListener { handleMusicState() }

        binding.musicNext.setOnClickListener { playNextMusic() }

        binding.musicPrevious.setOnClickListener { playPreviousMusic() }

        // sliding bar slide action
        binding.musicListViewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(pos: Int) {
                super.onPageSelected(pos)
                playSlideMusic(pos)
            }
        })

        // seekbar music control
        binding.musicSeekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (p2) { mediaPlayer.seekTo(p1) }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    private fun createContentList() {
        db.reference.child(MUSIC_LIST).get()
            .addOnSuccessListener { snapshot ->
                snapshot.children.forEach {
                    val item = it.getValue(Content::class.java)!!
                    contentList.add(item)
                }
                binding.progressBar.visibility = View.GONE
                val url = contentList[position].contentUrl!!
                playMusicWithUrl(url)
            }
    }

    private fun playMusicWithUrl(url: String) {
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
        setSeekBar()
        updateTimer()
    }

    private fun setSeekBar() {
        binding.musicTimeTotal.text = formatTime(mediaPlayer.duration.toLong())

        binding.musicSeekbar.max = mediaPlayer.duration

        Timer().scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                try {
                    binding.musicSeekbar.progress = mediaPlayer.currentPosition
                } catch (e: IllegalStateException) {}
            }
        }, 0, 900)
    }

    @SuppressLint("NewApi")
    private fun updateTimer() {
        Timer().scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                try {
                    val currentTime = formatTime(mediaPlayer.currentPosition.toLong())
                    binding.musicTimeCount.text = currentTime
                } catch (e: Exception) {}
            }
        }, 0, 1000)
    }

    private fun playSlideMusic(pos: Int) {
        try {
            mediaPlayer.stop()
            mediaPlayer.release()

            val url = contentList[pos].contentUrl!!
            setMusicTitle(contentList[pos].title!!)
            playMusicWithUrl(url)
        } catch (e: Exception) {}
    }

    private fun playNextMusic() {
        mediaPlayer.stop()
        mediaPlayer.release()

        // to loop back once we are at the edge of the list
        position = (position + 1) % contentList.size
        binding.musicListViewPager.currentItem = position

        val url = contentList[position].contentUrl!!
        setMusicTitle(contentList[position].title!!)
        playMusicWithUrl(url)
    }

    private fun playPreviousMusic() {
        mediaPlayer.stop()
        mediaPlayer.release()

        // to loop through the list
        if (position - 1 < 0) {
            position = contentList.size - 1
        } else { position.dec() }
        binding.musicListViewPager.currentItem = position

        val url = contentList[position].contentUrl!!
        setMusicTitle(contentList[position].title!!)
        playMusicWithUrl(url)
    }

    private fun handleMusicState() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            binding.musicPlay.setImageResource(R.drawable.ic_play)
        } else {
            mediaPlayer.start()
            binding.musicPlay.setImageResource(R.drawable.asset_relaxation_music_playing)
        }
    }

    @SuppressLint("NewApi")
    private fun formatTime(mls: Long): String {
        var time = ""
        val minutes = (mls/1000) / 60
        val seconds = (mls/1000) % 60
        time = "$time$minutes:"
        if (seconds < 10) { time+= '0' }
        return "$time$seconds"
    }

    private fun setMusicTitle(title: String) { binding.musicTitle.text = title }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.release()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    inner class CardMusicViewHolder(private val binding: CardMusicBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Content) {
            binding.musicThumbnail.load(item.thumbnailUrl)

            // title is not part of the view holder as to configure a sliding list
            setMusicTitle(item.title!!)
        }
    }
}