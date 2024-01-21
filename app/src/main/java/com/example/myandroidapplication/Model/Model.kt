package com.example.myandroidapplication.Model
import android.os.Looper
import android.widget.ProgressBar
import androidx.core.os.HandlerCompat
import com.example.myandroidapplication.dao.AppLocalDatabase
import com.example.myandroidapplication.dao.AppLocalDbRepository
import java.util.concurrent.Executors

class Model private  constructor(){
    private  val database = AppLocalDatabase.db
    private var executer = Executors.newSingleThreadExecutor()
    private  var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    companion object {
        val instance: Model = Model()
    }

    fun getAllStudent(callback: (List<Student>) -> Unit) {
        executer.execute{
            val students = database.studentDao().getAll()
            mainHandler.post {
                //Main Thread
                callback(students)
            }
        }
    }
    fun addStudent(student:Student,callback: () -> Unit){
        executer.execute(){
            Thread.sleep(5000)
            database.studentDao().insert(student)
            mainHandler.post{
                callback()
            }
        }
    }
  }
