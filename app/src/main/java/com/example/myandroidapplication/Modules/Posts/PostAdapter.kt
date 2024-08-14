package com.example.myandroidapplication.ui

import PostViewModel
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myandroidapplication.Model.Post
import com.example.myandroidapplication.R
import com.example.myandroidapplication.databinding.ItemPostBinding

class PostAdapter(
    private var posts: List<Post>,
    private val currentUserId: String,
    private val onEditClick: (Post) -> Unit,
    private val onDeleteClick: (Post) -> Unit,
    private val postViewModel: PostViewModel // Include postViewModel
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(private val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            binding.textViewPostContent.text = post.textContent

            when (post.contentType) {
                Post.ContentType.TEXT -> {
                    binding.imageViewPost.visibility = ViewGroup.GONE
                }
                Post.ContentType.IMAGE, Post.ContentType.GIF -> {
                    binding.imageViewPost.visibility = ViewGroup.VISIBLE
                    post.contentUrl?.let { url ->
                        Glide.with(binding.root.context)
                            .asBitmap()
                            .error(
                                R.drawable.batman)// Use asBitmap for both GIFs and images
                            .load(url)
                            .into(binding.imageViewPost)
                    }
                }
            }

            if (post.userId == currentUserId) {
                binding.buttonEditPost.visibility = ViewGroup.VISIBLE
                binding.buttonDeletePost.visibility = ViewGroup.VISIBLE
            } else {
                binding.buttonEditPost.visibility = ViewGroup.GONE
                binding.buttonDeletePost.visibility = ViewGroup.GONE
            }

            binding.buttonEditPost.setOnClickListener {
                onEditClick(post)
            }

            binding.buttonDeletePost.setOnClickListener {
                onDeleteClick(post)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
            Log.d("PostAdapter", "Updating posts list. New size: ${newPosts.size}")
            this.posts = newPosts
            notifyDataSetChanged()

    }
}
