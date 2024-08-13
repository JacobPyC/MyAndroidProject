package com.example.myandroidapplication.Modules.AddPost


import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myandroidapplication.Model.Post
import com.example.myandroidapplication.R
import com.example.myandroidapplication.databinding.FragmentCreatePostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CreatePostFragment : Fragment() {
    private lateinit var binding: FragmentCreatePostBinding
    private var selectedGifUrl: String? = null
    private var selectedImageUri: Uri? = null
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false)

        // Set up listener for GIF selection from GifListFragment
        setFragmentResultListener("gifRequestKey") { _, bundle ->
            selectedGifUrl = bundle.getString("selectedGifUrl")
            selectedGifUrl?.let { url ->
                Glide.with(this).asGif().load(url).into(binding.imageViewPost)
            }
        }

        // Initialize image picker launcher
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = uri
                Glide.with(this).load(uri).into(binding.imageViewPost)
            }
        }

        // Set up button click listener to select a GIF
        binding.buttonSelectGif.setOnClickListener {
            findNavController().navigate(R.id.action_createPostFragment_to_gifListFragment)
        }

        // Set up button click listener to select an image
        binding.buttonSelectImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        // Set up button click listener to create the post
        binding.buttonCreatePost.setOnClickListener {
            val postContent = binding.editTextPostContent.text.toString()

            if (postContent.isNotEmpty() && (selectedGifUrl != null || selectedImageUri != null)) {
                uploadPostToFirestore(postContent, selectedGifUrl, selectedImageUri)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please add content and select an image or GIF",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    private fun uploadPostToFirestore(postContent: String, gifUrl: String?, imageUri: Uri?) {
        val db = FirebaseFirestore.getInstance()
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userId = currentUser?.uid ?: return

        // Generate a unique post ID
        val postId = db.collection("posts").document().id

        val post = Post(
            id = postId,
            userId = userId,
            contentType = when {
                gifUrl != null -> Post.ContentType.GIF
                imageUri != null -> Post.ContentType.IMAGE
                else -> throw IllegalArgumentException("No content selected")
            },
            contentUrl = gifUrl ?: imageUri.toString(),
            textContent = postContent,
            timestamp = System.currentTimeMillis() // Store timestamp as milliseconds
        )

        Log.d("CreatePostFragment", "Posting: ${post.json}")

        db.collection("posts")
            .document(postId)
            .set(post.json)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Post uploaded!", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack(R.id.allPostsFragment, false)
            }
            .addOnFailureListener { e ->
                Log.e("CreatePostFragment", "Failed to upload post", e)
                Toast.makeText(requireContext(), "Failed to upload post", Toast.LENGTH_SHORT).show()
            }
    }
}
