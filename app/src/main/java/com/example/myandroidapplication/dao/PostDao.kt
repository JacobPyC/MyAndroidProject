package com.example.myandroidapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.myandroidapplication.Model.Post

@Dao
interface PostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg posts: Post) // Use suspend for coroutines

    @Update
    suspend fun update(post: Post) // Use suspend for coroutines

    @Delete
    suspend fun delete(post: Post) // Use suspend for coroutines

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Update
    suspend fun updateAll(posts: List<Post>)

    @Delete
    suspend fun deleteAll(posts: List<Post>)

    @Query("SELECT * FROM Post ORDER BY timestamp DESC")
    fun getAllPosts(): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE userId = :userId")
    fun getPostsByUser(userId: String): LiveData<List<Post>>

    @Query("SELECT * FROM Post WHERE id = :id")
    fun getPostById(id: String): LiveData<Post>
}
