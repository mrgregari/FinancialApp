package com.example.core_network.network

/**
 * Sealed class representing the result of network operations.
 *
 * @param T Type of data returned on success
 */
sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

}

