package com.example.myandroidapplication.dao

import androidx.room.TypeConverter
import com.google.firebase.Timestamp

class TimestampConverter {
    @TypeConverter
    fun toTimestamp(milliseconds: Long?): Timestamp? {
        return milliseconds?.let { Timestamp(it / 1000, 0) }
    }

    @TypeConverter
    fun fromTimestamp(timestamp: Timestamp?): Long? {
        return timestamp?.seconds?.times(1000)
    }
}
