package com.utesocial.android.core.presentation.util

import com.utesocial.android.remote.networkState.Error

class ResponseException(
    val error: Error?
) : Exception("HTTP Exception")