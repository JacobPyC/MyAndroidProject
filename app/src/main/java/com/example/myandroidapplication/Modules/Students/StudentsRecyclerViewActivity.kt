package com.example.myandroidapplication.Modules.Students

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Model
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.Adapter.StudentsRecyclerAdapter
import com.example.myandroidapplication.R

class StudentsRecyclerViewActivity : AppCompatActivity() {
    var studentsRecyclerView: RecyclerView? = null
    var students: MutableList<Student>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_recycler_view)

    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onStudentClicked(student: Student?)
    }



    }
