package com.example.myandroidapplication.dao

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.base.MyApplication
import java.lang.IllegalStateException

@Database(entities = [Student::class], version = 1)
abstract class AppLocalDbRepository: RoomDatabase(){
    abstract fun studentDao(): StudentDao
}
object AppLocalDatabase {}
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
