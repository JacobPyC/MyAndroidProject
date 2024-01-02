package com.example.myandroidapplication.Modules.Students

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Model
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.Adapter.StudentsRecyclerAdapter
import com.example.myandroidapplication.R

class StudentsFragment : Fragment() {
    var studentsRecyclerView: RecyclerView? = null
    var students: MutableList<Student>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_students, container, false)

        students = Model.instance.students

        studentsRecyclerView = view.findViewById(R.id.rvStudentsFragmentList)
        studentsRecyclerView?.setHasFixedSize(true)
        //set the layout manager
        studentsRecyclerView?.layoutManager= LinearLayoutManager(context)
//        //set the adapter
        studentsRecyclerView?.adapter = StudentsRecyclerAdapter(students)

        val adapter = StudentsRecyclerAdapter(students )
        adapter.listener = object : StudentsRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","StudentsRecyclerAdapter: Position Clicked $position")
            }

            override fun onStudentClicked(student: Student?) {
                Log.i("TAG","Student $student")
            }

        }
        studentsRecyclerView?.adapter=adapter
        return  view
    }

}