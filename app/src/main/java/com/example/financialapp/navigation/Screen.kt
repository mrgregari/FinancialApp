package com.example.financialapp.navigation

import com.example.financialapp.R

sealed class Screen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Expenses : Screen(
        route = "expenses",
        title = "Расходы",
        icon = R.drawable.downtrend
    )

    object Income : Screen(
        route = "income",
        title = "Доходы",
        icon = R.drawable.uptrend
    )

    object Account : Screen(
        route = "account",
        title = "Счета",
        icon = R.drawable.calculator
    )

    object Categories : Screen(
        route = "categories",
        title = "Статьи",
        icon = R.drawable.categories
    )

    object Settings : Screen(
        route = "settings",
        title = "Настройки",
        icon = R.drawable.settings
    )
}