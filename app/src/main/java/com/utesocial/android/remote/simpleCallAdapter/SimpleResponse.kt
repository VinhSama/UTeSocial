package com.utesocial.android.remote.simpleCallAdapter

import com.utesocial.android.remote.networkState.Error
import com.utesocial.android.remote.networkState.NetworkState
import com.utesocial.android.remote.networkState.Status

class SimpleResponse<T>(private var networkState: NetworkState, private val handler: () -> T) {
    fun getNetworkState(): NetworkState {
        return networkState
    }

    fun setNetworkState(networkState: NetworkState) {
        this.networkState = networkState
    }

    fun getResponseBody(): T {
        return handler()
    }

    fun isSuccessful(): Boolean {
        return networkState.getStatus() == Status.SUCCESS
    }

    fun isFailure(): Boolean {
        return networkState.getStatus() == Status.FAILED
    }

    fun isRunning(): Boolean {
        return networkState.getStatus() == Status.RUNNING
    }

    fun getError(): Error? {
        return networkState.getError()
    }

    override fun toString(): String {
        return "SimpleResponse(networkState=$networkState, handler=$handler)"
    }


}