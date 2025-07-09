package com.example.core_data.di

import com.example.core_data.data.repositories.AccountRepositoryImpl
import com.example.core_data.data.repositories.CategoryRepositoryImpl
import com.example.core_data.data.repositories.TransactionRepositoryImpl
import com.example.core_data.domain.repositories.AccountRepository
import com.example.core_data.domain.repositories.CategoryRepository
import com.example.core_data.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DataModule {
    @Binds
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository
}