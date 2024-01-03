package com.utesocial.android.feature_search.domain.model

import com.utesocial.android.core.domain.model.User
import java.io.Serializable

data class SearchUser(
    val userId: String,
    val user: User,
    val friendState: String
) : Serializable