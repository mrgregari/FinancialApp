package com.example.feature_categories.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_data.di.DataComponent
import com.example.core_ui.di.ViewModelFactoryModule
import dagger.Component

@CategoriesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [CategoriesViewModelModule::class, ViewModelFactoryModule::class]
)
interface CategoriesComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): CategoriesComponent
    }
} 