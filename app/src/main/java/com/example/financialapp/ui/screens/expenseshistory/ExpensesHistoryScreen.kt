package com.example.financialapp.ui.screens.expenseshistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomDatePickerDialog
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.utils.formatAmountWithCurrency
import com.example.financialapp.ui.utils.formatDate
import com.example.financialapp.ui.utils.formatDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesHistoryScreen(
    viewModel: ExpensesHistoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Моя история") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            painter = painterResource(R.drawable.leading_icon),
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(R.drawable.analytics),
                            contentDescription = "Analytics",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is ExpensesHistoryUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
            is ExpensesHistoryUiState.Success -> {
                val list = (uiState as ExpensesHistoryUiState.Success).expenses
                val startDate = (uiState as ExpensesHistoryUiState.Success).startDate
                val endDate = (uiState as ExpensesHistoryUiState.Success).endDate
                
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    stickyHeader {
                        CustomListItem(
                            modifier = Modifier
                                .height(56.dp),
                            title = "Начало",
                            trailingText = formatDate(startDate),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            onClick = { showStartDatePicker = true }
                        )
                        HorizontalDivider()
                        CustomListItem(
                            modifier = Modifier
                                .height(56.dp),
                            title = "Конец",
                            trailingText = formatDate(endDate),
                            containerColor = MaterialTheme.colorScheme.secondary,
                            onClick = { showEndDatePicker = true }
                        )
                        HorizontalDivider()
                        CustomListItem(
                            modifier = Modifier
                                .height(56.dp),
                            title = "Сумма",
                            subTitle = null,
                            trailingText = formatAmountWithCurrency(
                                list.sumOf { it.amount.toDouble() },
                                list.firstOrNull()?.currency ?: "₽"
                            ),
                            subTrailingText = null,
                            showArrow = false,
                            containerColor = MaterialTheme.colorScheme.secondary
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
                            subTrailingText = formatDateTime(expense.date),
                            showArrow = true,
                        )
                        HorizontalDivider()
                    }
                }
            }
            is ExpensesHistoryUiState.Error -> {
                val error = (uiState as ExpensesHistoryUiState.Error).throwable
                Text(
                    text = "Ошибка: ${error.localizedMessage ?: "Неизвестная ошибка"}",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        // DatePicker для начальной даты
        CustomDatePickerDialog(
            showDialog = showStartDatePicker,
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { viewModel.updateStartDate(it) }
        )
        
        // DatePicker для конечной даты
        CustomDatePickerDialog(
            showDialog = showEndDatePicker,
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { viewModel.updateEndDate(it) }
        )
    }
} 