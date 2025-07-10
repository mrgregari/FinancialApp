package com.example.feature_expenses.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_data.di.DataComponent
import com.example.core_ui.di.ViewModelFactoryModule
import dagger.Component

@ExpensesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [ExpensesViewModelModule::class, ViewModelFactoryModule::class]
)
interface ExpensesComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): ExpensesComponent
    }
} 