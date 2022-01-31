package com.example.cureya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.databinding.FragmentSignUpBinding
import com.example.cureya.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class SignUpFragment: Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener { prepareToRegister() }

        binding.googleSignIn.setOnClickListener { handleGoogleSignIn() }

        binding.logInTextView.setOnClickListener { goToLogInFragment() }
    }

    private fun prepareToRegister() {
        if (areTextFieldsValid(binding.nameEditText, binding.edtLogInEmail, binding.edtLogInPassword)) {
            register()
        }
    }

    private fun register() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.edtLogInEmail.text.toString().trim()
        val password = binding.edtLogInPassword.text.toString().trim()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w("SignUpFragment","Firebase auth successful")
                    val user = User(name, email, null)
                    addToUserBase(user)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    private fun areTextFieldsValid(
        nameField: EditText,
        emailFiled: EditText,
        passwordField: EditText
    ) : Boolean {
        val nameFieldCheck = isTextFieldEmpty(nameField.text.toString())

        val emailFieldCheck = isTextFieldEmpty(emailFiled.text.toString()) &&
                isEmailValid(emailFiled.text.toString())

        val passwordFieldCheck = isTextFieldEmpty(passwordField.text.toString()) &&
                isPasswordLong(passwordField.text.toString())

        if (!nameFieldCheck) {
            nameField.error = "Please enter your name"
            nameField.requestFocus()
        }
        if (!emailFieldCheck) {
            emailFiled.error = "Please enter a valid email address"
            emailFiled.requestFocus()
        }
        if (!passwordFieldCheck) {
            passwordField.error = "Password should be at least 8 characters long"
            passwordField.requestFocus()
        }
        return nameFieldCheck && emailFieldCheck && passwordFieldCheck
    }

    private fun isTextFieldEmpty(text: String): Boolean {
        return text.isNotEmpty() && text.isNotBlank()
    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isPasswordLong(str: String) = str.length > 7

    private fun addToUserBase(user: User) {
        db = Firebase.database
        val newChildKey = auth.currentUser?.uid!!

        db.reference.child(USER_LIST).child(newChildKey)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        db.reference.child(USER_LIST).child(newChildKey).setValue(user)
                        Log.w(TAG, "New user inserted to database")
                    } else Log.w(TAG, "User already exists")
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Inside addToUserList()", error.toException())
                }
            })
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity() ,gso)
        signIn()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        val bottomView = (activity as AppCompatActivity)
            .findViewById<BottomNavigationView>(R.id.nav_view)
        bottomView.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        val bottomView = (activity as AppCompatActivity)
            .findViewById<BottomNavigationView>(R.id.nav_view)
        bottomView.visibility = View.VISIBLE
    }


    private fun updateUI() {
        val currentUser = auth.currentUser
        if(currentUser != null) {
            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        } else {
            showToast("Please register to continue")
        }
    }

    private fun goToLogInFragment() = findNavController().navigate(R.id.action_signUpFragment_to_logInFragment)

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = User(
                        auth.currentUser?.displayName,
                        auth.currentUser?.email,
                        auth.currentUser?.photoUrl.toString()
                    )
                    addToUserBase(user)
                    updateUI()
                } else {
                    showToast("Unexpected error occurred")
                }
            }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
        return
    }

    companion object {
        const val RC_SIGN_IN = 100
        const val USER_LIST = "users"
        const val TAG = "GOOGLE_SIGN_IN_TAG"
        private const val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
    }
}
