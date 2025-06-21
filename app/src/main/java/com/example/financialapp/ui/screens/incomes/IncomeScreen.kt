package com.example.financialapp.ui.screens.incomes

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.CustomFab
import com.example.financialapp.ui.navigation.Screen
import com.example.financialapp.ui.utils.formatAmountWithCurrency

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun IncomeScreen(
    viewModel: IncomesViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Доходы сегодня") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.IncomesHistory.route) }) {
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
            is IncomesUiState.Loading -> {
                androidx.compose.material3.CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is IncomesUiState.Success -> {
                val list = (uiState as IncomesUiState.Success).expenses
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    stickyHeader {
                        CustomListItem(
                            modifier = Modifier
                                .height(56.dp),
                            title = "Всего",
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
                    items(list) { income ->
                        CustomListItem(
                            modifier = Modifier
                                .height(70.dp),
                            title = income.title,
                            emoji = income.icon,
                            subTitle = income.comment,
                            trailingText = formatAmountWithCurrency(income.amount.toDouble(), income.currency),
                            subTrailingText = null,
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
            is IncomesUiState.Error -> {
                val error = (uiState as IncomesUiState.Error).throwable
                Text(
                    text = "Ошибка: ${error.localizedMessage ?: "Неизвестная ошибка"}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}