package com.example.financialapp.navigation

import com.example.core_ui.R


/**
 * Sealed class representing navigation screens in the app.
 * Defines route, title and icon for each screen.
 *
 * @param route Navigation route identifier
 * @param titleResId Resource ID for screen title
 * @param icon Resource ID for screen icon
 */
sealed class Screen(
    val route: String,
    val titleResId: Int,
    val icon: Int
) {
    object Expenses : Screen(
        route = "expenses",
        titleResId = R.string.expenses,
        icon = R.drawable.downtrend
    )

    object Income : Screen(
        route = "income",
        titleResId = R.string.incomes,
        icon = R.drawable.uptrend
    )


    object Account : Screen(
        route = "account",
        titleResId = R.string.accounts,
        icon = R.drawable.calculator
    )


    object Categories : Screen(
        route = "categories",
        titleResId = R.string.categories,
        icon = R.drawable.categories
    )

    object Settings : Screen(
        route = "settings",
        titleResId = R.string.settings,
        icon = R.drawable.settings
    )

    object ExpensesHistory : Screen(
        route = "expenses_history",
        titleResId = R.string.expenses_history_title,
        icon = R.drawable.history
    )

    object IncomesHistory : Screen(
        route = "incomes_history",
        titleResId = R.string.incomes_history_title,
        icon = R.drawable.history
    )

    object EditAccount : Screen(
        route = "edit_account/{accountId}",
        titleResId = R.string.edit_account,
        icon = R.drawable.edit
    ) {
        fun routeWithIdAccount(accountId: Int) = "edit_account/$accountId"
    }

    object AddExpense : Screen(
        route = "add_expense",
        titleResId = R.string.add_expense,
        icon = R.drawable.edit
    )

    object AddIncome : Screen(
        route = "add_expense",
        titleResId = R.string.add_income,
        icon = R.drawable.edit
    )

    object EditExpense : Screen (
        route = "edit_expense/{expenseId}",
        titleResId = R.string.expenses,
        icon = R.drawable.edit
    ) {
        fun routeWithIdExpense(expenseId: Int) = "edit_expense/$expenseId"
    }
}