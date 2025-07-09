package com.example.feature_categories.di

import androidx.lifecycle.ViewModel
import com.example.core_ui.di.ViewModelKey
import com.example.feature_categories.presentation.CategoriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface CategoriesViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel
} 