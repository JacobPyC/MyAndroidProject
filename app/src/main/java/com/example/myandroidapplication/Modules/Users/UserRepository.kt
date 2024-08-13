package com.example.myandroidapplication.repository

import com.example.myandroidapplication.Model.User
interface UserRepository {
    suspend fun getCurrentUser(): User?
    suspend fun updateUserProfile(user: User, name: String, imageUri: String)
    suspend fun fetchUserData(userId: String): User?
}
