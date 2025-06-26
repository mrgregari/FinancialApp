package com.example.financialapp

import android.app.Application
import com.example.financialapp.di.ApplicationComponent
import com.example.financialapp.di.DaggerApplicationComponent

class FinanceApp : Application() {

    lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        component =  DaggerApplicationComponent.factory().create(this)
    }

}