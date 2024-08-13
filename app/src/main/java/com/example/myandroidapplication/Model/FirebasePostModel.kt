package com.example.myandroidapplication.Model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.myandroidapplication.dao.AppLocalDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class FirebasePostModel {

    private val postDao = AppLocalDatabase.db.postDao()
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    init {
        observeFirebaseChanges()
    }

    private fun observeFirebaseChanges() {
        postsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.w("FirebasePostModel", "Listen failed.", exception)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val posts = snapshot.toObjects(Post::class.java)
                updateLocalDatabase(posts)
            }
        }
    }

    private fun updateLocalDatabase(posts: List<Post>) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingPosts = postDao.getAllPosts().value.orEmpty()
            val postsToInsert = posts.filterNot { existingPosts.contains(it) }
            val postsToUpdate = posts.filter { existingPosts.contains(it) }
            val postsToDelete = existingPosts.filterNot { posts.contains(it) }

            if (postsToDelete.isNotEmpty()) {
                postDao.deleteAll(postsToDelete)
            }
            if (postsToInsert.isNotEmpty()) {
                postDao.insertAll(postsToInsert)
            }
            if (postsToUpdate.isNotEmpty()) {
                postDao.updateAll(postsToUpdate)
            }
        }
    }

    fun getAllPosts(): LiveData<List<Post>> {
        val localPosts = postDao.getAllPosts() // Get posts from local cache ordered by timestamp
        val liveData = MediatorLiveData<List<Post>>()

        liveData.addSource(localPosts) { posts ->
            liveData.value = posts // Update live data from local cache
        }

        fetchAndCachePostsFromFirebase()

        return liveData
    }

    private fun fetchAndCachePostsFromFirebase() {
        postsCollection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirebasePostModel", "Error fetching posts", exception)
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Post::class.java)
                } ?: emptyList()

                CoroutineScope(Dispatchers.IO).launch {
                    updateLocalDatabase(posts)
                }
            }
    }

    fun getPostsByUser(userId: String): LiveData<List<Post>> {
        val liveData = MutableLiveData<List<Post>>()
        postsCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirebasePostModel", "Error fetching posts by user", exception)
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Post::class.java)
                } ?: emptyList()

                liveData.value = posts

                // Cache posts locally in Room
                CoroutineScope(Dispatchers.IO).launch {
                    updateLocalDatabase(posts)
                }
            }
        return liveData
    }

    fun getPostById(id: String): LiveData<Post> {
        val postLiveData = MutableLiveData<Post>()
        postsCollection.document(id).addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("FirebasePostModel", "Error fetching post by id", exception)
                return@addSnapshotListener
            }

            val post = snapshot?.toObject(Post::class.java)
            postLiveData.value = post

            // Cache post locally in Room
            CoroutineScope(Dispatchers.IO).launch {
                post?.let { postDao.insert(it) }
            }
        }
        return postLiveData
    }

    fun addPost(post: Post, callback: (Boolean) -> Unit) {
        postsCollection.document(post.id)
            .set(post)
            .addOnSuccessListener {
                callback(true)

                // Cache post locally in Room
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.insert(post)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostModel", "Error adding post", e)
                callback(false)
            }
    }

    fun updatePost(post: Post, callback: (Boolean) -> Unit) {
        val postMap = mapOf(
            "contentType" to post.contentType,
            "contentUrl" to post.contentUrl,
            "textContent" to post.textContent,
            "timestamp" to post.timestamp,
            "userId" to post.userId
            // Ensure no redundant fields are included
        )

        postsCollection.document(post.id)
            .update(postMap)
            .addOnSuccessListener {
                callback(true)
                // Update local database
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.update(post)
                    Log.d("FirebasePostModel", "Post updated in local database")
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostModel", "Error updating post", e)
                callback(false)
            }
    }


    fun deletePost(post: Post, callback: (Boolean) -> Unit) {
        postsCollection.document(post.id)
            .delete()
            .addOnSuccessListener {
                callback(true)

                // Delete post locally in Room
                CoroutineScope(Dispatchers.IO).launch {
                    postDao.delete(post)
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirebasePostModel", "Error deleting post", e)
                callback(false)
            }
    }

    // Utility functions for handling images
    fun saveImage(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    fun loadImage(byteArray: ByteArray?): Bitmap? {
        return byteArray?.let {
            BitmapFactory.decodeByteArray(it, 0, it.size)
        }
    }
}
