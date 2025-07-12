package com.example.financialapp.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.financialapp.splash.SplashScreen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val splashFinished = rememberSaveable { mutableStateOf(false) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Crossfade(targetState = splashFinished.value) { finished ->
        if (!finished) {
            SplashScreen { splashFinished.value = true }
        } else {
            MainScaffold(navController, currentRoute)
        }
    }
}
