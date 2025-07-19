package com.example.core_data.di

import android.content.Context
import android.content.SharedPreferences
import com.example.core_domain.repositories.AccountRepository
import com.example.core_domain.repositories.CategoryRepository
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_data.sync.TransactionSyncWorkerFactory
import dagger.BindsInstance
import dagger.Component

@DataScope
@Component(
    modules = [DataModule::class, NetworkModule::class]
)
interface DataComponent {
    fun accountRepository(): AccountRepository
    fun transactionRepository(): TransactionRepository
    fun categoryRepository(): CategoryRepository

    fun transactionSyncWorkerFactory(): TransactionSyncWorkerFactory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }

    fun context(): Context
    fun sharedPreferences(): SharedPreferences
} 