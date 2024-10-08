package com.example.myandroidapplication.Model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.memoryCacheSettings
import com.google.firebase.ktx.Firebase

class FirebaseModel {
    private   val db = Firebase.firestore
    companion object{

        const val STUDENT_COLLECTION_PATH = "students"
    }
    init{

        val settings =  firestoreSettings {
            setLocalCacheSettings(memoryCacheSettings {  })
        }
        db.firestoreSettings= settings
    }

    fun getAllStudent(since:Long ,callback: (List<Student>) -> Unit) {
        db.collection(STUDENT_COLLECTION_PATH).whereGreaterThanOrEqualTo(Student.LAST_UPDATED,Timestamp(since,0)).get().addOnCompleteListener{
            when(it.isSuccessful){
                true ->  {
                    val students:MutableList<Student> = mutableListOf()
                    for(json in it.result){


                        val student = Student.fromJson(json.data)
                        students.add(student)
                    }
                    callback(students)
                }
                false -> callback(listOf())
            }
        }
    }

    fun addStudent(student:Student,callback: () -> Unit) {
        db.collection(STUDENT_COLLECTION_PATH).document(student.id).set(student.json).addOnSuccessListener {
            callback()
            Log.w("TAG", "Student Added Successfully")

        }.addOnFailureListener{ e ->
            Log.w("TAG", "Error adding Student", e)

        }
    }




}

