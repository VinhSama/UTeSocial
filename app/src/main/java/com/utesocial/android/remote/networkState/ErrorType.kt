package com.utesocial.android.remote.networkState

import com.utesocial.android.R

enum class ErrorType(val stringResId: Int) {
    CONNECTION_TIMEOUT(R.string.response_connection_timeout),
    REQUEST_TIMEOUT(R.string.response_request_timeout),
    NETWORK_PROBLEM(R.string.response_network_problem),
    REQUEST_CANCELED(R.string.response_request_canceled),
    UNKNOWN(R.string.has_error),
    UNDEFINED(0)
}