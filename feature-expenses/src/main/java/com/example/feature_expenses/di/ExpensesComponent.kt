package com.example.feature_expenses.di

import com.example.core_data.di.DataComponent
import dagger.Component

@ExpensesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [ExpensesViewModelModule::class]
)
interface ExpensesComponent {
    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): ExpensesComponent
    }
} 