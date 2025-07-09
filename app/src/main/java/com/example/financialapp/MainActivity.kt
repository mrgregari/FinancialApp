package com.example.financialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.core_ui.theme.FinancialAppTheme
import com.example.financialapp.navigation.AppNavGraph
import javax.inject.Inject

/**
 * Main activity of the application.
 * Sets up Compose UI and navigation.
 */
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as FinanceApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            FinancialAppTheme(dynamicColor = false) {
                AppNavGraph(viewModelFactory = viewModelFactory)
            }
        }
    }
}
