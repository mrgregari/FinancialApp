package com.example.feature_categories.di

import com.example.core_data.di.DataComponent
import dagger.Component

@CategoriesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [CategoriesViewModelModule::class]
)
interface CategoriesComponent {
    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): CategoriesComponent
    }
} 