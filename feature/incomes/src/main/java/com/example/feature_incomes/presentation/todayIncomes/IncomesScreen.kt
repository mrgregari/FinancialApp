package com.example.feature_incomes.presentation.todayIncomes

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
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
import com.example.feature_incomes.di.DaggerIncomesComponent


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun IncomeScreen(
    onHistoryClick: () -> Unit,
    onAddClick: () -> Unit,
    onItemClick: (Int) -> Unit
) {

    val app = LocalContext.current.applicationContext as DataComponentProvider
    val incomesComponent = remember {
        DaggerIncomesComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = incomesComponent.viewModelFactory()

    val viewModel: IncomesViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.retry()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.today_incomes)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                actions = {
                    IconButton(onClick = { onHistoryClick() }) {
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
            CustomFab(onClick = {
                onAddClick()
            })
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
                is IncomesUiState.Loading -> LoadingScreen()
                is IncomesUiState.Error -> {
                    val errorResId = (uiState as IncomesUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }
                is IncomesUiState.Success -> {
                    val state = uiState as IncomesUiState.Success
                    IncomesContent(
                        incomes = state.incomes,
                        modifier = Modifier.fillMaxSize(),
                        currency = state.currency,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}