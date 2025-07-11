package com.example.financialapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController

@Composable
fun MainScaffold(
    navController: NavHostController,
    currentRoute: String?
) {
    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) }
    ) { padding ->
        AppNavHost(navController, Modifier.padding(padding))
    }
}
