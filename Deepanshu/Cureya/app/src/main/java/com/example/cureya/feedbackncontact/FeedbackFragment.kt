package com.example.cureya.feedbackncontact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentFeedbackBinding
import com.example.cureya.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import com.example.cureya.R

import android.widget.TextView

import android.app.Activity
import android.app.Dialog
import android.view.Window
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController


@DelicateCoroutinesApi
class FeedbackFragment : Fragment() {
    private lateinit var binding: FragmentFeedbackBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var database: DatabaseReference
    private var rating: Int = 5

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        navController = findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val color = resources.getColor(R.color.background)
        binding.great.setOnClickListener {
            rating=5
            binding.great.setBackgroundColor(resources.getColor(R.color.green_400))
            binding.bad.setBackgroundColor(color)
            binding.good.setBackgroundColor(color)
            binding.terrible.setBackgroundColor(color)
            binding.ok.setBackgroundColor(color)
        }
        binding.bad.setOnClickListener {
            rating = 2
            binding.great.setBackgroundColor(color)
            binding.bad.setBackgroundColor(resources.getColor(R.color.green_400))
            binding.good.setBackgroundColor(color)
            binding.terrible.setBackgroundColor(color)
            binding.ok.setBackgroundColor(color)
        }
        binding.good.setOnClickListener {
            rating = 4
            binding.great.setBackgroundColor(color)
            binding.bad.setBackgroundColor(color)
            binding.good.setBackgroundColor(resources.getColor(R.color.green_400))
            binding.terrible.setBackgroundColor(color)
            binding.ok.setBackgroundColor(color)

        }
        binding.terrible.setOnClickListener {
            rating = 1
            binding.great.setBackgroundColor(color)
            binding.bad.setBackgroundColor(color)
            binding.good.setBackgroundColor(color)
            binding.terrible.setBackgroundColor(resources.getColor(R.color.green_400))
            binding.ok.setBackgroundColor(color)
        }
        binding.ok.setOnClickListener {
            rating = 3
            binding.great.setBackgroundColor(color)
            binding.bad.setBackgroundColor(color)
            binding.good.setBackgroundColor(color)
            binding.terrible.setBackgroundColor(color)
            binding.ok.setBackgroundColor(resources.getColor(R.color.green_400))
        }
        binding.dashboardBackButton.setOnClickListener{
            navController.navigate(R.id.action_feedbackFragment_to_homeFragment)
        }
        auth = Firebase.auth
         database =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        GlobalScope.launch {
            val email =
                database.child("users").child(auth.uid!!).child("email").get().await().getValue(String::class.java)
            withContext(Dispatchers.Main) {
                binding.sendMessage.setOnClickListener{
                    showDialog(activity,"Your sumbission has been recieved!")
                    val p : String = binding.feedbackData.text.toString()
                    val user_feed = Feedback(email,p,rating)
                    if (email != null) {
                        database.child("Feedback").child(auth.uid.toString()).setValue(user_feed)
                    }
                    else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun showDialog(activity: Activity?, msg: String?) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(com.example.cureya.R.layout.alertcard)
        val text : TextView = dialog.findViewById(com.example.cureya.R.id.subtext_dialog)
        text.text = msg
        val dialogButton: Button = dialog.findViewById(com.example.cureya.R.id.btn)
        dialogButton.setOnClickListener(View.OnClickListener { dialog.dismiss() })
        dialog.show()
    }
}