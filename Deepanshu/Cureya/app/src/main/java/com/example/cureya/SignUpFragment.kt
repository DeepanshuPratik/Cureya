package com.example.cureya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.Credentials.Credentials.Companion.CLIENT_ID
import com.example.cureya.databinding.FragmentSignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpFragment: Fragment() {

    private val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"

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

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity() ,gso)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener { register() }

        binding.SignIn.setOnClickListener { signIn() }

        binding.logIn.setOnClickListener { goToLogInFragment() }
    }

    private fun register() {
        binding.btnRegister.setOnClickListener {
            if ( binding.edtLogInEmail.text.toString() == "" ||
                 binding.nameEditText.text.toString() == "" ||
                 binding.edtLogInPassword.text.toString() == ""
            ) {
                Toast.makeText(context, "Please fill required text fields", Toast.LENGTH_SHORT).show()
            } else if (binding.edtLogInPassword.text!!.length < 8) {
                Toast.makeText(
                    context,
                    "Password should be at least 8 characters long",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                /* In terms of fragments, navigation is yet to be implemented
                val ActivityIntent = Intent(this, SplashScreenFragment::class.java)
                startActivity(ActivityIntent)
                finish() */
            }
        }
    }

    fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
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
        if(currentUser!=null){
            Toast.makeText(context,"Sign clicked", Toast.LENGTH_SHORT).show()
            /* Navigation is yet to be implemented
            val profileActivityIntent= Intent(this, SplashScreenFragment::class.java)
            startActivity(profileActivityIntent)
            finish() */
        } else{
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
    }
}