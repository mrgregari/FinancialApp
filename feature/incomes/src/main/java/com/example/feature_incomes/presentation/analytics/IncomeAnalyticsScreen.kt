package com.example.feature_incomes.presentation.analytics

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
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.feature_incomes.di.DaggerIncomesComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeAnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val incomesComponent = remember {
        DaggerIncomesComponent.factory()
            .create(app.dataComponent)
    }
    val viewModelFactory = incomesComponent.viewModelFactory()

    val viewModel: IncomeAnalyticsViewModel = viewModel(factory = viewModelFactory)
    val uiState by viewModel.uiState.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Анализ доходов") },
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
                is IncomeAnalyticsUiState.Loading -> LoadingScreen()
                is IncomeAnalyticsUiState.Error -> {
                    val errorResId = (uiState as IncomeAnalyticsUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }

                is IncomeAnalyticsUiState.Success -> {
                    val state = uiState as IncomeAnalyticsUiState.Success
                    IncomeAnalyticsContent(
                        incomes = state.incomes,
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