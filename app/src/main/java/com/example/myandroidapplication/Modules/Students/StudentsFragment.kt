package com.example.myandroidapplication.Modules.Students

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Model
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.Adapter.StudentsRecyclerAdapter
import com.example.myandroidapplication.R

class StudentsFragment : Fragment() {
    var studentsRecyclerView: RecyclerView? = null
    var students: List<Student>? = null
    var adapter:StudentsRecyclerAdapter?=null
    var progressBar: ProgressBar?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=  inflater.inflate(R.layout.fragment_students, container, false)
progressBar = view.findViewById(R.id.progressBar)

        progressBar?.visibility = View.VISIBLE
        adapter = StudentsRecyclerAdapter(students)

        Model.instance.getAllStudent{students ->
            adapter?.students=students
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }

            studentsRecyclerView = view.findViewById(R.id.rvStudentsFragmentList)
        studentsRecyclerView?.setHasFixedSize(true)
        //set the layout manager
        studentsRecyclerView?.layoutManager= LinearLayoutManager(context)
//        //set the adapter
        studentsRecyclerView?.adapter = StudentsRecyclerAdapter(students)

         adapter = StudentsRecyclerAdapter(students )
        adapter?.listener = object : StudentsRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","StudentsRecyclerAdapter: Position Clicked $position")
                val student = students?.get(position)
                student?.let {
                    val action= StudentsFragmentDirections.actionStudentsFragmentToBlueFragment(it.name)
                    Navigation.findNavController(view).navigate(action)


                }


            }

            override fun onStudentClicked(student: Student?) {
                Log.i("TAG","Student $student")
            }

        }
        studentsRecyclerView?.adapter=adapter
        val addStudentButton:ImageButton = view.findViewById(R.id.ibtnStudentsFragmentAddStudent)
          val action =   Navigation.createNavigateOnClickListener(StudentsFragmentDirections.actionGlobalAddStudentFragment())
        addStudentButton.setOnClickListener(action)

        return  view
    }
    override fun onResume() {

        super.onResume()
        progressBar?.visibility = View.VISIBLE

        Model.instance.getAllStudent{students ->
            this.students=students
            adapter?.students=students
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE

        }
    }
}