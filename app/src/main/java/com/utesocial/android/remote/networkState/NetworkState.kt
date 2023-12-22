package com.utesocial.android.remote.networkState

class NetworkState constructor(
    private val status: Status,
    private val error: Error?
){

    companion object {
        val LOADED = NetworkState(Status.SUCCESS, null)
        val LOADING = NetworkState(Status.RUNNING, null)
        val FIRST_LOADING = NetworkState(Status.FIRST_LOADING, null)
        val FIRST_FAILED = NetworkState(Status.FIRST_FAILED, null)
        val EMPTY = NetworkState(Status.EMPTY, null)
    }

    fun getStatus(): Status {
        return status
    }

    fun getError(): Error? {
        return error
    }

    override fun toString(): String {
        return "NetworkState{status=$status, error=$error}"
    }
}