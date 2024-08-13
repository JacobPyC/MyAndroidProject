package com.example.myandroidapplication.repository

import com.example.myandroidapplication.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        return fetchUserData(firebaseUser.uid)
    }

    override suspend fun updateUserProfile(user: User, name: String, imageUri: String) {
        val updates = mapOf(
            "displayName" to name,
            "photoUrl" to imageUri
        )
        firestore.collection("users").document(user.uid).update(updates).await()
    }

    override suspend fun fetchUserData(userId: String): User? {
        val document = firestore.collection("users").document(userId).get().await()
        return if (document.exists()) {
            User(
                uid = document.id,
                displayName = document.getString("displayName"),
                photoUrl = document.getString("photoUrl")
            )
        } else {
            null
        }
    }
}
