package com.example.financialapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.example.core_data.sync.SyncReceiver
import com.example.core_ui.theme.FinancialAppTheme
import com.example.core_ui.theme.getSecondaryForPrimary
import com.example.financialapp.navigation.AppNavGraph
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.Calendar
import com.example.financialapp.LocaleHelper

/**
 * Main activity of the application.
 * Sets up Compose UI and navigation.
 */
class MainActivity : ComponentActivity() {

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("app_lang", "ru") ?: "ru"
        super.attachBaseContext(LocaleHelper.wrap(newBase, lang))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = applicationContext
            val prefs = context.getSharedPreferences("sync_prefs", MODE_PRIVATE)
            val isDarkTheme = prefs.getBoolean("dark_theme", false)
            val mainColorInt = prefs.getInt("main_color", 0xFF2AE881.toInt())
            val mainColor = Color(mainColorInt)
            val secondaryColor = getSecondaryForPrimary(mainColor)

            val systemUiController = rememberSystemUiController()
            SideEffect {
                systemUiController.setStatusBarColor(
                    color = mainColor,
                    darkIcons = !isDarkTheme
                )
            }

            FinancialAppTheme(
                darkTheme = isDarkTheme,
                mainColor = mainColor,
                secondaryColor = secondaryColor,
                dynamicColor = false
            ) {
                AppNavGraph()
            }
        }
        setAlarm()
    }

    private fun setAlarm() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = SyncReceiver.newIntent(this)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.SECOND, 3)
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        val prefs = getSharedPreferences("sync_prefs", MODE_PRIVATE)
        val intervalMinutes = prefs.getInt("sync_interval_minutes", 20)
        val intervalMillis = intervalMinutes * 60 * 1000L

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            intervalMillis,
            pendingIntent
        )
    }
}
