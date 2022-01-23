package com.example.cureya

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cureya.Credentials.Credentials.Companion.CLIENT_ID
import com.example.cureya.databinding.ActivitySignUpBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActivitySignUp: AppCompatActivity() {

    private val PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})"

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivitySignUpBinding.inflate(layoutInflater)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)

        auth = Firebase.auth

        binding.btnRegister.setOnClickListener { register() }

        binding.SignIn.setOnClickListener { signIn() }
    }


    private fun register(){
        binding.btnRegister.setOnClickListener {
            if ( binding.edtSignUpEmail.text.toString() == "" ||
                 binding.edtName.text.toString() == "" ||
                 binding.edtPassword.text.toString() == ""
            ) {
                Toast.makeText(this, "Signup not possible", Toast.LENGTH_SHORT).show()
            } else if (binding.edtPassword.text.length < 10) {
                Toast.makeText(
                    this,
                    "enter password in more than 10 letters",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val ActivityIntent = Intent(this, SplashScreenActivity::class.java)
                startActivity(ActivityIntent)
                finish()
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
        var currentUser = auth.getCurrentUser()
        updateUI(currentUser);
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser!=null){
            Toast.makeText(this,"Sign clicked", Toast.LENGTH_SHORT).show()
            val profileActivityIntent= Intent(this, SplashScreenActivity::class.java)
            startActivity(profileActivityIntent)
            finish()
        } else{
            Toast.makeText(this,"not possible", Toast.LENGTH_SHORT).show()
        }
    }


    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
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