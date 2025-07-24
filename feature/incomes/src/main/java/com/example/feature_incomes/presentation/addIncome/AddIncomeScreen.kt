package com.example.feature_incomes.presentation.addIncome

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
import com.example.core_ui.utils.combineDateAndTime
import com.example.feature_incomes.di.DaggerIncomesComponent
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddIncomeScreen(
    onNavigateBack: () -> Unit
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val incomesComponent = remember {
        DaggerIncomesComponent.factory()
            .create(app.dataComponent)
    }
    val viewModelFactory = incomesComponent.viewModelFactory()
    val viewModel: AddIncomeViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var value by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by rememberSaveable { mutableStateOf(Date()) }
    var time by rememberSaveable { mutableStateOf(Date()) }
    var comment by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.add_income)) },
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
                    val state = uiState as? AddIncomeUiState.Success
                    val isFormValid = state?.validationState?.isFormValid == true
                    IconButton(
                        onClick = {
                            viewModel.validateAllFields(value, selectedCategory)
                            val currentState = (uiState as? AddIncomeUiState.Success)
                            if (currentState?.validationState?.isFormValid == true) {
                                val combinedDateTime = combineDateAndTime(date, time)
                                viewModel.addIncome(
                                    value = value,
                                    selectedCategory = selectedCategory,
                                    date = combinedDateTime,
                                    comment = comment
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
                is AddIncomeUiState.Error -> {
                    val errorResId = (uiState as AddIncomeUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }
                is AddIncomeUiState.Loading -> LoadingScreen()
                is AddIncomeUiState.Success -> {
                    val state = uiState as AddIncomeUiState.Success
                    AddIncomeContent(
                        account = state.account.name,
                        selectedCategory = selectedCategory?.name,
                        value = value,
                        onValueChange = {
                            value = it
                            viewModel.validateField("value", it, selectedCategory)
                        },
                        date = date,
                        time = time,
                        comment = comment,
                        onDateChange = {
                            date = it
                        },
                        onTimeChange = {
                            time = it
                        },
                        onCommentChange = {
                            comment = it
                        },
                        categories = state.categories,
                        onCategorySelected = {
                            selectedCategory = it
                            viewModel.validateField("category", value, it)
                        },
                        validationState = state.validationState
                    )
                }
                is AddIncomeUiState.Updated -> onNavigateBack()
            }
        }
    }
} 