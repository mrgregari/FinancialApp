package com.example.financialapp.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_ui.di.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}