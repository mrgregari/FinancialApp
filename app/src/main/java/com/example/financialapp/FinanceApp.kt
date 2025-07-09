package com.example.financialapp

import android.app.Application
import com.example.core_data.di.DaggerDataComponent
import com.example.financialapp.di.ApplicationComponent
import com.example.core_data.di.DataComponent

/**
 * Main application class.
 * Initializes dependency injection component.
 */
class FinanceApp : Application() {

    lateinit var dataComponent: DataComponent
    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =  DaggerApplicationComponent.factory().create(this)
        dataComponent = DaggerDataComponent.factory().create(this)
    }
}