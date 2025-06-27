package com.example.financialapp.di.module

import android.content.Context
import com.example.financialapp.data.api.AuthInterceptor
import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.network.NetworkState
import com.example.financialapp.data.network.RetryInterceptor
import com.example.financialapp.di.ApplicationScope
import com.example.financialapp.di.DefaultDispatcher
import com.example.financialapp.di.IODispatcher
import com.example.financialapp.di.MainDispatcher
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
    @ApplicationScope
    @IODispatcher
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @ApplicationScope
    @DefaultDispatcher
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Provides
    @ApplicationScope
    @MainDispatcher
    fun provideMainDispatcher() = Dispatchers.Main

    @Provides
    @ApplicationScope
    fun provideAuthInterceptor(context: Context): AuthInterceptor {
        return AuthInterceptor(context)
    }

    @Provides
    @ApplicationScope
    fun provideRetryInterceptor(): RetryInterceptor {
        return RetryInterceptor()
    }

    @Provides
    @ApplicationScope
    fun provideNetworkState(context: Context): NetworkState {
        return NetworkState(context)
    }

    @Provides
    @ApplicationScope
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
    @ApplicationScope
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @ApplicationScope
    fun provideFinancialApi(retrofit: Retrofit): FinancialApi {
        return retrofit.create(FinancialApi::class.java)
    }
}