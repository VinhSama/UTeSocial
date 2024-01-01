package com.utesocial.android.core.presentation.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.utesocial.android.feature_post.domain.model.LikesPostHeader
import com.utesocial.android.feature_post.domain.model.PostResource
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
    @TypeConverter
    fun fromJsonToPostResourceList(json: String) : List<PostResource> {
        val typeToken = object : TypeToken<ArrayList<PostResource>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromPostResourceListToString(list: List<PostResource>) : String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromJsonToLikesPostHeaderList(json: String) : List<LikesPostHeader> {
        val typeToken = object : TypeToken<ArrayList<LikesPostHeader>>() {}.type
        return Gson().fromJson(json, typeToken)
    }
    @TypeConverter
    fun fromLikesPostHeaderListToString(list: List<LikesPostHeader>) : String {
        return Gson().toJson(list)
    }


}