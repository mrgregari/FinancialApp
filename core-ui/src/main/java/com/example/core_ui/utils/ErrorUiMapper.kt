package com.example.core_ui.utils

import com.example.core_network.network.NetworkErrorType
import com.example.core_ui.R

fun getErrorResId(errorType: NetworkErrorType): Int = when (errorType) {
    NetworkErrorType.NO_INTERNET -> R.string.no_internet
    NetworkErrorType.TIMEOUT -> R.string.network_timeout
    NetworkErrorType.NETWORK -> R.string.network_error
    NetworkErrorType.INVALID_CREDENTIALS -> R.string.invalid_credentials
    NetworkErrorType.ACCESS_DENIED -> R.string.access_denied
    NetworkErrorType.NOT_FOUND -> R.string.not_found
    NetworkErrorType.SERVER_ERROR -> R.string.server_error
    NetworkErrorType.SERVER_UNAVAILABLE -> R.string.server_unavailable
    NetworkErrorType.SERVICE_UNAVAILABLE -> R.string.service_unavailable
    NetworkErrorType.SERVER_ERROR_WITH_CODE -> R.string.server_error_with_code
    NetworkErrorType.UNKNOWN -> R.string.unknown_error
} 