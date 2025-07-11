package com.example.feature_expenses.presentation.addExpense

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
import com.example.core_domain.models.Category
import com.example.core_ui.R
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.feature_expenses.di.DaggerExpensesComponent
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit
) {

    val app = LocalContext.current.applicationContext as DataComponentProvider
    val expensesComponent = remember {
        DaggerExpensesComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = expensesComponent.viewModelFactory()

    val viewModel: AddExpenseViewModel = viewModel(factory = viewModelFactory)

    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var value by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var date by remember { mutableStateOf(Date()) }
    var time by remember { mutableStateOf(Date()) }
    var comment by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.my_expenses)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
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
                    val state = uiState as? AddExpenseUiState.Success
                    val isFormValid = state?.validationState?.isFormValid == true
                    IconButton(
                        onClick = {
                            viewModel.validateAllFields(value, selectedCategory)
                            val currentState = (uiState as? AddExpenseUiState.Success)
                            if (currentState?.validationState?.isFormValid == true) {
                                viewModel.addExpense(
                                    value = value,
                                    selectedCategory = selectedCategory,
                                    date = date,
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
                is AddExpenseUiState.Error -> {
                    val errorResId = (uiState as AddExpenseUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }
                is AddExpenseUiState.Loading -> LoadingScreen()
                is AddExpenseUiState.Success -> {
                    val state = uiState as AddExpenseUiState.Success
                    AddExpenseContent(
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
                is AddExpenseUiState.Updated -> onNavigateBack()
            }
        }
    }

}

/*
@Preview
@Composable
fun AddExpensePreview() {
    AddExpenseScreen()
}

 */

