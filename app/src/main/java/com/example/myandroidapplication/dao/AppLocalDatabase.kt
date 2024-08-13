package com.example.myandroidapplication.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.base.MyApplication
import com.example.myandroidapplication.Model.Post

@Database(entities = [Student::class, Post::class], version = 5)
@TypeConverters(TimestampConverter::class)
abstract class AppLocalDbRepository : RoomDatabase() {
    abstract fun studentDao(): StudentDao
    abstract fun postDao(): PostDao
}

object AppLocalDatabase {
    val db: AppLocalDbRepository by lazy {
        val context = MyApplication.Globals.appContext
            ?: throw IllegalStateException("Application context not available")
        Room.databaseBuilder(
            context,
            AppLocalDbRepository::class.java,
            "dbFileName.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
