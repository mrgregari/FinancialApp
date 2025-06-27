package com.example.financialapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.financialapp.ui.screens.settings.SettingsScreen
import com.example.financialapp.ui.screens.account.AccountScreen
import com.example.financialapp.ui.screens.categories.CategoriesScreen
import com.example.financialapp.ui.screens.expenses.ExpensesScreen
import com.example.financialapp.ui.screens.expensesHistory.ExpensesHistoryScreen
import com.example.financialapp.ui.screens.incomes.IncomeScreen
import com.example.financialapp.ui.screens.incomesHistory.IncomesHistoryScreen

@Composable
fun AppNavHost(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Expenses.route,
        modifier = modifier
    ) {
        composable(Screen.Expenses.route) {
            ExpensesScreen(
                viewModelFactory = viewModelFactory,
                navController = navController
            )
        }
        composable(Screen.ExpensesHistory.route) {
            ExpensesHistoryScreen(
                viewModelFactory = viewModelFactory,
                navController = navController
            )
        }
        composable(Screen.IncomesHistory.route) {
            IncomesHistoryScreen(
                viewModelFactory = viewModelFactory,
                navController = navController
            )
        }
        composable(Screen.Income.route) {
            IncomeScreen(
                viewModelFactory = viewModelFactory,
                navController = navController
            )
        }
        composable(Screen.Account.route) {
            AccountScreen(viewModelFactory = viewModelFactory)
        }
        composable(Screen.Categories.route) {
            CategoriesScreen(viewModelFactory = viewModelFactory)
        }
        composable(Screen.Settings.route) { SettingsScreen() }
    }
}
