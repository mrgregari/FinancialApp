package com.example.financialapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.core_data.di.DaggerDataComponent
import com.example.core_data.di.DataComponent
import com.example.core_data.di.DataComponentProvider

/**
 * Main application class.
 * Initializes dependency injection component.
 */
class FinanceApp : Application(), DataComponentProvider, Configuration.Provider {

    override val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(this) }

    override fun onCreate() {
        super.onCreate()
        val workerFactory = dataComponent.transactionSyncWorkerFactory()
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        )
    }

    override val workManagerConfiguration: Configuration
        get() {
            val workerFactory = dataComponent.transactionSyncWorkerFactory()
            return Configuration.Builder()
                .setWorkerFactory(workerFactory)
                .build()
        }
}