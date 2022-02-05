package com.example.cureya.settings

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cureya.R
import com.example.cureya.databinding.FragmentSettingsChangeDetailsBinding
import com.example.cureya.register.SignUpFragment.Companion.EMAIL
import com.example.cureya.register.SignUpFragment.Companion.PASSWORD
import com.example.cureya.register.SignUpFragment.Companion.USER_EXISTS_ERROR
import com.example.cureya.register.SignUpFragment.Companion.USER_LIST
import com.example.cureya.settings.AccountFragment.Companion.CHANGE_EMAIL_CODE
import com.example.cureya.settings.AccountFragment.Companion.CHANGE_PASSWORD_CODE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChangeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsChangeDetailsBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase

    private val navArgument: ChangeDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsChangeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = Firebase.database

        val changeDataType = navArgument.changeDataType

        if (changeDataType == CHANGE_EMAIL_CODE) {
            // set email changing layout
            binding.apply {
                labelChangeDetails.text = getString(R.string.change_email_address)
                frameOneEditText.hint = getString(R.string.active_mail_id)
                frameTwoEditText.hint = getString(R.string.enter_new_email_address)
                frameThreeEditText.hint = getString(R.string.confirm_new_email_address)
                frameThree.visibility = View.VISIBLE
            }
        } else if (changeDataType == CHANGE_PASSWORD_CODE) {
            // set password changing layout
            binding.apply {
                labelChangeDetails.text = getString(R.string.change_password)
                frameOneEditText.hint = getString(R.string.current_password)
                frameTwoEditText.hint = getString(R.string.new_password)
                frameThree.visibility = View.GONE
            }
        }

        binding.saveButton.setOnClickListener {
            when (changeDataType) {
                CHANGE_EMAIL_CODE -> prepareToChangeEmail()
                CHANGE_PASSWORD_CODE -> prepareToChangePassword()
                else -> Toast.makeText(context,
                    "Unexpected error occurred",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun prepareToChangeEmail() {
        val currentEmail = binding.frameOneEditText.text.toString()
        val newEmail = binding.frameTwoEditText.text.toString()
        val confirmText = binding.frameThreeEditText.text.toString()

        db.reference.child(USER_LIST).orderByChild(EMAIL).equalTo(currentEmail)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        showToast("User does not exist")
                        return
                    }
                    if (!validateEmail(newEmail, confirmText)) {
                        binding.frameThreeEditText.apply {
                            error = "Passwords don't match"
                            requestFocus()
                        }
                    } else {
                        changeEmail(newEmail)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "error in finding email in database", error.toException())
                }
            })
    }

    private fun prepareToChangePassword() {
        val currentPassword = binding.frameOneEditText.text.toString().trim()
        val newPassword = binding.frameTwoEditText.text.toString().trim()

        db.reference.child(USER_LIST).orderByChild(PASSWORD).equalTo(currentPassword)
            .addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value == null) {
                        binding.frameOneEditText.apply {
                            error = "Current password does not match"
                            requestFocus()
                            return
                        }
                    }
                    if (newPassword.length < 8) {
                        showToast("Password must be 8 character long")
                    } else {
                        changePassword(newPassword)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG,"unexpected error occurred in changing password")
                }
            })
    }

    private fun changeEmail(email: String) {
        val user = auth.currentUser!!
        user.updateEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    changeEmailInDatabase(email)
                }
            }
            .addOnFailureListener {
                when (it.message) {
                    CASE_SENSITIVE_SIGN_OUT_ERROR -> showDialog()
                    USER_EXISTS_ERROR -> showToast("New user email already exists")
                    else -> Log.e("TAG", "error in updating email", it)
                }
            }
    }

    private fun changePassword(password: String) {
        val user = auth.currentUser!!
        user.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    changePasswordInDatabase(password)
                }
            }
            .addOnFailureListener {
                if (it.message == CASE_SENSITIVE_SIGN_OUT_ERROR) {
                    showDialog()
                } else {
                    Log.e(TAG, "error updating password", it)
                }
            }
    }

    private fun changeEmailInDatabase(email: String) {
        val userId = auth.currentUser!!.uid
        db.reference.child(USER_LIST).child(userId).child(EMAIL).setValue(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Email changed successfully")
                    findNavController().navigateUp()
                } else {
                    showToast("Error in updating email in database")
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "error in updating error in database", it)
            }
    }

    private fun changePasswordInDatabase(password: String) {
        val userId = auth.currentUser!!.uid
        db.reference.child(USER_LIST).child(userId).child(PASSWORD).setValue(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    showToast("Password changed successfully")
                    findNavController().navigateUp()
                } else {
                    showToast("Error in updating password in database")
                }
            }
            .addOnFailureListener {
                Log.e(TAG, "error in updating password in database", it)
            }
    }

    private fun validateEmail(newEmail: String, confirmText: String) : Boolean {
        return newEmail == confirmText &&
                newEmail.isNotEmpty() &&
                confirmText.isNotEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()
    }

    private fun goToHomeFragment() = findNavController().navigate(R.id.action_changeDetailsFragment_to_homeFragment)

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Action Required")
            .setMessage(R.string.dialog_sign_out_warning_text)
            .setPositiveButton(R.string.ok) { _, _ ->
                auth.signOut()
                goToHomeFragment()
            }
            .setNegativeButton(R.string.no) { _, _ -> }
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
            setTextColor(getColor(requireContext(), R.color.white))
            setBackgroundColor(getColor(requireContext(), R.color.colorPr))
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
        private const val TAG = "ChangeDetailsFragment"
        private const val CASE_SENSITIVE_SIGN_OUT_ERROR =
            "This operation is sensitive and requires recent authentication. Log in again before retrying this request."
    }
}