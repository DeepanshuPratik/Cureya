package com.example.cureya.home

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cureya.R
import com.example.cureya.databinding.FragmentHomeBinding
import com.example.cureya.home.data.blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(), blogitemClicked {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var blogRecyclerView: RecyclerView
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        database =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference

        binding.homeContextualMenu.setOnClickListener { showMenuPopUp(it) }
        binding.webLink.setOnClickListener {
            openweblink()
        }
        binding.fbLink.setOnClickListener {
            openfblink()
        }
        binding.linkedInLink.setOnClickListener {
            openlinkedinlink()
        }
        binding.ytLink.setOnClickListener {
            openytlink()
        }
        binding.instaLink.setOnClickListener {
            openinstalink()
        }
        binding.twitterLink.setOnClickListener {
            opentwitterlink()
        }
        getusername()
        initMembers(view)
        val images = listOf<blog>(
            blog(
                "Mental Health Disorders and How to Overcome",
                R.drawable.frame_546,
                "https://cureya.blogspot.com/2021/10/mental-health-disorders-how-to-overcome.html"
            ),
            blog(
                "What is Depression, Symptoms, Know all",
                R.drawable.frame_547,
                "https://cureya.blogspot.com/2022/01/what-is-depression-symptoms-know-all.html"
            ),
            blog(
                "Foods that Relieve Anxiety",
                R.drawable.frame_548,
                "https://cureya.blogspot.com/2022/01/foods-that-relieve-anxiety.html"
            ),
            blog(
                "Music & Our Mind",
                R.drawable.frame_549,
                "https://cureya.blogspot.com/2022/01/music-and-our-mind.html"
            ),
            blog(
                "Good Food Good Mood",
                R.drawable.frame_550,
                "https://cureya.blogspot.com/2022/01/good-food-good-mood.html"
            )
        )
        blogRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        blogRecyclerView.setHasFixedSize(true)
        blogRecyclerView.adapter = blogAdapter(this, images)
    }

    private fun getusername() {
        var username = ""
        val user = auth.currentUser
        val uid = user?.uid.toString()
        database.child("users").child(uid).get().addOnSuccessListener {
            username = it.child("name").value.toString().split(" ")[0]
            val photoUrl = it.child("photoUrl").value.toString()
            binding.userinfo.text = username
            binding.profile.load(photoUrl)
        }
    }

    private fun opentwitterlink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(
            requireContext(),
            Uri.parse("https://twitter.com/CureyaR?t=9l3a2-Qx3EkMLD-4JYnFYw&s=09")
        )
    }

    private fun openinstalink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(
            requireContext(),
            Uri.parse("https://www.instagram.com/cureya.in/")
        )
    }

    private fun openytlink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(
            requireContext(),
            Uri.parse("https://youtube.com/channel/UCjsRwGm--mr1ADln5CB5Siw")
        )
    }

    private fun openlinkedinlink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(
            requireContext(),
            Uri.parse("https://www.linkedin.com/company/cureya")
        )
    }

    private fun openfblink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse("https://m.facebook.com/cureya7"))
    }

    private fun openweblink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse("https://www.cureya.in/"))
    }

    private fun initMembers(view: View) {
        blogRecyclerView = view.findViewById(R.id.blogs_recycler_view)
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
                    R.id.profile -> {
                        val direction =
                            HomeFragmentDirections.actionHomeFragmentToPersonalProfile(auth.uid!!)
                        findNavController().navigate(direction)
                        true
                    }
                    R.id.moods -> false
                    R.id.contact_us -> {
                        findNavController().navigate(R.id.action_homeFragment_to_contactUsFragment)
                        true
                    }
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