package com.example.myandroidapplication.Modules.Students.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.StudentsRecyclerViewActivity
import com.example.myandroidapplication.R

 class StudentsRecyclerAdapter (var students: MutableList<Student>?): RecyclerView.Adapter<StudentViewHolder>() {
    var listener: StudentsRecyclerViewActivity.OnItemClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_layout_row, parent, false)
        return StudentViewHolder(itemView,listener,students)


    }

    override fun getItemCount(): Int = students?.size ?: 0

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

        val student = students?.get(position)
        holder.bind(student)
    }

}