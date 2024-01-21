package com.example.myandroidapplication.Modules.Students

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.ModelDownloadListener
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
    var students: List<Student>? = null
var adapter:StudentsRecyclerAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_recycler_view)
         adapter = StudentsRecyclerAdapter(students)

        Model.instance.getAllStudent{students ->
            this.students=students
            adapter?.students=students
            adapter?.notifyDataSetChanged()
        }
       studentsRecyclerView = findViewById(R.id.rvStudentRecyclerList)
        studentsRecyclerView?.setHasFixedSize(true)
        studentsRecyclerView?.layoutManager = LinearLayoutManager(this)
    adapter?.listener = object :OnItemClickListener{
        override fun onItemClick(position: Int) {
            Log.i("TAG","StudentsRecyclerAdapter:Position Clicked $position")
        }

        override fun onStudentClicked(student: Student?) {
            Log.i("TAG","STUDENT $student")
        }

    }
        studentsRecyclerView?.adapter = adapter
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onStudentClicked(student: Student?)
    }

    override fun onResume() {
        super.onResume()
        Model.instance.getAllStudent{students ->
            this.students=students
            adapter?.students=students
            adapter?.notifyDataSetChanged()
        }
    }
}
