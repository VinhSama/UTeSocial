package com.utesocial.android.remote.interceptor

import androidx.lifecycle.MutableLiveData
import com.utesocial.android.core.data.network.dto.TokensBody
import com.utesocial.android.core.data.util.Common
import com.utesocial.android.core.data.util.Constants
import com.utesocial.android.core.data.util.Debug
import com.utesocial.android.core.data.util.PreferenceManager
import com.utesocial.android.feature_login.data.network.LoginApi
import com.utesocial.android.feature_login.data.network.dto.AppResponse
import com.utesocial.android.remote.simpleCallAdapter.SimpleResponse
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap
import java.util.Objects
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Provider

class UnauthorizedInterceptor @Inject constructor(
    private val preferenceManager: PreferenceManager,
    private val loginApiProvider: Provider<LoginApi>,
    private val unauthorizedEventBroadcast: MutableLiveData<Boolean>,
) : Interceptor {


    @Synchronized
    fun getAccessToken() : String? {
        return preferenceManager
            .getString(Constants.ACCESS_TOKEN, null)
    }
    @Synchronized
    fun updateAccessToken(accessToken: String) {
        preferenceManager.putString(Constants.ACCESS_TOKEN, accessToken)
    }
    @Synchronized
    fun updateRefreshToken(refreshToken: String) {
        preferenceManager.putString(Constants.REFRESH_TOKEN, refreshToken)
    }

    @Synchronized
    fun getRefreshToken() : String? {
        return preferenceManager
            .getString(Constants.REFRESH_TOKEN)
    }

    companion object {
        const val TAG = "UnauthorizedInterceptor"
        private val accessTokenReference = AtomicReference<String>()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var isTokenChanged = false
        accessTokenReference.set(getAccessToken())
        if(!Objects.equals(Common.getTokenFromHeader(request),getAccessToken())) {
            Debug.log("UnauthorizedInterceptor", "isTokenChanged")
            isTokenChanged = true
        }
        if(isTokenChanged) {
            request = request.newBuilder()
                .header("Authorization", "Bearer " + accessTokenReference.get())
                .build()
        }
        var response = chain.proceed(request)
        when(response.code) {
            401 -> {
                response.close()
                var originRequest : Request
                synchronized(this) {
                    val currentTokenInRequest = Common.getTokenFromHeader(request)
                    if(currentTokenInRequest != null && !Objects.equals(currentTokenInRequest, getAccessToken())) {
                        val tokenResponse = refreshToken()
                        if(tokenResponse != null && tokenResponse.isSuccessful()) {
                            originRequest = request.newBuilder()
                                .removeHeader("Authorization")
                                .build()
                            response = chain.proceed(originRequest)
                        } else {
                            /* Post unauthorized event to global. Change user state to sign-out  */
                            unauthorizedEventBroadcast.postValue(true)
                        }
                    } else {
                        originRequest = request.newBuilder()
                            .removeHeader("Authorization")
                            .build()
                        response = chain.proceed(originRequest)
                    }
                    return response
                }
            }
            403 -> {
                response.body?.apply {
                    val errorMessage = Common.getDetailMessageBody<String>(this, "error")
                    if(errorMessage.equals("Invalid token")) {
                        unauthorizedEventBroadcast.postValue(true)
                    }
                }
                return response
            }
            else -> return response
        }
//        if(response.code == 401) {
//            response.close()
//            var originRequest : Request
//            synchronized(this) {
//                val currentTokenInRequest = Common.getTokenFromHeader(request)
//                if(currentTokenInRequest != null && !Objects.equals(currentTokenInRequest, getAccessToken())) {
//                    val tokenResponse = refreshToken()
//                    if(tokenResponse != null && tokenResponse.isSuccessful()) {
//                        originRequest = request.newBuilder()
//                            .removeHeader("Authorization")
//                            .build()
//                        response = chain.proceed(originRequest)
//                    } else {
//                        /* Post unauthorized event to global. Change user state to sign-out  */
//                        unauthorizedEventBroadcast.postValue(true)
//                    }
//                } else {
//                    originRequest = request.newBuilder()
//                        .removeHeader("Authorization")
//                        .build()
//                    response = chain.proceed(originRequest)
//                }
//                return response
//            }
//        }

    }
    @Synchronized
    fun refreshToken() : SimpleResponse<AppResponse<TokensBody>?>? {
        val refreshToken = getRefreshToken()
        if(!refreshToken.isNullOrEmpty()) {
            val body = HashMap<String, String>()
            body[Constants.REFRESH_TOKEN] = refreshToken
            val response : SimpleResponse<AppResponse<TokensBody>?>
            try {
                response = loginApiProvider.get().refreshToken(body).execute()
                if(response.isSuccessful()) {
                    response.getResponseBody()?.let {
                        updateAccessToken(it.data.accessToken)
                        updateRefreshToken(it.data.refreshToken)
                    }

                }
                return response
            } catch (e: Exception) {
                Debug.log("UnauthorizedInterceptor", "refreshToken:${e.message}")
                e.printStackTrace()
            }
        }
        return null
    }
}