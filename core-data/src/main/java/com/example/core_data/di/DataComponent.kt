package com.example.core_data.di

import android.content.Context
import com.example.core_data.domain.repositories.AccountRepository
import com.example.core_data.domain.repositories.TransactionRepository
import dagger.BindsInstance
import dagger.Component

@DataScope
@Component(
    modules = [DataModule::class, NetworkModule::class]
)
interface DataComponent {
    fun accountRepository(): AccountRepository
    fun transactionRepository(): TransactionRepository

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): DataComponent
    }
} 