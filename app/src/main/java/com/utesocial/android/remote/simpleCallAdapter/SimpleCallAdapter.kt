package com.utesocial.android.remote.simpleCallAdapter

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class SimpleCallAdapter<R>(private val responseType: Type) : CallAdapter<R, SimpleCall<R>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): SimpleCall<R> {
        return SimpleCall(call)
    }
}