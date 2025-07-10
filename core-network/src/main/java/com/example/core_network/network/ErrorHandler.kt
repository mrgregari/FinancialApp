package com.example.core_network.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * Handles error code and returns string id for each code
 */

class ErrorHandler @Inject constructor() {

    fun getErrorType(throwable: Throwable): NetworkErrorType {
        return when (throwable) {
            is UnknownHostException -> NetworkErrorType.NO_INTERNET
            is SocketTimeoutException -> NetworkErrorType.TIMEOUT
            is IOException -> NetworkErrorType.NETWORK
            is HttpException -> {
                when (throwable.code()) {
                    401 -> NetworkErrorType.INVALID_CREDENTIALS
                    403 -> NetworkErrorType.ACCESS_DENIED
                    404 -> NetworkErrorType.NOT_FOUND
                    500 -> NetworkErrorType.SERVER_ERROR
                    502 -> NetworkErrorType.SERVER_UNAVAILABLE
                    503 -> NetworkErrorType.SERVICE_UNAVAILABLE
                    else -> NetworkErrorType.SERVER_ERROR_WITH_CODE
                }
            }
            else -> NetworkErrorType.UNKNOWN
        }
    }

}