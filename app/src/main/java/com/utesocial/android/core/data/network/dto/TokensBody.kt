package com.utesocial.android.core.data.network.dto


data class TokensBody (
    val accessToken: String,
    val refreshToken: String
) {
    override fun toString(): String {
        return "RefreshTokenResponse(accessToken='$accessToken', refreshToken='$refreshToken')"
    }
}
