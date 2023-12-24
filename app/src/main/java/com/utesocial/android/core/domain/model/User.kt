package com.utesocial.android.core.domain.model

import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.utesocial.android.core.data.util.Common
import java.io.Serializable
import java.util.Date

data class User(
    @SerializedName("_id")
    var userId: String,
    var identityCode: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var username: String,
    var homeTown: String,
    var birthdate: String,
    var avatar: Avatar? = null,
    var status: Int?,
    var friends: List<String>,
    var friendCount: Int?,
    var type: UserType?,
    var createdAt: Date?,
    var updatedAt: Date?,
    var details: UserDetails?
) : Serializable {

    companion object {
        val EMPTY: User = User()
    }

    constructor() : this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        null,
        null,
        emptyList(),
        null,
        null,
        null,
        null,
        null
    )
    @JsonAdapter(UserTypeAdapter::class)
    enum class UserType(val type: Int) {
        UNDEFINED(0),
        CollegeStudent(1),
        Lecturer(2),
        Candidate(3);

        companion object {
            fun fromInt(value: Int): UserType? {
                return entries.find { it.type == value }
            }
        }

    }

    class UserTypeAdapter : TypeAdapter<UserType>() {
        override fun write(out: JsonWriter?, value: UserType?) {
            out?.value(value?.type)
        }

        override fun read(`in`: JsonReader?): UserType? {
            if(`in`?.peek() == JsonToken.NULL) {
                `in`.nextNull()
                return null
            }
            return UserType.fromInt(`in`?.nextInt() ?: 0)
        }

    }

}