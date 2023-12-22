package com.utesocial.android.core.data.util

import android.content.Context
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.utesocial.android.R
import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.networkState.ErrorType
import okhttp3.Request
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.roundToInt

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

    private fun currentDate() : Date {
        return Calendar.getInstance().time
    }
    private fun getTimeDistanceInMinutes(time: Long) : Int {
        val timeDistance = currentDate().time - time
        return ((abs(timeDistance) / 1000).toFloat() / 60).roundToInt()
    }

    fun dateFormatReadable(date: Date): String? {
        val timeDiff = Math.abs(date.time - Date().time)
        val hoursDiff = TimeUnit.HOURS.convert(timeDiff, TimeUnit.MILLISECONDS)
        return if (hoursDiff < 24) {
            SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
        } else {
            SimpleDateFormat("HH:mm dd-MM-yyyy", Locale.getDefault()).format(date)
        }
    }
    fun getTimeAgo(date: Date?, ctx: Context): String? {
        if (date == null) {
            return null
        }
        val time = date.time
        val curDate: Date = currentDate()
        val now = curDate.time
        if (time > now || time <= 0) {
            return null
        }
        val dim: Int = getTimeDistanceInMinutes(time)
        var timeAgo: String? = null
        if (dim == 0) {
            timeAgo =
                ctx.resources.getString(R.string.date_util_term_less) + " " + ctx.resources.getString(
                    R.string.date_util_term_a
                ) + " " + ctx.resources.getString(R.string.date_util_unit_minute)
        } else if (dim == 1) {
            return "1 " + ctx.resources.getString(R.string.date_util_unit_minute)
        } else if (dim in 2..44) {
            timeAgo =
                dim.toString() + " " + ctx.resources.getString(R.string.date_util_unit_minutes)
        } else if (dim in 45..89) {
            timeAgo =
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + ctx.resources.getString(
                    R.string.date_util_term_an
                ) + " " + ctx.resources.getString(R.string.date_util_unit_hour)
        } else if (dim in 90..1439) {
            timeAgo =
                ctx.resources.getString(R.string.date_util_prefix_about) + " " + Math.round(dim.toFloat() / 60) + " " + ctx.resources.getString(
                    R.string.date_util_unit_hours
                )
        } else if (dim in 1440..2519) {
            timeAgo = "1 " + ctx.resources.getString(R.string.date_util_unit_day)
        } else {
            timeAgo = dateFormatReadable(date)
            return timeAgo
        }
        return timeAgo + " " + ctx.resources.getString(R.string.date_util_suffix)
    }

}