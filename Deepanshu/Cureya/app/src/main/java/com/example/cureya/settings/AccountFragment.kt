package com.example.cureya.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cureya.R
import com.example.cureya.databinding.FragmentSettingsAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentSettingsAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        binding.apply {
            changePasswordFrame.setOnClickListener {
                this@AccountFragment.findNavController().navigate(AccountFragmentDirections
                    .actionAccountFragmentToChangeDetailsFragment(CHANGE_PASSWORD_CODE)
                )
            }
            changeEmailFrame.setOnClickListener {
                this@AccountFragment.findNavController().navigate(AccountFragmentDirections
                    .actionAccountFragmentToChangeDetailsFragment(CHANGE_EMAIL_CODE)
                )
            }
            deleteAccountFrame.setOnClickListener {
                auth.currentUser!!.delete()
                goToHomeFragment()
            }
            signOutFrame.setOnClickListener {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setMessage(R.string.sign_out_dialog_text)
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

    private fun goToHomeFragment() = findNavController().navigate(R.id.action_accountFragment_to_homeFragment)

    companion object {
        const val CHANGE_EMAIL_CODE = 1
        const val CHANGE_PASSWORD_CODE = 2
    }
}