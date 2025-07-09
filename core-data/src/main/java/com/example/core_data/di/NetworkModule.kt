package com.example.core_data.di

import android.content.Context
import com.example.core_data.network.NetworkState
import com.example.core_data.network.RetryInterceptor
import com.example.core_data.remote.api.AuthInterceptor
import com.example.core_data.remote.api.FinancialApi
import com.example.core_data.di.DefaultDispatcher
import com.example.core_data.di.IODispatcher
import com.example.core_data.di.MainDispatcher
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
object NetworkModule {

    private const val BASE_URL = "https://shmr-finance.ru/api/v1/"

    @Provides
    @IODispatcher
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Provides
    @MainDispatcher
    fun provideMainDispatcher() = Dispatchers.Main

    @Provides
    fun provideAuthInterceptor(context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Provides
    fun provideRetryInterceptor(): RetryInterceptor {
        return RetryInterceptor()
    }

    @Provides
    fun provideNetworkState(context: Context): NetworkState {
        return NetworkState(context)
    }

    @Provides
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        retryInterceptor: RetryInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(retryInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideFinancialApi(retrofit: Retrofit): FinancialApi {
        return retrofit.create(FinancialApi::class.java)
    }
} 