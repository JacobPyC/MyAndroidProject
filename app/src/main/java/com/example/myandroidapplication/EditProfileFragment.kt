package com.example.myandroidapplication

import UserProfileViewModelFactory
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myandroidapplication.databinding.FragmentEditProfileBinding
import com.example.myandroidapplication.repository.UserRepositoryImpl
import com.example.myandroidapplication.viewmodel.UserProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class EditProfileFragment : Fragment() {

    private val userProfileViewModel: UserProfileViewModel by viewModels {
        UserProfileViewModelFactory(
            UserRepositoryImpl(
                FirebaseAuth.getInstance(),
                FirebaseFirestore.getInstance()
            )
        )
    }

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ActivityResultLauncher for handling image capture
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                imageBitmap?.let {
                    binding.ivEditProfileImage.setImageBitmap(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        // Observe the userProfile LiveData
        userProfileViewModel.userProfile.observe(viewLifecycleOwner) { userProfile ->
            if (userProfile != null) {
                binding.etProfileName.setText(userProfile.displayName)
                userProfile.photoUrl?.let { photoUrl ->
                    Glide.with(this).load(photoUrl).into(binding.ivEditProfileImage)
                }

                binding.btnSaveProfile.visibility = View.VISIBLE
            }
        }

        // Set up button listeners
        binding.btnChangeProfileImage.setOnClickListener {
            dispatchTakePictureIntent()
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etProfileName.text.toString()
            val bitmap = (binding.ivEditProfileImage.drawable as? BitmapDrawable)?.bitmap
            val imageUri = bitmap?.let { saveImageToUri(it) }

            if (name.isNotBlank() && imageUri != null) {
                userProfileViewModel.updateUserProfile(name, imageUri)
                findNavController().navigateUp()
            } else {
                // Optionally, show a validation error message
                // For example: showToast("Name or image cannot be empty")
            }
        }

        return view
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePictureLauncher.launch(takePictureIntent)
    }

    private fun saveImageToUri(bitmap: Bitmap): String? {
        val filename = "profile_image_${System.currentTimeMillis()}.png" // Unique file name
        val file = File(requireContext().filesDir, filename)
        return try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()
            file.toURI().toString()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
}