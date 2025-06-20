package com.example.financialapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financialapp.ui.screens.*
import com.example.financialapp.ui.screens.categories.CategoriesScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen() }
        composable(Screen.Income.route) { IncomeScreen() }
        composable(Screen.Account.route) { AccountScreen() }
        composable(Screen.Categories.route) { CategoriesScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
