package com.example.feature_incomes.presentation.editIncome

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core_data.di.DataComponentProvider
import com.example.core_domain.models.Category
import com.example.core_ui.R
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.core_ui.utils.parseIso8601Date
import com.example.core_ui.utils.parseIso8601LocalDate
import com.example.feature_incomes.di.DaggerIncomesComponent
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditIncomeScreen(
    incomeId: Int,
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val incomesComponent = remember {
        DaggerIncomesComponent.factory()
            .create(app.dataComponent)
    }
    val viewModelFactory = incomesComponent.viewModelFactory()
    val viewModel: EditIncomeViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var value by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by rememberSaveable { mutableStateOf(Date()) }
    var time by rememberSaveable { mutableStateOf(Date()) }
    var comment by remember { mutableStateOf("") }
    var isInitialized by remember { mutableStateOf(false) }

    LaunchedEffect(incomeId) { viewModel.loadIncome(incomeId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.my_incomes)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                navigationIcon = {
                    IconButton(onClick = { onNavigateBack() }) {
                        Icon(
                            painter = painterResource(R.drawable.cancel),
                            contentDescription = "Закрыть",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    val state = uiState as? EditIncomeUiState.Success
                    val isFormValid = state?.validationState?.isFormValid == true
                    IconButton(
                        onClick = {
                            viewModel.validateAllFields(value, selectedCategory)
                            val currentState = (uiState as? EditIncomeUiState.Success)
                            if (currentState?.validationState?.isFormValid == true) {
                                viewModel.updateIncome(
                                    value = value,
                                    date = date,
                                    comment = comment,
                                    selectedCategory = selectedCategory
                                )
                            }
                        },
                        enabled = isFormValid
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
            NetworkErrorBanner(
                isVisible = !isNetworkAvailable
            )
            when (uiState) {
                is EditIncomeUiState.Error -> {
                    val errorResId = (uiState as EditIncomeUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.loadIncome(incomeId) }
                    )
                }
                is EditIncomeUiState.Loading -> LoadingScreen()
                is EditIncomeUiState.Success -> {
                    val state = uiState as EditIncomeUiState.Success
                    val income = state.income
                    val validationState = state.validationState

                    LaunchedEffect(income) {
                        if (!isInitialized) {
                            value = income.amount
                            selectedCategory = state.categories.find { it.name == income.title }
                            println("selectedCategory: $selectedCategory")
                            println("income.date: ${income.date}")
                            val parsedDate = parseIso8601LocalDate(income.date)
                            println("parsedDate: $parsedDate")
                            if (parsedDate != null) {
                                date = parsedDate
                                time = parsedDate
                                println("Set date and time to: $parsedDate")
                            } else {
                                println("parsedDate is null, keeping current time")
                            }
                            comment = income.comment ?: ""
                            isInitialized = true
                            viewModel.validateAllFields(value, selectedCategory)
                        }
                    }

                    EditIncomeContent(
                        account = state.account.name,
                        selectedCategory = selectedCategory?.name,
                        value = value,
                        onValueChange = { newValue ->
                            value = newValue
                            viewModel.validateField("value", newValue, selectedCategory)
                        },
                        date = date,
                        time = time,
                        comment = comment,
                        onDateChange = { newDate ->
                            date = newDate
                        },
                        onTimeChange = { newTime ->
                            time = newTime
                        },
                        onCommentChange = { newComment ->
                            comment = newComment
                        },
                        categories = state.categories,
                        onCategorySelected = { category ->
                            selectedCategory = category
                            viewModel.validateField("category", value, category)
                        },
                        validationState = validationState,
                        onDeleteClick = { viewModel.deleteIncome() }
                    )
                }
                is EditIncomeUiState.Updated, is EditIncomeUiState.Deleted -> onNavigateBack()
            }
        }
    }
} 