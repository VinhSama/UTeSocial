package com.utesocial.android.core.domain.model

import com.google.gson.annotations.SerializedName
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
    var hometown: String,
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

    enum class UserType(val userType: Int) {
        CollegeStudent(1),
        Lecturer(2),
        Candidate(3)
    }

}