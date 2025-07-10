package com.example.feature_incomes.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_data.di.DataComponent
import com.example.core_ui.di.ViewModelFactoryModule
import dagger.Component

@IncomesScope
@Component(
    dependencies = [DataComponent::class],
    modules = [IncomesViewModelModule::class, ViewModelFactoryModule::class]
)
interface IncomesComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): IncomesComponent
    }
} 