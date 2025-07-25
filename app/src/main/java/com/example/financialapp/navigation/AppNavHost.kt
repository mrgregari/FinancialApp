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
import com.example.feature_expenses.presentation.analytics.ExpenseAnalyticsScreen
import com.example.feature_expenses.presentation.editExpense.EditExpenseScreen
import com.example.feature_incomes.presentation.todayIncomes.IncomeScreen
import com.example.feature_incomes.presentation.incomesHistory.IncomesHistoryScreen
import com.example.feature_incomes.presentation.editIncome.EditIncomeScreen
import com.example.feature_settings.presentation.SettingsScreen
import com.example.feature_expenses.presentation.todayExpenses.ExpensesScreen
import com.example.feature_expenses.presentation.expensesHistory.ExpensesHistoryScreen
import com.example.feature_incomes.presentation.addIncome.AddIncomeScreen

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
                },
                onItemClick = { id ->
                    navController.navigate(Screen.EditExpense.routeWithIdExpense(id))
                }
            )
        }
        composable(Screen.ExpensesHistory.route) {
            ExpensesHistoryScreen(
                onNavigateUp = { navController.navigateUp() },
                onItemClick = { id ->
                    navController.navigate(Screen.EditExpense.routeWithIdExpense(id))
                },
                onAnalyticsClick = {
                    navController.navigate(Screen.ExpenseAnalytics.route)
                }
            )
        }
        composable(Screen.IncomesHistory.route) {
            IncomesHistoryScreen(
                onNavigateUp = { navController.navigateUp() },
                onItemClick = { id ->
                    navController.navigate(Screen.EditIncome.routeWithIdIncome(id))
                },
                onAnalyticsClick = {
                    navController.navigate(Screen.IncomeAnalytics.route)
                }
            )
        }
        composable(Screen.Income.route) {
            IncomeScreen(
                onHistoryClick = {
                    navController.navigate(Screen.IncomesHistory.route)
                },
                onAddClick = {
                    navController.navigate(Screen.AddIncome.route)
                },
                onItemClick = { id ->
                    navController.navigate(Screen.EditIncome.routeWithIdIncome(id))
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

        composable(
            Screen.EditExpense.route,
            arguments = listOf(navArgument("expenseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getInt("expenseId") ?: return@composable
            EditExpenseScreen(
                onNavigateBack = { navController.navigateUp() },
                expenseId = expenseId
            )
        }

        composable(
            Screen.EditIncome.route,
            arguments = listOf(navArgument("incomeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val incomeId = backStackEntry.arguments?.getInt("incomeId") ?: return@composable
            EditIncomeScreen(
                onNavigateBack = { navController.navigateUp() },
                incomeId = incomeId
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

        composable(Screen.AddIncome.route) {
            AddIncomeScreen(
                onNavigateBack = {
                    navController.navigate(Screen.Income.route) {
                        popUpTo(Screen.AddIncome.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.ExpenseAnalytics.route) {
            ExpenseAnalyticsScreen(
                onNavigateBack = {
                    navController.navigate(Screen.ExpensesHistory.route) {
                        popUpTo(Screen.ExpenseAnalytics.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.IncomeAnalytics.route) {
            com.example.feature_incomes.presentation.analytics.IncomeAnalyticsScreen(
                onNavigateBack = {
                    navController.navigate(Screen.IncomesHistory.route) {
                        popUpTo(Screen.IncomeAnalytics.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

    }
}

