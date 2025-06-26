package com.example.financialapp.di

import com.example.financialapp.data.repositories.AccountRepositoryImpl
import com.example.financialapp.data.repositories.CategoryRepositoryImpl
import com.example.financialapp.data.repositories.TransactionRepositoryImpl
import com.example.financialapp.domain.repositories.AccountRepository
import com.example.financialapp.domain.repositories.CategoryRepository
import com.example.financialapp.domain.repositories.TransactionRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {
    @Binds
    @ApplicationScope
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @ApplicationScope
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository

    @Binds
    @ApplicationScope
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository
}

