package com.example.financialapp.ui.screens.expenses


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.ui.components.*
import com.example.financialapp.ui.navigation.Screen
import com.example.financialapp.ui.theme.FinancialAppTheme
import com.example.financialapp.ui.utils.formatAmountWithCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel(),
    navController: NavController
) {
    val expenses by viewModel.expenses.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Расходы сегодня") },
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
                            contentDescription = "History",
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
                title = "Всего",
                subTitle = null,
                trailingText = formatAmountWithCurrency(
                    expenses.sumOf { it.amount.toDouble() },
                    expenses.firstOrNull()?.currency ?: "RUB"
                ),
                subTrailingText = null,
                showArrow = false,
                containerColor = MaterialTheme.colorScheme.secondary
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
        
        items(expenses) { expense ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = expense.icon,
                title = expense.title,
                subTitle = expense.comment,
                trailingText = formatAmountWithCurrency(expense.amount.toDouble(), expense.currency),
                showArrow = true,
            )
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}
