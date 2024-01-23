package com.example.myandroidapplication.Modules.Students

import StudentsViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Model
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.Adapter.StudentsRecyclerAdapter
import com.example.myandroidapplication.R
import com.example.myandroidapplication.databinding.FragmentStudentsBinding

class StudentsFragment : Fragment() {
    var studentsRecyclerView: RecyclerView? = null
    var adapter:StudentsRecyclerAdapter?=null
    var progressBar: ProgressBar?=null
    private var _binding:FragmentStudentsBinding?= null
    private val binding get()=_binding!!

    private lateinit var viewModel: StudentsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentStudentsBinding.inflate(inflater,container,false)
        val view=  binding.root
progressBar = binding.progressBar
        viewModel= ViewModelProvider(this)[StudentsViewModel::class.java]

        progressBar?.visibility = View.VISIBLE
        adapter = StudentsRecyclerAdapter(viewModel.students)

        Model.instance.getAllStudent{students ->
            viewModel.students= students
            adapter?.students=students
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE
        }

            studentsRecyclerView =binding.rvStudentsFragmentList
        studentsRecyclerView?.setHasFixedSize(true)
        //set the layout manager
        studentsRecyclerView?.layoutManager= LinearLayoutManager(context)
//        //set the adapter
        studentsRecyclerView?.adapter = StudentsRecyclerAdapter(   viewModel.students)

         adapter = StudentsRecyclerAdapter(   viewModel.students )
        adapter?.listener = object : StudentsRecyclerViewActivity.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Log.i("TAG","StudentsRecyclerAdapter: Position Clicked $position")
                val student =    viewModel.students?.get(position)
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
        val addStudentButton:ImageButton = binding.ibtnStudentsFragmentAddStudent
          val action =   Navigation.createNavigateOnClickListener(StudentsFragmentDirections.actionGlobalAddStudentFragment())
        addStudentButton.setOnClickListener(action)

        return  view
    }
    override fun onResume() {

        super.onResume()
        progressBar?.visibility = View.VISIBLE

        Model.instance.getAllStudent{students ->
            this.viewModel.students=students
            adapter?.students=students
            adapter?.notifyDataSetChanged()
            progressBar?.visibility = View.GONE

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }
}