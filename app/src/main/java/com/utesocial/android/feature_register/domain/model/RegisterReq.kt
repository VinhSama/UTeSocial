package com.utesocial.android.feature_register.domain.model

data class RegisterReq(
    var identityCode: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var email: String = "",
    var password: String = "",
    var homeTown: String = "",
    var birthDate: String = "",
    var type: Int = 1,
    var details: DetailsReq = DetailsReq.StudentReq()
)