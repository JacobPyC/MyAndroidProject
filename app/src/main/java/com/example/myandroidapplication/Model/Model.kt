package com.example.myandroidapplication.Model

class Model private  constructor(){
    val students: MutableList<Student> = ArrayList()
    companion object{
        val instance:Model=Model()
    }
  
}