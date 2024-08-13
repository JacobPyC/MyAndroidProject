package com.example.myandroidapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val uid: String,
    val displayName: String?,
    val photoUrl: String?
)