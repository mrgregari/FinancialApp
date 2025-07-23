package com.example.feature_account.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.charts.BarChart
import com.example.charts.BarChartEntity
import com.example.core_data.di.DataComponentProvider
import com.example.core_domain.models.Account
import com.example.core_ui.components.CustomListItem
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.core_ui.utils.formatNumber
import com.example.core_ui.utils.getCurrencySymbol
import com.example.core_ui.R
import com.example.feature_account.alignBarChartEntities
import com.example.feature_account.di.DaggerAccountComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    onEditAccount: (Int) -> Unit
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val accountComponent = remember {
        DaggerAccountComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = accountComponent.viewModelFactory()
    val viewModel: AccountViewModel = viewModel(factory = viewModelFactory)

    val accounts by viewModel.accounts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorResId by viewModel.errorResId.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val incomes by viewModel.incomes.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

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
                        account?.let { onEditAccount(it.id) }
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

                else -> {
                    val incomes =
                        incomes.map { BarChartEntity(it.date, it.amount.toDouble()) }
                    val expenses =
                        expenses.map { BarChartEntity(it.date, it.amount.toDouble()) }
                    val (alignedIncomes, alignedExpenses) = alignBarChartEntities(incomes, expenses)
                    AccountContent(
                        accounts = accounts,
                        modifier = Modifier.fillMaxSize(),
                        alignedIncomes,
                        alignedExpenses
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountContent(
    accounts: List<Account>,
    modifier: Modifier = Modifier,
    incomes: List<BarChartEntity>,
    expenses: List<BarChartEntity>
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
            Spacer(modifier = Modifier.height(30.dp))
            BarChart(
                incomes = incomes,
                expenses = expenses,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
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