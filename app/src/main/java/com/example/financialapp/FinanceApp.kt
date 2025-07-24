package com.example.financialapp

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.core_data.di.DaggerDataComponent
import com.example.core_data.di.DataComponent
import com.example.core_data.di.DataComponentProvider
import android.content.Context
import android.content.ContextWrapper
import java.util.Locale

/**
 * Main application class.
 * Initializes dependency injection component.
 */
class LocaleHelper(base: Context) : ContextWrapper(base) {
    companion object {
        fun wrap(context: Context, language: String): ContextWrapper {
            val config = android.content.res.Configuration(context.resources.configuration)
            val locale = Locale(language)
            Locale.setDefault(locale)
            config.setLocale(locale)
            return ContextWrapper(context.createConfigurationContext(config))
        }
    }
}

class FinanceApp : Application(), DataComponentProvider, Configuration.Provider {

    override val dataComponent: DataComponent by lazy { DaggerDataComponent.factory().create(this) }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

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