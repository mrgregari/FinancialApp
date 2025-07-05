package com.example.financialapp.ui.screens.account

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.domain.models.Account
import com.example.financialapp.ui.components.*
import com.example.financialapp.ui.navigation.Screen
import com.example.financialapp.ui.utils.formatNumber
import com.example.financialapp.ui.utils.getCurrencySymbol

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {

    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    val accounts by viewModel.accounts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorResId by viewModel.errorResId.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.account_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = {
                        val account = accounts.firstOrNull()
                        account?.let {
                            navController.popBackStack(Screen.Account.route, inclusive = true)
                            navController.navigate(Screen.EditAccount.routeWithIdAccount(it.id))
                        }
                    }) {
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
                isVisible = !isNetworkAvailable
            )

            when {
                isLoading -> LoadingScreen()
                errorResId != null -> ErrorScreen(
                    error = stringResource(errorResId!!),
                    onRetry = { viewModel.retry() }
                )

                else -> AccountContent(
                    accounts = accounts,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun AccountContent(
    accounts: List<Account>,
    modifier: Modifier = Modifier
) {
    val account = accounts.firstOrNull()

    if (account != null) {
        Column(modifier = modifier) {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.account_name),
                trailingText = account.name,
                containerColor = MaterialTheme.colorScheme.secondary,
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(56.dp),
                emoji = "\uD83D\uDCB0",
                emojiBackgroundColor = Color.White,
                title = stringResource(R.string.balance),
                trailingText = "${formatNumber(account.balance)} ${getCurrencySymbol(account.currency)}",
                containerColor = MaterialTheme.colorScheme.secondary,
            )
            HorizontalDivider()
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.currency),
                trailingText = getCurrencySymbol(account.currency),
                containerColor = MaterialTheme.colorScheme.secondary,
            )
        }
    } else {
        Column(modifier = modifier) {
            Text(
                text = stringResource(R.string.no_account),
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}