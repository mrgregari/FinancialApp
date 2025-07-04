package com.example.financialapp.ui.screens.accountEdit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.financialapp.R
import com.example.financialapp.ui.components.CustomListItem
import com.example.financialapp.ui.components.ErrorScreen
import com.example.financialapp.ui.components.LoadingScreen
import com.example.financialapp.ui.components.NetworkErrorBanner
import com.example.financialapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEditScreen(
    viewModelFactory: ViewModelProvider.Factory,
    navController: NavController,
    accountId: Int
) {
    val viewModel: AccountEditViewModel = viewModel(factory = viewModelFactory)
    println("DEBUG: viewModel instance = $viewModel")
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }

    var balance by remember { mutableStateOf("") }
    var balanceError by remember { mutableStateOf<String?>(null) }

    var currency by remember { mutableStateOf("") }
    var currencyError by remember { mutableStateOf<String?>(null) }
    var showCurrencySheet by remember { mutableStateOf(false) }
    var isInitialized by remember { mutableStateOf(false) }


    LaunchedEffect(accountId) { viewModel.loadAccount(accountId) }


    // Валидация
    fun validateAll() {
        nameError = if (name.isBlank()) "Имя не может быть пустым" else null
        balanceError = if (balance.isBlank()) "Баланс не может быть пустым"
        else if (balance.toDoubleOrNull() == null) "Введите корректное число"
        else null
        currencyError = if (currency.isBlank()) "Выберите валюту" else null
    }

    val isFormValid = nameError == null && balanceError == null && currencyError == null
            && name.isNotBlank() && balance.isNotBlank() && currency.isNotBlank()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                title = { Text(stringResource(R.string.account_title)) },
                actions = {
                    val account = (uiState as? AccountEditUiState.Success)?.account
                    IconButton(
                        onClick = {
                            validateAll()
                            val isValid = nameError == null && balanceError == null && currencyError == null
                                    && name.isNotBlank() && balance.isNotBlank() && currency.isNotBlank()
                            if (isValid && account != null) {
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
                    LaunchedEffect(account) {
                        if (!isInitialized) {
                            name = account.name
                            balance = account.balance
                            currency = account.currency
                            isInitialized = true
                        }
                    }
                    AccountEditContent(
                        name = name,
                        onNameChange = {
                            name = it
                            nameError = if (it.isBlank()) "Имя не может быть пустым" else null
                        },
                        nameError = nameError,
                        balance = balance,
                        onBalanceChange = {
                            balance = it
                            balanceError = if (it.isBlank()) "Баланс не может быть пустым"
                            else if (it.toDoubleOrNull() == null) "Введите корректное число"
                            else null
                        },
                        balanceError = balanceError,
                        currency = currency,
                        onCurrencyClick = { showCurrencySheet = true },
                        currencyError = currencyError
                    )
                }

                is AccountEditUiState.Updated -> {
                    navController.navigate(Screen.Account.route) {
                        popUpTo(Screen.EditAccount.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            if (showCurrencySheet) {
                CurrencyBottomSheet(
                    onCurrencySelected = {
                        currency = it
                        currencyError = null
                    },
                    onDismiss = { showCurrencySheet = false }
                )
            }
        }
    }
}

@Composable
fun AccountEditContent(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String?,
    balance: String,
    onBalanceChange: (String) -> Unit,
    balanceError: String?,
    currency: String,
    onCurrencyClick: () -> Unit,
    currencyError: String?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Название счёта") },
            isError = nameError != null,
            supportingText = { if (nameError != null) Text(nameError) },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = balance,
            onValueChange = onBalanceChange,
            label = { Text("Баланс") },
            isError = balanceError != null,
            supportingText = { if (balanceError != null) Text(balanceError) },
            modifier = Modifier.padding(bottom = 8.dp)
        )
        CustomListItem(
            title = "Валюта",
            trailingText = currency,
            showArrow = true,
            onClick = onCurrencyClick
        )
        if (currencyError != null) {
            Text(
                text = currencyError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyBottomSheet(
    onCurrencySelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currencies = listOf("RUB", "USD", "EUR")
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        currencies.forEach { currency ->
            ListItem(
                headlineContent = { Text(currency) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onCurrencySelected(currency)
                        onDismiss()
                    }
            )
        }
    }
}