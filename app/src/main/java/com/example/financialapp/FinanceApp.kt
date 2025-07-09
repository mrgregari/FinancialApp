package com.example.financialapp

import android.app.Application
import com.example.financialapp.di.ApplicationComponent
import com.example.financialapp.di.DaggerApplicationComponent
import com.example.core_data.di.DataComponent
import com.example.core_data.di.DaggerDataComponent

/**
 * Main application class.
 * Initializes dependency injection component.
 */
class FinanceApp : Application() {

    lateinit var appComponent: ApplicationComponent
    lateinit var dataComponent: DataComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.factory().create(this)
        dataComponent = DaggerDataComponent.factory().create(appComponent)
    }
}