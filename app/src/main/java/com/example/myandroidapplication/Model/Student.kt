package com.example.myandroidapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Student(
    @PrimaryKey val name:String, val id:String, val avatar:String, var isChecked:Boolean)
