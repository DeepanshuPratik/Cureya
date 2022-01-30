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
import com.example.cureya.Credentials.Credentials.Companion.CLIENT_ID
import com.example.cureya.databinding.FragmentSignUpBinding
import com.example.cureya.model.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpFragment: Fragment() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
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

        binding.SignIn.setOnClickListener { handleGoogleSignIn() }

        binding.logInTextView.setOnClickListener { goToLogInFragment() }
    }

    private fun prepareToRegister() {
        if (areTextFieldsValid(binding.nameEditText, binding.edtLogInEmail, binding.edtLogInPassword)) {
            register()
        }
    }

    private fun register() {
        val name = binding.nameEditText.text.toString()
        val email = binding.edtLogInEmail.text.toString()
        val password = binding.edtLogInPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.w("SignUpFragment","Firebase auth successful")
                    val user = User(name, email)

                    FirebaseDatabase.getInstance().getReference(USER_PATH)
                        .child(auth.currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { task ->
                            Log.w("SignUpFragment","got reference to database")
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Sign In successful", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Sign in failed", Toast.LENGTH_LONG).show()
                            }
                        }
                }
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

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun handleGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
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
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.getCurrentUser()
        updateUI(currentUser);
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

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null) {
            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
        } else {
            Toast.makeText(context,"not possible", Toast.LENGTH_SHORT).show()
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
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    companion object {
        val RC_SIGN_IN = 100
        val TAG = "GOOGLE_SIGN_IN_TAG"
        private val USER_PATH = "Users"
        private val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"
    }
}