package com.example.myandroidapplication.Model
import android.os.Looper
import android.util.Log
import androidx.core.os.HandlerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myandroidapplication.dao.AppLocalDatabase

import java.util.concurrent.Executors

class Model private  constructor(){

    enum class  LoadingState{
        LOADING,
        LOADED
    }
    private  val database = AppLocalDatabase.db
    private var executer = Executors.newSingleThreadExecutor()
    private  var mainHandler = HandlerCompat.createAsync(Looper.getMainLooper())
    private val firebaseModel= FirebaseModel()
    private val students:LiveData<MutableList<Student>>? = null
    val studentsListLoadingState: MutableLiveData<LoadingState> = MutableLiveData(LoadingState.LOADED)

    companion object {
        val instance: Model = Model()
    }
    interface  GetAllStudentsListener{
        fun onComplete(student: List<Student>)
    }
fun getAllStudents():LiveData<MutableList<Student>>{
    refreshAllStudents()
    return students ?: database.studentDao().getAll()
}
    fun refreshAllStudents(){
        studentsListLoadingState.value =LoadingState.LOADING
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
                        studentsListLoadingState.postValue(LoadingState.LOADED)

                    }
        }
    }

    fun addStudent(student:Student,callback: () -> Unit){
        firebaseModel.addStudent(student){

            refreshAllStudents()
            callback()
        }
    }
  }


