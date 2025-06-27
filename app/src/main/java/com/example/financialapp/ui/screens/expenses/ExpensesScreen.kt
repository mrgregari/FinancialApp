package com.example.financialapp.ui.screens.expenses


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.ui.components.CustomFab
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.ErrorScreen
import com.example.financialapp.ui.components.LoadingScreen
import com.example.financialapp.ui.components.NetworkErrorBanner
import com.example.financialapp.ui.navigation.Screen
import com.example.financialapp.ui.utils.formatAmountWithCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {

    val viewModel: ExpensesViewModel = viewModel(factory = viewModelFactory)
    val expenses by viewModel.expenses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorResId by viewModel.errorResId.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.expenses_today)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.ExpensesHistory.route)
                    }) {
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
                    ExpensesContent(
                        expenses = expenses,
                        modifier = Modifier.fillMaxSize()
                    )
            }

        }
    }
}

@Composable
private fun ExpensesContent(
    expenses: List<Expense>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            CustomListItem(
                modifier = Modifier.height(56.dp),
                title = stringResource(R.string.total),
                subTitle = null,
                trailingText = formatAmountWithCurrency(
                    expenses.sumOf { it.amount.toDouble() },
                    expenses.firstOrNull()?.currency ?: "RUB"
                ),
                subTrailingText = null,
                showArrow = false,
                containerColor = MaterialTheme.colorScheme.secondary
            )
            HorizontalDivider()
        }

        items(expenses) { expense ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = expense.icon,
                title = expense.title,
                subTitle = expense.comment,
                trailingText = formatAmountWithCurrency(
                    expense.amount.toDouble(),
                    expense.currency
                ),
                showArrow = true,
            )
            HorizontalDivider()
        }
    }
}
