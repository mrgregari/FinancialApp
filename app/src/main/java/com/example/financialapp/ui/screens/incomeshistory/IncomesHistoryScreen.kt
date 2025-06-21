package com.example.financialapp.ui.screens.incomeshistory

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.ui.components.*
import com.example.financialapp.ui.utils.formatAmountWithCurrency
import com.example.financialapp.ui.utils.formatDate
import com.example.financialapp.ui.utils.formatDateTime
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun IncomesHistoryScreen(
    viewModel: IncomesHistoryViewModel = hiltViewModel(),
    navController: NavController
) {
    val incomes by viewModel.incomes.collectAsState()
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
                title = { Text("История доходов") },
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
                                        incomes.sumOf { it.amount.toDouble() },
                                        incomes.firstOrNull()?.currency ?: "₽"
                                    ),
                                    showArrow = false,
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        
                        items(incomes) { income ->
                            CustomListItem(
                                modifier = Modifier.height(70.dp),
                                emoji = income.icon,
                                title = income.title,
                                subTitle = income.comment,
                                trailingText = formatAmountWithCurrency(income.amount.toDouble(), income.currency),
                                subTrailingText = formatDateTime(income.date),
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