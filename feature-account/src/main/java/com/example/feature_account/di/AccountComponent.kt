package com.example.feature_account.di

import com.example.core_data.di.DataComponent
import dagger.Component

@AccountScope
@Component(
    dependencies = [DataComponent::class],
    modules = [AccountViewModelModule::class]
)
interface AccountComponent {


    @Component.Factory
    interface Factory {
        fun create(dataComponent: DataComponent): AccountComponent
    }
} 