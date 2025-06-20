package com.example.financialapp.data.api

import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context
import com.example.financialapp.R

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = context.getString(R.string.api_token)
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
} 