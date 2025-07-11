package com.example.financialapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.feature_account.presentation.AccountScreen
import com.example.feature_account.presentation.accountEdit.AccountEditScreen
import com.example.feature_categories.presentation.CategoriesScreen
import com.example.feature_expenses.presentation.addExpense.AddExpenseScreen
import com.example.feature_incomes.presentation.IncomeScreen
import com.example.feature_incomes.presentation.incomesHistory.IncomesHistoryScreen
import com.example.feature_settings.presentation.SettingsScreen
import com.example.feature_expenses.presentation.todayExpenses.ExpensesScreen
import com.example.feature_expenses.presentation.expensesHistory.ExpensesHistoryScreen

@Composable
fun AppNavHost(
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
                onHistoryClick = {
                    navController.navigate(Screen.ExpensesHistory.route)
                },
                onAddClick = {
                    navController.navigate(Screen.AddExpense.route)
                }
            )
        }
        composable(Screen.ExpensesHistory.route) {
            ExpensesHistoryScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Screen.IncomesHistory.route) {
            IncomesHistoryScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Screen.Income.route) {
            IncomeScreen(
                onHistoryClick = {
                    navController.navigate(Screen.IncomesHistory.route)
                }
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(
                onEditAccount = { id ->
                    navController.popBackStack(Screen.Account.route, inclusive = true)
                    navController.navigate(Screen.EditAccount.routeWithIdAccount(id))
                }
            )
        }
        composable(Screen.Categories.route) {
            CategoriesScreen()
        }
        composable(Screen.Settings.route) { SettingsScreen() }

        composable(
            Screen.EditAccount.route,
            arguments = listOf(navArgument("accountId") { type = NavType.IntType })
        ) { backStackEntry ->
            val accountId = backStackEntry.arguments?.getInt("accountId") ?: return@composable
            AccountEditScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Account.route) {
                        popUpTo(Screen.EditAccount.route) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                accountId = accountId
            )
        }

        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Expenses.route) {
                        popUpTo(Screen.AddExpense.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}
