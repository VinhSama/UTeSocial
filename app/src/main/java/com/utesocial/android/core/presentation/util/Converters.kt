package com.utesocial.android.core.presentation.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    @TypeConverter
    fun fromListToJson(list: List<String>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToList(json: String) : List<String> {
        val typeToken = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromHashMapToString(hashmap: HashMap<String, String>) : String {
        return Gson().toJson(hashmap)
    }
    @TypeConverter
    fun fromStringToHashMap(json: String) : HashMap<String, String> {
        val typeToken = object : TypeToken<HashMap<String, String>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
}