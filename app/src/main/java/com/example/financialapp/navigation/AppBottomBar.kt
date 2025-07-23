package com.example.financialapp.navigation

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf

private val bottomBarScreens = listOf(
    Screen.Expenses,
    Screen.Income,
    Screen.Account,
    Screen.Categories,
    Screen.Settings
)

@Composable
fun AppBottomBar(navController: NavHostController, currentRoute: String?) {
    val context = LocalContext.current
    var hapticMode by remember {
        mutableStateOf(
            context.getSharedPreferences(
                "sync_prefs",
                Context.MODE_PRIVATE
            ).getInt("haptic_mode", 0)
        )
    }
    DisposableEffect(Unit) {
        val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == "haptic_mode") {
                hapticMode = prefs.getInt("haptic_mode", 0)
            }
        }
        prefs.registerOnSharedPreferenceChangeListener(listener)
        onDispose {
            prefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
        bottomBarScreens.forEach { screen ->
            val selected = currentRoute == screen.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.titleResId),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        maxLines = 1
                    )
                },
                selected = selected,
                onClick = {
                    when (hapticMode) {
                        1 -> vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                20,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )

                        2 -> vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                50,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )

                        3 -> vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                100,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    }
                    navController.navigate(screen.route) {
                        popUpTo(screen.route) { inclusive = true }
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
