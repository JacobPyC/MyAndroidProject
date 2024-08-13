package com.example.myandroidapplication.Modules.Posts

import PostViewModel
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myandroidapplication.Model.Post
import com.example.myandroidapplication.R
import com.example.myandroidapplication.databinding.FragmentEditPostBinding
import com.google.firebase.auth.FirebaseAuth

class EditPostFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private var postId: String? = null
    private var _binding: FragmentEditPostBinding? = null
    private val binding get() = _binding!!
    private var selectedGifUrl: String? = null
    private var selectedImageUri: Uri? = null
    private val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val selectImageResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let {
                    selectedImageUri = it
                    binding.imageViewPost.visibility = View.VISIBLE
                    Glide.with(this).load(it).into(binding.imageViewPost)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPostBinding.inflate(inflater, container, false)

        // Listen for GIF selection
        setFragmentResultListener("gifRequestKey") { _, bundle ->
            selectedGifUrl = bundle.getString("selectedGifUrl")
            Log.d("EditPostFragment", "Selected GIF URL: $selectedGifUrl")
            selectedGifUrl?.let { url ->
                binding.imageViewPost.visibility = View.VISIBLE
                Glide.with(this).asGif().load(url).into(binding.imageViewPost)
            }
        }

        // Listen for image selection
        setFragmentResultListener("imageRequestKey") { _, bundle ->
            selectedImageUri = bundle.getParcelable("selectedImageUri")
            Log.d("EditPostFragment", "Selected Image URI: $selectedImageUri")
            selectedImageUri?.let { uri ->
                binding.imageViewPost.visibility = View.VISIBLE
                Glide.with(this).load(uri).into(binding.imageViewPost)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        postId = arguments?.getString("postId")
        postId?.let { id ->
            postViewModel.getPostById(id).observe(viewLifecycleOwner) { post ->
                post?.let {
                    // Set the post content in the EditText
                    binding.editTextPostContent.setText(it.textContent)

                    // Only update the image or GIF if the user has not selected a new one
                    if (selectedGifUrl != null || selectedImageUri != null) {
                        // Show the user-selected content
                        binding.imageViewPost.visibility = View.VISIBLE
                        selectedGifUrl?.let { url ->
                            Glide.with(this).asGif().load(url).into(binding.imageViewPost)
                        }
                        selectedImageUri?.let { uri ->
                            Glide.with(this).load(uri).into(binding.imageViewPost)
                        }
                    } else {
                        // No user selection, show the post's content
                        when (it.contentType) {
                            Post.ContentType.GIF -> {
                                binding.imageViewPost.visibility = View.VISIBLE
                                Glide.with(this).asGif().load(it.contentUrl).into(binding.imageViewPost)
                            }
                            Post.ContentType.IMAGE -> {
                                binding.imageViewPost.visibility = View.VISIBLE
                                Glide.with(this).load(it.contentUrl).into(binding.imageViewPost)
                            }
                            else -> binding.imageViewPost.visibility = View.GONE
                        }
                    }
                }
            }
        }

        binding.buttonSelectGif.setOnClickListener {
            findNavController().navigate(R.id.action_editPostFragment_to_gifListFragment)
        }

        binding.buttonSelectImage.setOnClickListener {
            selectImage()
        }

        binding.buttonSavePost.setOnClickListener {
            savePost()
        }

        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageResultLauncher.launch(intent)
    }

    private fun savePost() {
        val textContent = binding.editTextPostContent.text.toString()

        postId?.let { id ->
            postViewModel.getPostById(id).observe(viewLifecycleOwner) { existingPost ->
                existingPost?.let {
                    // Determine the content type and URL based on the selected content
                    val contentType = when {
                        selectedGifUrl != null -> Post.ContentType.GIF
                        selectedImageUri != null -> Post.ContentType.IMAGE
                        else -> it.contentType
                    }

                    val contentUrl = when {
                        selectedGifUrl != null -> selectedGifUrl
                        selectedImageUri != null -> selectedImageUri.toString()
                        else -> it.contentUrl
                    }

                    Log.d("EditPostFragment", "Saving Post with contentType: $contentType and contentUrl: $contentUrl")

                    // Create the updated post
                    val updatedPost = it.copy(
                        textContent = textContent,
                        contentType = contentType,
                        contentUrl = contentUrl
                    )

                    // Update the post in the ViewModel
                    postViewModel.updatePost(updatedPost) {

                        findNavController().navigateUp()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
