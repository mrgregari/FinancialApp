package com.example.core_network.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * OkHttp Interceptor that automatically retries failed network requests.
 *
 * Retries requests with a delay between attempts if a network error or HTTP 500 occurs
 */

class RetryInterceptor : Interceptor {
    
    companion object {
        private const val MAX_RETRIES = 2
        private const val RETRY_DELAY_MS = 500L // 0.5 second
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var exception: IOException? = null

        for (attempt in 0..MAX_RETRIES) {
            try {
                response = chain.proceed(request)

                if (response.isSuccessful) {
                    return response
                }

                if (response.code == 500 && attempt < MAX_RETRIES) {
                    response.close()
                    Thread.sleep(RETRY_DELAY_MS)
                    continue
                }

                return response
                
            } catch (e: IOException) {
                exception = e

                if (attempt == MAX_RETRIES) {
                    throw e
                }

                try {
                    Thread.sleep(RETRY_DELAY_MS)
                } catch (interruptedException: InterruptedException) {
                    Thread.currentThread().interrupt()
                    throw IOException("Retry interrupted", exception)
                }
            }
        }

        throw exception ?: IOException("Unknown error occurred")
    }
} 