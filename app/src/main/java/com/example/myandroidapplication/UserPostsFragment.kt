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
import com.example.myandroidapplication.Model.Post
import com.example.myandroidapplication.databinding.FragmentUserPostsBinding
import com.example.myandroidapplication.ui.PostAdapter
import com.google.firebase.auth.FirebaseAuth

class UserPostsFragment : Fragment() {

    private lateinit var postViewModel: PostViewModel
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserPostsBinding.inflate(inflater, container, false)
        postViewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        // Initialize RecyclerView
        val recyclerView = binding.recyclerViewUserPosts
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Get the current user ID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Initialize Adapter with postViewModel
        postAdapter = PostAdapter(
            posts = listOf(), // Initial empty list
            currentUserId = currentUserId,
            onEditClick = { post -> openEditPost(post) },
            onDeleteClick = { post -> deletePost(post) },
        )

        recyclerView.adapter = postAdapter

        // Observe user-specific posts
        postViewModel.getUserPosts().observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts)
        }

        return binding.root
    }

    private fun openEditPost(post: Post) {
        val bundle = Bundle().apply {
            putString("postId", post.id)
        }
        findNavController().navigate(R.id.action_userPostsFragment_to_editPostFragment, bundle)
    }

    private fun deletePost(post: Post) {
        postViewModel.deletePost(post) {
            // Handle success or failure, e.g., show a Toast message
            // For example: Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
        }
    }
}

