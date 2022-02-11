package com.example.cureya.profile

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import coil.load
import com.bumptech.glide.Glide
import com.example.cureya.R
import com.example.cureya.community.utils.toDateString
import com.example.cureya.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class PersonalProfile : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var navController: NavController
    private lateinit var getContent: ActivityResultLauncher<String>

    private var _binding: ProfileFragmentBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private var imageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProfileFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setClickListeners()
        observeData()
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        val profileId = PersonalProfileArgs.fromBundle(requireArguments()).userId
        if (profileId != auth.uid!!) {
            binding.apply {
                editAbout.visibility = View.GONE
                editEmail.visibility = View.GONE
                editProfilePhoto.visibility = View.GONE
                editGender.visibility = View.GONE
            }
        }
        getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                val contentResolver = requireActivity().contentResolver
                imageUri = null
                if (uri != null) {
                    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        cursor.moveToFirst()
                        val size = cursor.getLong(sizeIndex)
                        if (size < 500000) {
                            imageUri = uri
                            Log.d(TAG, "initMembers: $uri")
                            AlertDialog.Builder(requireContext()).apply {
                                setTitle("New profile Picture")
                                val imageView = ImageView(requireContext()).apply {
                                    layoutParams =
                                        LinearLayoutCompat.LayoutParams(
                                            resources.getDimensionPixelSize(R.dimen.dp_64),
                                            resources.getDimensionPixelSize(R.dimen.dp_64)
                                        ).apply {
                                            setMargins(resources.getDimensionPixelSize(R.dimen.dp_8))
                                        }

                                    scaleType = ImageView.ScaleType.CENTER_CROP

                                }
                                Glide.with(this@PersonalProfile).load(uri).into(imageView)
                                setView(imageView)
                                setNegativeButton("Cancel") { view, _ ->
                                    view.dismiss()
                                }
                                setPositiveButton("Set") { _, _ ->

                                }
                                show()
                            }
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "File size exceeded 500KB",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        navController = findNavController()
        viewModel.loadData(profileId)
    }

    private fun setClickListeners() {
        binding.apply {
            btnBack.setOnClickListener { navController.popBackStack() }
            editProfilePhoto.setOnClickListener {
                getContent.launch("image/*")
            }
            editAbout.setOnClickListener {
                AlertDialog.Builder(requireContext()).apply {
                    val e = layoutInflater.inflate(R.layout.edit_about_layout, null)
                    val editText = e.findViewById<EditText>(R.id.edit_email)
                    val builder = AlertDialog.Builder(requireContext()).apply {
                        setTitle("About")
                        setView(e)
                        setCancelable(false)
                        setNegativeButton("Cancel", null)
                        setPositiveButton("Set", null)
                    }
                    val dialog = builder.create()
                    dialog.show()
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val text = editText.text.toString()
                        if (text.isNotEmpty()) {
                            viewModel.editProfile(about = text)
                            dialog.dismiss()
                        } else {
                            editText.error = "Please enter a valid value!"
                        }
                    }
                }
            }
            editEmail.setOnClickListener {
                val e = layoutInflater.inflate(R.layout.layout_edit_email, null)
                val editText = e.findViewById<EditText>(R.id.edit_email)
                val builder = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Email")
                    setView(e)
                    setCancelable(false)
                    setNegativeButton("Cancel", null)
                    setPositiveButton("Set", null)
                }
                val dialog = builder.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    val text = editText.text.toString()
                    if (isEmailValid(text)) {
                        viewModel.editProfile(email = text)
                    } else {
                        editText.error = "Please enter a valid email!"
                    }
                }
            }
            editGender.setOnClickListener {
                val e = layoutInflater.inflate(R.layout.layout_edit_gender, null)
                val radioGroup = e.findViewById<RadioGroup>(R.id.radioGroup)
                var gender: ProfileViewModel.GENDER = ProfileViewModel.GENDER.MALE
                e.findViewById<RadioButton>(R.id.male).isChecked = true
                radioGroup.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.male -> {
                            gender = ProfileViewModel.GENDER.MALE
                        }
                        R.id.female -> {
                            gender = ProfileViewModel.GENDER.FEMALE
                        }
                        R.id.lgbtq -> {
                            gender = ProfileViewModel.GENDER.LGBTQ
                        }
                    }
                }
                val builder = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Gender")
                    setView(e)
                    setCancelable(false)
                    setNegativeButton("Cancel", null)
                    setPositiveButton("Set", null)
                }
                val dialog = builder.create()
                dialog.show()
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                    viewModel.editProfile(gender = gender)
                    dialog.dismiss()
                }
            }
        }
    }

    private fun observeData() {
        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            binding.apply {
                binding.profilePhoto.load(profile.photoUrl)
                profileEmail.text = profile.email
                profileJoined.text = profile.joinedCureya.toDateString()
                profileGender.text = profile.gender
                profileAbout.text = profile.about
                textJoinedGroups.text = profile.joinedGroups.toString()
            }
        }
    }

    private fun isEmailValid(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    companion object {
        private const val TAG = "PROFILE_FRAGMENT"
    }

}