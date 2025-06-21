package com.example.financialapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financialapp.ui.screens.*
import com.example.financialapp.ui.screens.account.AccountScreen
import com.example.financialapp.ui.screens.categories.CategoriesScreen
import com.example.financialapp.ui.screens.expenses.ExpensesScreen
import com.example.financialapp.ui.screens.expenseshistory.ExpensesHistoryScreen
import com.example.financialapp.ui.screens.incomes.IncomeScreen
import com.example.financialapp.ui.screens.incomeshistory.IncomesHistoryScreen
import com.example.financialapp.ui.screens.SettingsScreen
import com.example.financialapp.ui.screens.SplashScreen

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        composable(Screen.Expenses.route) { ExpensesScreen(navController = navController) }
        composable(Screen.ExpensesHistory.route) { ExpensesHistoryScreen(navController = navController) }
        composable(Screen.IncomesHistory.route) {
            IncomesHistoryScreen(navController = navController)
        }
        composable(Screen.Income.route) {
            IncomeScreen(navController = navController)
        }
        composable(Screen.Account.route) {
            AccountScreen()
        }
        composable(Screen.Categories.route) {
            CategoriesScreen()
        }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
