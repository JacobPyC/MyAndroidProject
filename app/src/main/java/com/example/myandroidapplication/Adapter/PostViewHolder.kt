package com.example.myandroidapplication.Adapter

import PostViewModel
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Post
import com.example.myandroidapplication.R
import com.example.myandroidapplication.databinding.ItemPostBinding

class PostViewHolder(
    private val binding: ItemPostBinding,
    private val postViewModel: PostViewModel // Add this parameter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.textViewPostContent.text = post.textContent
        // Load image or GIF into imageViewPost if needed

        binding.buttonEditPost.setOnClickListener {
            // Navigate to EditPostFragment with post ID
            val bundle = Bundle().apply {
                putString("postId", post.id)
            }
            itemView.findNavController().navigate(R.id.action_allPostsFragment_to_editPostFragment, bundle)
        }

        binding.buttonDeletePost.setOnClickListener {
            // Delete post
            postViewModel.deletePost(post) {
                // Optional: Provide feedback to the user (e.g., Toast message)
            }
        }
    }
}
