package com.utesocial.android.remote.interceptor

import com.utesocial.android.core.data.util.Common
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class MainNetworkInterceptor @Inject constructor(private val preferenceManager: PreferenceManager) : Interceptor {

    private val ignoreInUnauthorizedInterceptor = listOf("/login", "/refresh-token")
    override fun intercept(chain: Interceptor.Chain): Response {
        Debug.log("MainNetworkInterceptor:origin", chain.request().url.toString())
        val requestBuilder = chain.request().newBuilder()
//            .addHeader("Content-Type", "application/json")
        for(ignore in ignoreInUnauthorizedInterceptor) {
            if(chain.request().url.toUrl().toString().endsWith(ignore)) {
                Debug.log("MainNetworkInterceptor", requestBuilder
                    .build().url.toString())
                return chain.proceed(requestBuilder.build())
            }
        }
        val token = preferenceManager.getString(Constants.ACCESS_TOKEN)
        val authHeader = Common.getTokenFromHeader(chain.request())
        if(authHeader != null) {
            if(authHeader.isEmpty() || token != authHeader) {
                Debug.log("MainNetworkInterceptor", "set Authorization")
                requestBuilder.header("Authorization", "Bearer $token")
            }
        } else {
            Debug.log("MainNetworkInterceptor", "add Authorization")
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }
//        if(authHeader != null && authHeader.isEmpty()) {
//            Debug.log("MainNetworkInterceptor", "authHeader ! =null && authHeader is empty")
//            if(token != authHeader) {
//                Debug.log("MainNetworkInterceptor", "set Authorization")
//                requestBuilder.header("Authorization", "Bearer $token")
//            }
//        } else {
//            Debug.log("MainNetworkInterceptor", "add Authorization")
//            requestBuilder.addHeader("Authorization", "Bearer $token")
//        }
        return chain.proceed(requestBuilder.build())
    }
}