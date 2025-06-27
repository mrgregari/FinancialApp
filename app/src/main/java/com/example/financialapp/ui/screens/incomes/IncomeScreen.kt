package com.example.financialapp.ui.screens.incomes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.domain.models.Income
import com.example.financialapp.ui.components.*
import com.example.financialapp.ui.navigation.Screen
import com.example.financialapp.ui.utils.formatAmountWithCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun IncomeScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {
    val viewModel: IncomesViewModel = viewModel(factory = viewModelFactory)
    val incomes by viewModel.incomes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorResId by viewModel.errorResId.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.today_incomes)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.IncomesHistory.route) }) {
                        Icon(
                            painter = painterResource(R.drawable.history),
                            contentDescription = stringResource(R.string.history),
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NetworkErrorBanner(
                isVisible = !isNetworkAvailable
            )

            when {
                isLoading -> LoadingScreen()
                errorResId != null ->
                    ErrorScreen(
                        error = stringResource(errorResId!!),
                        onRetry = { viewModel.retry() }
                    )

                else ->
                    IncomesContent(
                        incomes = incomes,
                        modifier = Modifier.fillMaxSize()
                    )
            }
        }
    }
}

@Composable
private fun IncomesContent(
    incomes: List<Income>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.total),
                subTitle = null,
                trailingText = formatAmountWithCurrency(
                    incomes.sumOf { it.amount.toDouble() },
                    incomes.firstOrNull()?.currency ?: "RUB"
                ),
                subTrailingText = null,
                showArrow = false,
                containerColor = MaterialTheme.colorScheme.secondary
            )
            HorizontalDivider()
        }

        items(incomes) { income ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                title = income.title,
                emoji = income.icon,
                subTitle = income.comment,
                trailingText = formatAmountWithCurrency(income.amount.toDouble(), income.currency),
                subTrailingText = null,
                showArrow = true,
            )
            HorizontalDivider()
        }
    }
}