package com.example.myandroidapplication

import PostViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myandroidapplication.databinding.FragmentAllPostsBinding
import com.example.myandroidapplication.ui.PostAdapter
import com.example.myandroidapplication.Model.Post
import com.google.firebase.auth.FirebaseAuth

class AllPostsFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentAllPostsBinding.inflate(inflater, container, false)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        val recyclerView = binding.recyclerViewPosts
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get the current user ID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        postAdapter = PostAdapter(
            posts = listOf(), // Initial empty list
            currentUserId = currentUserId,
            onEditClick = { post -> openEditPost(post) },
            onDeleteClick = { post -> deletePost(post) },
            postViewModel = postViewModel
        )

        recyclerView.adapter = postAdapter

        postViewModel.getAllPosts().observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }

        // Optional: Add listener for adding new posts
        binding.buttonAddPost.setOnClickListener {
            // Navigate to com.example.myandroidapplication.Modules.AddPost.CreatePostFragment
            val action = AllPostsFragmentDirections.actionAllPostsFragmentToCreatePostFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun openEditPost(post: Post) {
        val action = AllPostsFragmentDirections.actionAllPostsFragmentToEditPostFragment(post.id)
        findNavController().navigate(action)
    }


    private fun deletePost(post: Post) {
        postViewModel.deletePost(post) {
            // Handle success or failure, e.g., show a Toast message
        }
    }
}