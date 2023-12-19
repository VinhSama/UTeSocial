package com.utesocial.android.core.data.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.networkState.ErrorType
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
import java.util.Objects

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
                val gson = GsonBuilder().setLenient().serializeNulls().create()
                val jsonElement = gson.fromJson(jsonObject.toString(), JsonElement::class.java)
                if(jsonElement.asJsonObject.has(field)) {
                    data = gson.fromJson(jsonElement.asJsonObject.get(field), T::class.java)
                }
            } else {
                throw NullPointerException()
            }
        } catch (e: JSONException) {
            e.message?.let { Debug.log("getDetailMessageBody", it) }
            return null
        } catch (e: IOException) {
            e.message?.let { Debug.log("getDetailMessageBody", it) }
            return null
        }
        Debug.log("getDetailMessageBody", "data:${data}")

        return data
    }

    fun getErrorMessageFromState(error: Error, context: Context) : String? {
        if(Objects.equals(error.errorType, ErrorType.UNDEFINED)) {
            return error.undefinedMessage
        }
        return context.getString(error.errorType.stringResId)
    }

}