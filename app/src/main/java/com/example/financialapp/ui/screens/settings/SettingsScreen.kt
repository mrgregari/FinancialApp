package com.example.financialapp.ui.screens.settings

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    
    val settings = listOf(
        R.string.dark_theme to true,
        R.string.main_color to false,
        R.string.sounds to false,
        R.string.haptics to false,
        R.string.code_password to false,
        R.string.sync to false,
        R.string.language to false,
        R.string.about to false
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            settings.forEach { (titleRes, isSwitch) ->
                val title = stringResource(titleRes)
                if (isSwitch) {
                    ListItem(
                        headlineContent = { Text(title) },
                        trailingContent = {
                            Switch(
                                checked = isDarkTheme,
                                onCheckedChange = { isDarkTheme = it }
                            )
                        }
                    )
                } else {
                    CustomListItem(
                        title = title,
                        showArrow = true,
                        arrowIcon = painterResource(R.drawable.arrow_right)
                    )
                }
                HorizontalDivider()
            }
        }
    }
}