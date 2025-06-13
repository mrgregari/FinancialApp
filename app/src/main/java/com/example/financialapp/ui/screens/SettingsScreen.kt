package com.example.financialapp.ui.screens

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
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }
    
    val settings = listOf(
        "Темная тема" to true,
        "Основной цвет" to false,
        "Звуки" to false,
        "Хаптики" to false,
        "Код пароль" to false,
        "Синхронизация" to false,
        "Язык" to false,
        "О программе" to false
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройки") },
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
            settings.forEach { (title, isSwitch) ->
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