package com.example.financialapp.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.financialapp.R
import com.example.financialapp.ui.components.*
import com.example.financialapp.ui.utils.formatNumber
import com.example.financialapp.ui.utils.getCurrencySymbol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModel: AccountViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NetworkErrorBanner(
                isVisible = !isNetworkAvailable,
                onDismiss = {  }
            )

            if (isLoading) {
                LoadingScreen()
            } else if (errorMessage != null) {
                ErrorScreen(
                    error = errorMessage!!,
                    onRetry = { viewModel.retry() }
                )
            } else {
                AccountContent(
                    accounts = accounts,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AccountContent(
    accounts: List<com.example.financialapp.domain.models.Account>,
    modifier: Modifier = Modifier
) {
    val account = accounts.firstOrNull()
    
    if (account != null) {
        Column(modifier = modifier) {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                emoji = "\uD83D\uDCB0",
                emojiBackgroundColor = Color.White,
                title = "Баланс",
                trailingText = "${formatNumber(account.balance)} ${getCurrencySymbol(account.currency)}",
                containerColor = MaterialTheme.colorScheme.secondary,
                showArrow = true
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = "Валюта",
                trailingText = getCurrencySymbol(account.currency),
                containerColor = MaterialTheme.colorScheme.secondary,
                showArrow = true
            )
        }
    } else {
        Column(modifier = modifier) {
            Text(
                text = "Нет доступного счёта",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}