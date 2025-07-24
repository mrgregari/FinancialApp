package com.example.feature_settings.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.core_data.di.DataComponentProvider
import com.example.core_ui.R
import com.example.core_ui.components.CustomListItem
import com.example.feature_settings.di.DaggerSettingsComponent
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val context = LocalContext.current
    val settingsComponent = remember {
        DaggerSettingsComponent.factory().create(app.dataComponent)
    }
    val viewModelFactory = settingsComponent.viewModelFactory()
    val viewModel: SettingsViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel(factory = viewModelFactory)
    val lastSyncText by viewModel.lastSyncText.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    val settings = listOf(
        R.string.dark_theme to true,
        R.string.main_color to false,
        R.string.haptics to false,
        R.string.sync to false,
        R.string.language to false,
        R.string.about to false
    )

    var pendingRestart by remember { mutableStateOf(false) }
    var showColorSheet by remember { mutableStateOf(false) }
    val colorOptions = listOf(
        Color(0xFF2AE881),
        Color(0xFFE46962),
        Color(0xFF62A3E4),
        Color(0xFFF39C12),
        Color(0xFF8E44AD)
    )

    val prefs = context.getSharedPreferences("sync_prefs", Context.MODE_PRIVATE)
    var showHapticSheet by remember { mutableStateOf(false) }
    val hapticOptions = listOf(
        stringResource(R.string.off),
        stringResource(R.string.short_hapt),
        stringResource(R.string.mediun),
        stringResource(R.string.long_hapt)
    )
    val selectedHaptic = prefs.getInt("haptic_mode", 0)

    var showSyncSheet by remember { mutableStateOf(false) }
    val currentInterval = prefs.getInt("sync_interval_minutes", 20)
    var sliderValue by remember { mutableStateOf(currentInterval.toFloat()) }

    var showLanguageSheet by remember { mutableStateOf(false) }
    val currentLang = prefs.getString("app_lang", "ru") ?: "ru"

    var showAboutSheet by remember { mutableStateOf(false) }

    fun restartApp(activity: Activity) {
        val intent = activity.packageManager.getLaunchIntentForPackage(activity.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
        activity.finish()
        Runtime.getRuntime().exit(0)
    }



    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.last_sync, lastSyncText),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
            HorizontalDivider()
            settings.forEach { (titleRes, isSwitch) ->
                val title = stringResource(titleRes)
                if (isSwitch) {
                    ListItem(headlineContent = { Text(title) }, trailingContent = {
                        Switch(
                            checked = isDarkTheme, onCheckedChange = {
                                viewModel.setDarkTheme(it)
                                pendingRestart = true
                            })
                    })
                } else if (titleRes == R.string.main_color) {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right),
                        onClick = { showColorSheet = true })
                } else if (titleRes == R.string.haptics) {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right),
                        onClick = { showHapticSheet = true })
                } else if (titleRes == R.string.sync) {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right),
                        onClick = { showSyncSheet = true })
                } else if (titleRes == R.string.language) {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right),
                        onClick = { showLanguageSheet = true })
                } else {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right),
                        onClick = {
                            if (titleRes == R.string.about) showAboutSheet = true
                        })
                }
                HorizontalDivider()
            }
        }
        if (showColorSheet) {
            ColorSheet(
                showColorSheet = showColorSheet,
                onDismiss = { showColorSheet = false },
                colorOptions = colorOptions,
                prefs = prefs,
                onColorSelected = { pendingRestart = true })
        }
        if (showHapticSheet) {
            HapticSheet(
                showHapticSheet = showHapticSheet,
                onDismiss = { showHapticSheet = false },
                hapticOptions = hapticOptions,
                selectedHaptic = selectedHaptic,
                prefs = prefs
            )
        }
        if (showSyncSheet) {
            SyncSheet(
                showSyncSheet = showSyncSheet,
                onDismiss = { showSyncSheet = false },
                sliderValue = sliderValue,
                onSliderValueChange = { sliderValue = it },
                prefs = prefs
            )
        }
        if (showLanguageSheet) {
            LanguageSheet(
                showLanguageSheet = showLanguageSheet,
                onDismiss = { showLanguageSheet = false },
                currentLang = currentLang,
                prefs = prefs,
                onLangSelected = { pendingRestart = true })
        }
        if (showAboutSheet) {
            AboutSheet(
                showAboutSheet = showAboutSheet,
                onDismiss = { showAboutSheet = false },
                context = context
            )
        }
    }
    if (pendingRestart) {
        LaunchedEffect(Unit) {
            delay(200)
            restartApp(context as Activity)
        }
    }
}