package com.example.financialapp.data.network

import com.example.financialapp.R
import com.example.financialapp.di.ApplicationScope
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles error code and returns string id for each code
 */

@ApplicationScope
class ErrorHandler @Inject constructor(
) {

    fun getErrorResId(throwable: Throwable): Int {
        return when (throwable) {
            is UnknownHostException -> R.string.no_internet
            is SocketTimeoutException -> R.string.network_timeout
            is IOException -> R.string.network_error
            is HttpException -> {
                when (throwable.code()) {
                    401 -> R.string.invalid_credentials
                    403 -> R.string.access_denied
                    404 -> R.string.not_found
                    500 -> R.string.server_error
                    502 -> R.string.server_unavailable
                    503 -> R.string.service_unavailable
                    else -> R.string.server_error_with_code
                }
            }
            else -> R.string.unknown_error
        }
    }

}