package com.example.financialapp.di

import com.example.financialapp.data.repositories.AccountRepositoryImpl
import com.example.financialapp.domain.repositories.CategoryRepository
import com.example.financialapp.data.repositories.CategoryRepositoryImpl
import com.example.financialapp.domain.repositories.AccountRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindAccountRepository(
        impl: AccountRepositoryImpl
    ): AccountRepository
} 