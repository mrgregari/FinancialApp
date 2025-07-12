package com.example.feature_account.presentation.accountEdit

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
import com.example.feature_account.di.DaggerAccountComponent
import com.example.feature_account.presentation.accountEdit.components.AccountEditContent
import com.example.feature_account.presentation.accountEdit.components.CurrencyBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditScreen(
    onNavigateBack: () -> Unit,
    accountId: Int
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val accountComponent = remember {
        DaggerAccountComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = accountComponent.viewModelFactory()

    val viewModel: AccountEditViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }
    var showCurrencySheet by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(accountId) { viewModel.loadAccount(accountId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.cancel),
                            contentDescription = "Закрыть",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text(stringResource(R.string.account_title)) },
                actions = {
                    val account = (uiState as? AccountEditUiState.Success)?.account
                    val isFormValid =
                        (uiState as? AccountEditUiState.Success)?.validationState?.isFormValid == true

                    IconButton(
                        onClick = {
                            viewModel.validateAllFields(name, balance, currency)
                            if (isFormValid && account != null) {
                                viewModel.updateAccount(account.id, name, balance, currency)
                            }
                        },
                        enabled = isFormValid && account != null
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.check_icon),
                            contentDescription = "Confirm",
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
            NetworkErrorBanner(isVisible = !isNetworkAvailable)

            when (uiState) {
                is AccountEditUiState.Loading -> LoadingScreen()
                is AccountEditUiState.Error -> {
                    val errorResId = (uiState as AccountEditUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.loadAccount(accountId) }
                    )
                }

                is AccountEditUiState.Success -> {
                    val account = (uiState as AccountEditUiState.Success).account
                    val validationState = (uiState as AccountEditUiState.Success).validationState

                    LaunchedEffect(account) {
                        if (!isInitialized) {
                            name = account.name
                            balance = account.balance
                            currency = account.currency
                            isInitialized = true
                            viewModel.validateAllFields(name, balance, currency)
                        }
                    }

                    AccountEditContent(
                        name = name,
                        onNameChange = { newName ->
                            name = newName
                            viewModel.validateField("name", newName)
                        },
                        nameError = validationState.nameError,
                        balance = balance,
                        onValueChange = { newBalance ->
                            balance = newBalance
                            viewModel.validateField("balance", newBalance)
                        },
                        balanceError = validationState.balanceError,
                        currency = currency,
                        onCurrencyClick = { showCurrencySheet = true },
                        currencyError = validationState.currencyError
                    )
                }

                is AccountEditUiState.Updated -> {
                    onNavigateBack()
                }
            }

            if (showCurrencySheet) {
                CurrencyBottomSheet(
                    onCurrencySelected = { selectedCurrency ->
                        currency = selectedCurrency
                        viewModel.validateField("currency", selectedCurrency)
                        showCurrencySheet = false
                    },
                    onDismiss = { showCurrencySheet = false }
                )
            }
        }
    }
}




