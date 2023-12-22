package com.utesocial.android.remote.simpleCallAdapter

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.IllegalArgumentException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class SimpleCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> {
        val enclosingType = returnType as? ParameterizedType
            ?: throw IllegalArgumentException("Resource must be parameterized")
        if(enclosingType.rawType != SimpleCall::class.java) {
            throw IllegalArgumentException("Resource must be parameterized")
        } else {
            val type = enclosingType.actualTypeArguments[0]
            return SimpleCallAdapter<Type>(type)
        }
    }
}