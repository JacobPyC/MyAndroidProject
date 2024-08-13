package com.example.myandroidapplication.Model

import android.content.Context
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myandroidapplication.base.MyApplication
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue

@Entity
data class Student(
    @PrimaryKey val name: String,
    val id: String,
    val avatarUrl: String,
    var isChecked: Boolean,
    var lastUpdated: Long?=null
) {
    companion object {
        var lastUpdated: Long
            get() {
                return MyApplication.Globals.appContext?.getSharedPreferences(
                    "TAG",
                    Context.MODE_PRIVATE
                )?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {

                MyApplication.Globals?.appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.edit()?.putLong(
                        GET_LAST_UPDATED, value
                    )?.apply()
            }
        const val NAME_KEY = "name"
        const val ID_KEY = "id"
        const val IS_CHECKED_KEY = "isChecked"
        const val AVATAR_URL_KEY = "avatarUrl"
        const val LAST_UPDATED = "lastUpdated"
        const val GET_LAST_UPDATED = "get_L ast_Updated"


        fun fromJson(json: Map<String, Any>): Student {

            val id = json[ID_KEY] as String ?: ""
            val name = json[NAME_KEY] as String ?: ""
            val avatarUrl = json[AVATAR_URL_KEY] as String ?: ""
            val isChecked = json[IS_CHECKED_KEY] as Boolean ?: false
            val  student =  Student(id, name, avatarUrl, isChecked)
            val timestamp: Timestamp?=json[LAST_UPDATED] as? Timestamp
            timestamp?.let {
                student.lastUpdated = it.seconds
            }
            return  student

        }
    }


    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                NAME_KEY to name,
                AVATAR_URL_KEY to avatarUrl,
                IS_CHECKED_KEY to isChecked,
                LAST_UPDATED to FieldValue.serverTimestamp()

            )
        }


}

