package com.example.feature_settings.di

import androidx.lifecycle.ViewModelProvider
import com.example.core_data.di.DataComponent
import com.example.core_ui.di.ViewModelFactoryModule
import dagger.Component

@SettingsScope
@Component(
    dependencies = [DataComponent::class],
    modules = [SettingsViewModelModule::class, ViewModelFactoryModule::class]
)
interface SettingsComponent {
    fun viewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): SettingsComponent
    }
} 