package com.example.cureya.home

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.example.cureya.databinding.FragmentHomeBinding
import com.example.cureya.home.data.blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(),blogitemClicked {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var blogRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.homeContextualMenu.setOnClickListener { showMenuPopUp(it) }

        initMembers(view)
        val images= listOf<blog>(
            blog("Mental Health Disorders and How to Overcome them",R.drawable.frame_546, "https://cureya.blogspot.com/2021/10/mental-health-disorders-how-to-overcome.html"),
            blog("What is Depression, Symptoms, Know all",R.drawable.frame_547,"https://cureya.blogspot.com/2022/01/what-is-depression-symptoms-know-all.html"),
            blog("Foods that Relieve Anxiety",R.drawable.frame_548,"https://cureya.blogspot.com/2022/01/foods-that-relieve-anxiety.html"),
            blog("Music & Our Mind",R.drawable.frame_549,"https://cureya.blogspot.com/2022/01/music-and-our-mind.html"),
            blog("Good Food Good Mood", R.drawable.frame_550,"https://cureya.blogspot.com/2022/01/good-food-good-mood.html")
        )
        blogRecyclerView.layoutManager = LinearLayoutManager(this.context)
        blogRecyclerView.setHasFixedSize(true)
        blogRecyclerView.adapter = blogAdapter(this,images)
    }

    private fun initMembers(view: View) {
        blogRecyclerView=view.findViewById(R.id.blogs_recycler_view)

    }
    private fun showMenuPopUp(view: View) {
        PopupMenu(context, view).apply {
            setOnMenuItemClickListener { p0 ->
                when (p0?.itemId) {
                    R.id.settings -> {
                        findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                        true
                    }
                    R.id.about_us -> {
                        findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
                        true
                    }
                    R.id.log_out -> {
                        auth.signOut()
                        findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
                        true
                    }
                    R.id.profile -> false
                    R.id.moods -> false
                    R.id.contact_us -> false
                    R.id.report -> false
                    else -> false
                }
            }
            inflate(R.menu.home_contextual_menu)
            show()
        }

    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            findNavController().navigate(R.id.action_homeFragment_to_logInFragment)
        }
    }

    override fun onItemClicked(item: blog) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse(item.url))
    }
}