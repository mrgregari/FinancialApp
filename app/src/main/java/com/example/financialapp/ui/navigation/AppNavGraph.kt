package com.example.financialapp.ui.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financialapp.di.ViewModelFactory
import com.example.financialapp.ui.screens.SplashScreen

@Composable
fun AppNavGraph(viewModelFactory: ViewModelProvider.Factory) {
    val navController = rememberNavController()
    val splashFinished = rememberSaveable { mutableStateOf(false) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Crossfade(targetState = splashFinished.value) { finished ->
        if (!finished) {
            SplashScreen { splashFinished.value = true }
        } else {
            MainScaffold(viewModelFactory, navController, currentRoute)
        }
    }
}
