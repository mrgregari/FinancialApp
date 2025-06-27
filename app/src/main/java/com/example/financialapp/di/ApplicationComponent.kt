package com.example.financialapp.di

import android.content.Context
import com.example.financialapp.MainActivity
import com.example.financialapp.di.module.NetworkModule
import com.example.financialapp.di.module.RepositoryModule
import com.example.financialapp.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ViewModelModule::class,
        RepositoryModule::class,
        NetworkModule::class
    ]
)

interface ApplicationComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context
        ) : ApplicationComponent
    }
}