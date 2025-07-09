package com.example.feature_incomes.di

import com.example.core_data.di.DataComponent
import dagger.Component

@IncomesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [IncomesViewModelModule::class]
)
interface IncomesComponent {
    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): IncomesComponent
    }
} 