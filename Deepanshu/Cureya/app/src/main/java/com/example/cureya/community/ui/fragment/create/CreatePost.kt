package com.example.cureya.community.ui.fragment.create

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cureya.community.models.TAG
import com.example.cureya.databinding.CreatePostFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class CreatePost : Fragment() {

    private var _binding: CreatePostFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: CreatePostViewModel
    private var imageUri: Uri? = null

    private lateinit var getContent: ActivityResultLauncher<String>

    private var tag: TAG? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePostFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMembers()
        setClickListeners()
        observeData()
        binding.tagPicker.chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.tagPicker.chipAnxiety.id -> tag = TAG.ANXIETY
                binding.tagPicker.chipDepression.id -> tag = TAG.DEPRESSION
                binding.tagPicker.chipParanoia.id -> tag = TAG.PARANOIA
                binding.tagPicker.chipPsychosis.id -> tag = TAG.PSYCHOSIS
                binding.tagPicker.chipStress.id -> tag = TAG.STRESS
            }
        }
    }

    private fun initMembers() {
        viewModel = ViewModelProvider(this)[CreatePostViewModel::class.java]
        getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                val contentResolver = requireActivity().contentResolver
                imageUri = null
                if (uri != null) {
                    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        cursor.moveToFirst()
                        val size = cursor.getLong(sizeIndex)
                        if (size < 2000000) {
                            imageUri = uri
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "File size exceeded 2MB",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
    }

    private fun setClickListeners() {
        binding.createPostCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.pickImage.setOnClickListener {
            getContent.launch("image/*")
        }
        binding.createPostFab.setOnClickListener {
            if (tag == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select a Tag",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (imageUri == null) {
                Toast.makeText(
                    requireContext(),
                    "Please select a file",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val caption = binding.createPostCaption.text.toString()
                viewModel.createPost(imageUri!!, caption, listOf(tag!!))
            }
        }


    }

    private fun observeData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.createPostLoading.visibility = View.VISIBLE
            } else {
                imageUri = null
                binding.createPostLoading.visibility = View.GONE
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                imageUri = null
            }
        }
        viewModel.currentUser.observe(viewLifecycleOwner) {
            binding.userProfilePhoto.load(it.photoUrl)
            binding.postUserName.text = it.name
        }
    }
}