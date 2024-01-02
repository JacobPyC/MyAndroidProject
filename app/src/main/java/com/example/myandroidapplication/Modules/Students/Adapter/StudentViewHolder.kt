package com.example.myandroidapplication.Modules.Students.Adapter

import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.Modules.Students.StudentsRecyclerViewActivity
import com.example.myandroidapplication.R

 class StudentViewHolder(val itemView: View, val listener: StudentsRecyclerViewActivity.OnItemClickListener?,  var students: MutableList<Student>? ) : RecyclerView.ViewHolder(itemView) {


    var nameTextView: TextView? = null
    var idTextView: TextView? = null
    var studentCheckBox: CheckBox? = null
    var student: Student? =null
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