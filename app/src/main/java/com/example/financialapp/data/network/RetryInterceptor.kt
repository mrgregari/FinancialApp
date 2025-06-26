package com.example.financialapp.data.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor : Interceptor {
    
    companion object {
        private const val MAX_RETRIES = 3
        private const val RETRY_DELAY_MS = 2000L // 2 секунды
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

                if (response.code() == 500 && attempt < MAX_RETRIES) {
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