package com.example.myandroidapplication

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

class StudentsRecyclerViewActivity : AppCompatActivity() {
    var studentsRecyclerView: RecyclerView? = null
    var students: MutableList<Student>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students_recycler_view)

        students = Model.instance.students

        studentsRecyclerView = findViewById(R.id.rvStudentRecyclerList)
        studentsRecyclerView?.setHasFixedSize(true)
        //set the layout manager
        studentsRecyclerView?.layoutManager= LinearLayoutManager(this)
//        //set the adapter
        studentsRecyclerView?.adapter = StudentsRecyclerAdapter()

        val adapter =StudentsRecyclerAdapter()
        adapter.listener = object : OnItemClickListener{
            override fun onItemClick(position: Int) {
                Log.i("TAG","StudentsRecyclerAdapter: Position Clicked $position")
            }

            override fun onStudentClicked(student: Student?) {
Log.i("TAG","Student $student")
            }

        }
        studentsRecyclerView?.adapter=adapter
    }
    interface OnItemClickListener{
        fun onItemClick(position: Int)
        fun onStudentClicked(student: Student?)
    }
        inner class StudentViewHolder(val itemView: View,val listener: OnItemClickListener?) : RecyclerView.ViewHolder(itemView) {


            var nameTextView: TextView? = null
            var idTextView: TextView? = null
            var studentCheckBox: CheckBox? = null
            var student:Student? =null
            init {
                nameTextView = itemView.findViewById(R.id.tvStudentListRowName)
                idTextView = itemView.findViewById(R.id.tvStudentListRowID)
                studentCheckBox = itemView.findViewById(R.id.cbStudentListRow)

                studentCheckBox?.setOnClickListener {
                    val student = students?.get(adapterPosition)
                    student?.isChecked = studentCheckBox?.isChecked ?: false
                }
                itemView.setOnClickListener{
                    Log.i("TAG","StudentViewHolder: Position Clicked $adapterPosition")
                    listener?.onItemClick(adapterPosition)
                    listener?.onStudentClicked(student)
                }
            }

            fun bind(student: Student?) {
                this.student=student
                nameTextView?.text = student?.name
                idTextView?.text = student?.id
                studentCheckBox?.apply {
                    isChecked = student?.isChecked ?: false

                }

            }
        }

        inner class StudentsRecyclerAdapter : RecyclerView.Adapter<StudentViewHolder>() {
            var listener:OnItemClickListener?=null

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
                val itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.student_layout_row, parent, false)
                return StudentViewHolder(itemView,listener)


            }

            override fun getItemCount(): Int = students?.size ?: 0

            override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {

                val student = students?.get(position)
                holder.bind(student)
            }

        }
    }
