package com.example.financialapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import com.example.financialapp.di.ViewModelFactory

@Composable
fun MainScaffold(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavHostController,
    currentRoute: String?
) {
    Scaffold(
        bottomBar = { AppBottomBar(navController, currentRoute) }
    ) { padding ->
        AppNavHost(viewModelFactory, navController, Modifier.padding(padding))
    }
}
