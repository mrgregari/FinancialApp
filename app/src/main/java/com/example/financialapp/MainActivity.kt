package com.example.financialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.financialapp.ui.navigation.AppNavGraph
import com.example.financialapp.ui.theme.FinancialAppTheme
import javax.inject.Inject

/**
 * Main activity of the application.
 * Sets up Compose UI and navigation.
 */
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as FinanceApp).component.inject(this)
        super.onCreate(savedInstanceState)
        setContent {
            FinancialAppTheme(dynamicColor = false) {
                AppNavGraph(viewModelFactory = viewModelFactory)
            }

        }
    }
}
