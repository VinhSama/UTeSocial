package com.utesocial.android.remote.networkState

sealed class Error(val errorType: ErrorType, var undefinedMessage: String? = null) {
    data object UndefinedError : Error(ErrorType.UNDEFINED)
    data object ConnectionTimeoutError : Error(ErrorType.CONNECTION_TIMEOUT)
    data object RequestTimeoutError : Error(ErrorType.REQUEST_TIMEOUT)
    data object NetworkProblemError : Error(ErrorType.NETWORK_PROBLEM)
    data object UnknownErrorOccurred : Error(ErrorType.UNKNOWN)
    data object RequestCanceled : Error(ErrorType.REQUEST_CANCELED)

    data class CustomError(val customMessage: String) : Error(ErrorType.UNDEFINED, customMessage)

    companion object {
        fun customError(customMessage: String): Error {
            return CustomError(customMessage)
        }
    }
}