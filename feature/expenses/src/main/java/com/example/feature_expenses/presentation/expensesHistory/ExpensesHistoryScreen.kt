package com.example.feature_expenses.presentation.expensesHistory

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.feature_expenses.di.DaggerExpensesComponent


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ExpensesHistoryScreen(
    onNavigateUp: () -> Unit,
    onItemClick: (Int) -> Unit,
    onAnalyticsClick: () -> Unit
) {

    val app = LocalContext.current.applicationContext as DataComponentProvider
    val expensesComponent = remember {
        DaggerExpensesComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = expensesComponent.viewModelFactory()

    val viewModel : ExpensesHistoryViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.retry()
    }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.expenses_history_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigateUp() }) {
                        Icon(
                            painter = painterResource(R.drawable.leading_icon),
                            contentDescription = stringResource(R.string.back),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAnalyticsClick() }) {
                        Icon(
                            painter = painterResource(R.drawable.analytics),
                            contentDescription = stringResource(R.string.analytics),
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

            when (uiState) {
                is ExpensesHistoryUiState.Loading -> LoadingScreen()
                is ExpensesHistoryUiState.Error -> {
                    val errorResId = (uiState as ExpensesHistoryUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }
                is ExpensesHistoryUiState.Success -> {
                    val state = uiState as ExpensesHistoryUiState.Success
                    ExpensesHistoryContent(
                        expenses = state.expenses,
                        startDate = state.startDate,
                        endDate = state.endDate,
                        currency = state.currency,
                        showStartDatePicker = showStartDatePicker,
                        showEndDatePicker = showEndDatePicker,
                        onShowStartDatePicker = { showStartDatePicker = true },
                        onShowEndDatePicker = { showEndDatePicker = true },
                        onDismissStartDatePicker = { showStartDatePicker = false },
                        onDismissEndDatePicker = { showEndDatePicker = false },
                        onStartDateSelected = { viewModel.updateStartDate(it) },
                        onEndDateSelected = { viewModel.updateEndDate(it) },
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

