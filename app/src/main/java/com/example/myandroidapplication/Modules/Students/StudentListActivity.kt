package com.example.myandroidapplication.Modules.Students

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import com.example.myandroidapplication.Model.Model
import com.example.myandroidapplication.Model.Student
import com.example.myandroidapplication.R

class StudentListActivity : AppCompatActivity() {
    var studentsListView:ListView?= null
    var students:MutableList<Student>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_list)


        students = Model.instance.students

        studentsListView= findViewById(R.id.lvStudentList)
        studentsListView?.adapter = StudentsListAdapter(students)
        studentsListView?.setOnItemClickListener{_,_,position,_ ->
            Log.i("TAG","Row was clicked at:$position")

        }
    }
    class StudentsListAdapter(val students:MutableList<Student>?): BaseAdapter(){
        override fun getCount(): Int = students?.size ?:0

        override fun getItem(position: Int): Any {
            TODO("Not yet implemented")
        }

        override fun getItemId(position: Int): Long =0

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val student = students?.get(position)
              var view: View? = null
              if (convertView==null){
          view = LayoutInflater.from(parent?.context).inflate(R.layout.student_layout_row,parent,false)
                  val studentCheckBox:CheckBox?=view?.findViewById(R.id.cbStudentListRow)
                  studentCheckBox?.setOnClickListener{
                      (studentCheckBox.tag as? Int)?.let{tag ->
                          var student = students?.get(tag)
                          student?.isChecked=studentCheckBox.isChecked ?: false

                      }
                  }
      }
                 view=view?:convertView
            val nameTextView:TextView?  = view?.findViewById(R.id.tvStudentListRowName)
            val idTextView:TextView?  = view?.findViewById(R.id.tvStudentListRowID)
            val studentCheckBox:CheckBox?  = view?.findViewById(R.id.cbStudentListRow)


            nameTextView?.text  = student?.name
            idTextView?.text  = student?.id
            studentCheckBox?.apply {
                isChecked  = student?.isChecked ?: false
                tag=position

            }



            return view!!
              }

    }
}