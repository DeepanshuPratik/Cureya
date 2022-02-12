package com.example.cureya.home

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cureya.R
import com.example.cureya.databinding.FragmentHomeBinding
import com.example.cureya.databinding.FragmentMoodBinding
import com.example.cureya.feedbackncontact.Feedback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class mood : Fragment() {
    private lateinit var binding: FragmentMoodBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoodBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        database =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        GlobalScope.launch {
            val email =
                database.child("users").child(auth.uid!!).child("email").get().await()
                    .getValue(String::class.java)
            val name = database.child("users").child(auth.uid!!).child("name").get().await()
                .getValue(String::class.java)
            withContext(Dispatchers.Main) {
                binding.welcomeMsg.text = "Hi $name, what's your mood today"
                binding.angry.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is angry")
                    val user_feed = Mood(name, email, "angry")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.anxious.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is anxious")
                    val user_feed = Mood(name, email, "anxious")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.confounded.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is confounded")
                    val user_feed = Mood(name, email, "confounded")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.confused.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is confused")
                    val user_feed = Mood(name, email, "confused")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.crying.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is crying")
                    val user_feed = Mood(name, email, "crying")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.disappointed.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is disappointed")
                    val user_feed = Mood(name, email, "disappointed")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.downcast.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is downcast")
                    val user_feed = Mood(name, email, "downcast")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.excellent.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is excellent")
                    val user_feed = Mood(name, email, "excellent")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.expressionless.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is expressionless")
                    val user_feed = Mood(name, email, "expressionless")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.frowning.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is frowning")
                    val user_feed = Mood(name, email, "frowning")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.good.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is good")
                    val user_feed = Mood(name, email, "good")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
                binding.happy.setOnClickListener {
                    showDialog(activity, "Your submission has been received! Got it your Mood is happy")
                    val user_feed = Mood(name, email, "happy")
                    if (email != null) {
                        database.child("Mood").child(auth.uid.toString()).setValue(user_feed)
                    } else {
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