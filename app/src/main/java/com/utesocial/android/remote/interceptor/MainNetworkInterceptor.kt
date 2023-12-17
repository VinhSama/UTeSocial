package com.utesocial.android.remote.interceptor

import com.utesocial.android.core.data.util.Common
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class MainNetworkInterceptor(private val preferenceManager: PreferenceManager) : Interceptor {

    private val ignoreInUnauthorizedInterceptor = listOf("/login", "/refresh-token")
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder : Request.Builder = chain.request().newBuilder()
        for(ignore in ignoreInUnauthorizedInterceptor) {
            if(chain.request().url.toUrl().toString().endsWith(ignore)) {
                return chain.proceed(requestBuilder.build())
            }
        }
        val token = preferenceManager.getString(Constants.ACCESS_TOKEN)
        val authHeader = Common.getTokenFromHeader(chain.request())
        if(authHeader != null && authHeader.isEmpty()) {
            if(token != authHeader) {
                requestBuilder.header("Authorization", "Bearer $token")
            }
        } else {
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
        return chain.proceed(requestBuilder.build())
    }
}