package com.example.myandroidapplication.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myandroidapplication.Model.Student

@Dao
interface StudentDao {
    @Query("Select * FROM Student")
    fun getAll():LiveData< MutableList<Student>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    fun insert(vararg students:Student)

    @Delete
    fun delete (student: Student)

    @Query("SELECT * FROM Student  WHERE id =:id")
    fun getStudentById(id:String):LiveData<Student>
}