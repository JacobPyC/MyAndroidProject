package com.example.myandroidapplication.Model
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import com.example.myandroidapplication.dao.AppLocalDatabase
import com.example.myandroidapplication.dao.AppLocalDbRepository
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.Executors

class Model private  constructor(){
    private  val database = AppLocalDatabase.db
    private var executer = Executors.newSingleThreadExecutor()
    private  var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel= FirebaseModel()
    private val students:LiveData<MutableList<Student>>? =null
    companion object {
        val instance: Model = Model()
    }
    interface  GetAllStudentListener{
        fun onComplete(student: List<Student>)
    }
fun getAllStudents():LiveData<MutableList<Student>>{
    return students ?: database.studentDao().getAll()
}
    fun RefreshAllStudents(){
      val lastUpdated:Long = Student.lastUpdated
        firebaseModel.getAllStudent(lastUpdated){ List ->
            Log.i("TAG","Firebase returns  ${List.size}, lastUpdate:$lastUpdated")

                    executer.execute {
var time = lastUpdated
                        for(student in List){

                            database.studentDao().insert(student)
                            student.lastUpdated?.let{
                                if (time < it )
                                    time = student.lastUpdated?: System.currentTimeMillis()
                            }
                    }
                        Student.lastUpdated = time

                    }
        }
    }

    fun addStudent(student:Student,callback: () -> Unit){
        firebaseModel.addStudent(student,callback)
    }
  }


