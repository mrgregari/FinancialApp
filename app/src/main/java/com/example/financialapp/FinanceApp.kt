package com.example.financialapp

import android.app.Application
import com.example.core_data.di.DaggerDataComponent
import com.example.core_data.di.DataComponent
import com.example.core_data.di.DataComponentProvider

/**
 * Main application class.
 * Initializes dependency injection component.
 */
class FinanceApp : Application(), DataComponentProvider {

    override val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(this) }

    override fun onCreate() {
        super.onCreate()
    }
}