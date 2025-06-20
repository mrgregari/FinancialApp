package com.example.financialapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.financialapp.R
import com.example.financialapp.domain.models.Account
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.CustomFab
import com.example.financialapp.ui.utils.formatNumber
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen() {

    val account =
        Account(
            name = "Основной счет",
            balance = 650000,
            currency = "$"
        )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мой счет") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),

                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.edit),
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            CustomFab(onClick = { })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                emoji = "\uD83D\uDCB0",
                emojiBackgroundColor = Color.White,
                title = "Баланс",
                trailingText = "${formatNumber(account.balance)} ${account.currency}",
                containerColor = MaterialTheme.colorScheme.secondary,
                showArrow = true
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = "Валюта",
                trailingText = account.currency,
                containerColor = MaterialTheme.colorScheme.secondary,
                showArrow = true
            )
        }
    }
}