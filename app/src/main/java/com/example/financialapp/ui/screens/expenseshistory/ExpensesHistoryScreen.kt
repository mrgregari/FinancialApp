package com.example.financialapp.ui.screens.expenseshistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomDatePickerDialog
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.ErrorScreen
import com.example.financialapp.ui.components.LoadingScreen
import com.example.financialapp.ui.components.NetworkErrorBanner
import com.example.financialapp.ui.utils.formatAmountWithCurrency
import com.example.financialapp.ui.utils.formatDate
import com.example.financialapp.ui.utils.formatDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesHistoryScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController
) {
    val viewModel : ExpensesHistoryViewModel = viewModel(factory = viewModelFactory)
    val expenses by viewModel.expenses.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NetworkErrorBanner(
                isVisible = !isNetworkAvailable,
                onDismiss = { }
            )
            
            when {
                isLoading -> LoadingScreen()
                errorMessage != null -> ErrorScreen(
                    error = errorMessage!!,
                    onRetry = { viewModel.retry() }
                )
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        stickyHeader {
                            Column {
                                CustomListItem(
                                    modifier = Modifier.height(56.dp),
                                    title = "Начало",
                                    trailingText = startDate?.let { formatDate(it) } ?: "Выберите дату",
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    onClick = { showStartDatePicker = true }
                                )
                                HorizontalDivider()
                                CustomListItem(
                                    modifier = Modifier.height(56.dp),
                                    title = "Конец",
                                    trailingText = endDate?.let { formatDate(it) } ?: "Выберите дату",
                                    containerColor = MaterialTheme.colorScheme.secondary,
                                    onClick = { showEndDatePicker = true }
                                )
                                HorizontalDivider()
                                CustomListItem(
                                    modifier = Modifier.height(56.dp),
                                    title = "Сумма",
                                    trailingText = formatAmountWithCurrency(
                                        expenses.sumOf { it.amount.toDouble() },
                                        expenses.firstOrNull()?.currency ?: "₽"
                                    ),
                                    showArrow = false,
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        
                        items(expenses) { expense ->
                            CustomListItem(
                                modifier = Modifier.height(70.dp),
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
            }
        }
        
        CustomDatePickerDialog(
            showDialog = showStartDatePicker,
            onDismiss = { showStartDatePicker = false },
            onDateSelected = { viewModel.updateStartDate(it) }
        )
        
        CustomDatePickerDialog(
            showDialog = showEndDatePicker,
            onDismiss = { showEndDatePicker = false },
            onDateSelected = { viewModel.updateEndDate(it) }
        )
    }
} 