package com.example.feature_expenses.presentation.analytics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core_data.di.DataComponentProvider
import com.example.core_ui.R
import com.example.core_ui.components.CustomFab
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.feature_expenses.di.DaggerExpensesComponent
import com.example.feature_expenses.presentation.expensesHistory.ExpensesHistoryUiState
import com.example.feature_expenses.presentation.todayExpenses.ExpensesContent
import com.example.feature_expenses.presentation.todayExpenses.ExpensesUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseAnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val expensesComponent = remember {
        DaggerExpensesComponent.factory()
            .create(app.dataComponent)
    }
    val viewModelFactory = expensesComponent.viewModelFactory()

    val viewModel: ExpenseAnalyticsViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.analytics)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.leading_icon),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
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

            when (uiState){
                is ExpensesAnalyticsUiState.Loading -> LoadingScreen()
                is ExpensesAnalyticsUiState.Error -> {
                    val errorResId = (uiState as ExpensesAnalyticsUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }

                is ExpensesAnalyticsUiState.Success -> {
                    val state = uiState as ExpensesAnalyticsUiState.Success
                    ExpenseAnalyticsContent(
                        expenses = state.expenses,
                        startDate = state.startDate,
                        endDate = state.endDate,
                        currency = state.currency,
                        total = state.total,
                        showStartDatePicker = showStartDatePicker,
                        showEndDatePicker = showEndDatePicker,
                        onShowStartDatePicker = { showStartDatePicker = true },
                        onShowEndDatePicker = { showEndDatePicker = true },
                        onDismissStartDatePicker = { showStartDatePicker = false },
                        onDismissEndDatePicker = { showEndDatePicker = false },
                        onStartDateSelected = { viewModel.updateStartDate(it) },
                        onEndDateSelected = { viewModel.updateEndDate(it) }
                    )
                }
            }

        }
    }
}