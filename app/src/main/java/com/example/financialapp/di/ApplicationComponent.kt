package com.example.financialapp.di

import android.content.Context
import com.example.financialapp.MainActivity
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