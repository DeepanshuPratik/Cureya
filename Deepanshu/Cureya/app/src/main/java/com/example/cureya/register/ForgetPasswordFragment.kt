package com.example.cureya.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cureya.databinding.FragmentForgetPasswordBinding
import com.example.cureya.register.SignUpFragment.Companion.USER_LIST
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ForgetPasswordFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var binding: FragmentForgetPasswordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.continueButton.setOnClickListener {
            validateEmail()
        }
        // EmailAuthProvider.
    }

    private fun validateEmail() {
        val email = binding.resetEmail.text.toString().trim()

        db = Firebase.database
        db.reference.child(USER_LIST).orderByChild(EMAIL_CHILD).equalTo(email)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        sendOTP()
                    } else {
                        showToast("User does not exist")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "error in finding user", error.toException())
                }
            })
    }

    private fun sendOTP() {

    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        const val EMAIL_CHILD = "email"
        private const val TAG = "ForgetPasswordFragment"
    }
}