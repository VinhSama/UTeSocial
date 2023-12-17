package com.utesocial.android.core.data.util

import com.google.gson.Gson
import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.networkState.NetworkState
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import java.net.SocketTimeoutException

object Common {
    fun getTokenFromHeader(request: Request) : String? {
        val token = request.header("Authorization")
        return token?.substring(7, token.length - 1)
    }
    fun errorHandler(call: Call<*>, t: Throwable) : Error {
        return when (t) {
            is SocketTimeoutException -> Error.ConnectionTimeoutError
            is HttpException -> {
                val response = t.response()
                if(response != null) {
                    try {
                        response.errorBody()?.let {
                            Error.CustomError(it.string())
                        } ?: Error.UnknownErrorOccurred
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Error.UnknownErrorOccurred
                    }
                }
                Error.UnknownErrorOccurred
            }
            is IOException -> Error.RequestTimeoutError
            else -> {
                if(call.isCanceled) {
                    Error.RequestCanceled
                } else {
                    Error.NetworkProblemError
                }
            }
        }
    }
    inline fun <reified T> getDetailMessageBody(responseBody: ResponseBody?, field: String) : T? {
        var data: T? = null
        try {
            if(responseBody != null && field.isNotEmpty()) {
                val jsonObject = JSONObject(responseBody.string())
                if(jsonObject.has(field)) {
                    val fieldJSONObject = jsonObject.getJSONObject(field)
                    data = Gson().fromJson(fieldJSONObject.toString(), T::class.java)
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: JSONException) {
            e.message?.let { Debug.log("getDetailMessageBody", it) }
        } catch (e: IOException) {
            e.message?.let { Debug.log("getDetailMessageBody", it) }
        }
        return data
    }

}