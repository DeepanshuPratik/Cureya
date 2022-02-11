package com.example.cureya.feedbackncontact

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentContactusBinding
import com.example.cureya.databinding.FragmentFeedbackBinding
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

class ContactFragment : Fragment() {
    private lateinit var binding: FragmentContactusBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.instagram.setOnClickListener {
            openinstalink()
        }
        binding.mailId.setOnClickListener {
            composeEmail("info@cureya.in","Have a query. Revert me back")
        }
        binding.facebook.setOnClickListener {
            openfblink()
        }
        binding.twitter.setOnClickListener {
            opentwitterlink()
        }
        binding.linkedIn.setOnClickListener {
            openlinkedinlink()
        }
        binding.youtube.setOnClickListener {
            openytlink()
        }
        auth = Firebase.auth
        database =
            FirebaseDatabase.getInstance("https://cureyadraft-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        GlobalScope.launch {
            val email =
                database.child("users").child(auth.uid!!).child("email").get().await().getValue(String::class.java)
            withContext(Dispatchers.Main) {
                binding.sendMessage.setOnClickListener{
                    val message : String = binding.message.text.toString()
                    val subject : String = binding.subject.text.toString()
                    val name : String = binding.name.text.toString()
                    val user_details = Contact(name,email,subject,message)
                    if (email != null) {
                        database.child("Contact Details").child(auth.uid.toString()).setValue(user_details)
                    }
                    else {
                        Toast.makeText(activity, "email not mentioned!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

    @SuppressLint("QueryPermissionsNeeded")
    private fun composeEmail(recipient: String, subject: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(activity, "Error Occurred", Toast.LENGTH_LONG).show()
        }
    }

    private fun openweblink() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), Uri.parse("https://www.cureya.in/"))
    }
}
