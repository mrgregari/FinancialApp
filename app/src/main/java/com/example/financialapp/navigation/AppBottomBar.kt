package com.example.financialapp.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController

private val bottomBarScreens = listOf(
    Screen.Expenses,
    Screen.Income,
    Screen.Account,
    Screen.Categories,
    Screen.Settings
)

@Composable
fun AppBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        bottomBarScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = { Icon(painterResource(screen.icon), null) },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.secondary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }
    }
}
