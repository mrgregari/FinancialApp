package com.example.financialapp.data.network

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: Throwable) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

}

