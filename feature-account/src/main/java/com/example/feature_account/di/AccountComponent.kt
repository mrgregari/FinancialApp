package com.example.feature_account.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_data.di.DataComponent
import com.example.core_ui.di.ViewModelFactoryModule
import dagger.Component

@AccountScope
@Component(
    dependencies = [DataComponent::class],
    modules = [AccountViewModelModule::class, ViewModelFactoryModule::class]
)
interface AccountComponent {

    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): AccountComponent
    }
} 