package com.utesocial.android.core.presentation.util

import androidx.room.TypeConverter
import java.util.Date

object Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?) : Date? {
       return if(value != null) {
           Date(value)
       } else {
           null
       }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time
    }
}