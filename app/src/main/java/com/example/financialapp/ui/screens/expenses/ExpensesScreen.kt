package com.example.financialapp.ui.screens.expenses


import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.financialapp.R
import com.example.financialapp.domain.models.Expense
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.theme.FinancialAppTheme
import com.example.financialapp.ui.utils.formatNumber
import com.example.financialapp.ui.components.CustomFab
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import com.example.financialapp.ui.screens.expenses.ExpensesViewModel
import com.example.financialapp.ui.screens.expenses.ExpensesUiState
import com.example.financialapp.ui.utils.getCurrencySymbol
import java.util.Calendar
import com.example.financialapp.ui.utils.formatAmountWithCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Расходы сегодня") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { }) {
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
        when (uiState) {
            is ExpensesUiState.Loading -> {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is ExpensesUiState.Success -> {
                val list = (uiState as ExpensesUiState.Success).expenses
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    stickyHeader {
                        CustomListItem(
                            modifier = Modifier
                                .height(56.dp),
                            title = "Всего:",
                            subTitle = null,
                            trailingText = formatAmountWithCurrency(
                                list.sumOf { it.amount.toDouble() },
                                list.firstOrNull()?.currency ?: "RUB"
                            ),
                            subTrailingText = null,
                            showArrow = false,
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                    items(list) { expense ->
                        CustomListItem(
                            modifier = Modifier
                                .height(70.dp),
                            emoji = expense.icon,
                            title = expense.title,
                            subTitle = expense.comment,
                            trailingText = formatAmountWithCurrency(expense.amount.toDouble(), expense.currency),
                            showArrow = true,
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(),
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
            is ExpensesUiState.Error -> {
                val error = (uiState as ExpensesUiState.Error).throwable
                Text(
                    text = "Ошибка: ${error.localizedMessage ?: "Неизвестная ошибка"}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Preview
@Composable
fun ExpensesScreenPreview() {
    FinancialAppTheme(dynamicColor = false) {
        ExpensesScreen()
    }

}