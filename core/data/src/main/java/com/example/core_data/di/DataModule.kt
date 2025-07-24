package com.example.core_data.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.dao.CategoryDao
import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.local.database.AppDatabase
import com.example.core_data.repositories.AccountRepositoryImpl
import com.example.core_data.repositories.CategoryRepositoryImpl
import com.example.core_data.repositories.TransactionRepositoryImpl
import com.example.core_data.repositories.ThemeRepositoryImpl
import com.example.core_domain.repositories.AccountRepository
import com.example.core_domain.repositories.CategoryRepository
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_domain.repositories.ThemeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

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

    @Binds
    abstract fun bindThemeRepository(impl: ThemeRepositoryImpl): ThemeRepository

    companion object {
        @Provides
        fun provideAccountDao(
            context: Context
        ): AccountDao {
            return AppDatabase.getInstance(context).accountDao()
        }

        @Provides
        fun provideCategoryDao(
            context: Context
        ): CategoryDao {
            return AppDatabase.getInstance(context).categoryDao()
        }

        @Provides
        fun provideTransactionDao(
            context: Context
        ): TransactionDao {
            return AppDatabase.getInstance(context).transactionDao()
        }

        @Provides
        fun provideSyncPrefs(context: Context): SharedPreferences =
            context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    }
}