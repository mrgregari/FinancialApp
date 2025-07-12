package com.example.financialapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.core_ui.theme.FinancialAppTheme
import com.example.financialapp.navigation.AppNavGraph

/**
 * Main activity of the application.
 * Sets up Compose UI and navigation.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinancialAppTheme(dynamicColor = false) {
                AppNavGraph()
            }
        }
    }
}
