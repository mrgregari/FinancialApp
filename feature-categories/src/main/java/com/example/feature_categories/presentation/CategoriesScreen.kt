package com.example.feature_categories.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core_data.di.DataComponentProvider
import com.example.core_ui.R
import com.example.core_ui.components.ErrorScreen
import com.example.core_ui.components.LoadingScreen
import com.example.core_ui.components.NetworkErrorBanner
import com.example.feature_categories.di.DaggerCategoriesComponent
import androidx.compose.runtime.remember


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
) {
    val app = LocalContext.current.applicationContext as DataComponentProvider
    val categoriesComponent = remember {
        DaggerCategoriesComponent.factory()
            .create(app.dataComponent)
    }

    val viewModelFactory = categoriesComponent.viewModelFactory()

    val viewModel: CategoriesViewModel = viewModel(factory = viewModelFactory)
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.categories_title)) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                is CategoriesUiState.Loading -> LoadingScreen()
                is CategoriesUiState.Error -> {
                    val errorResId = (uiState as CategoriesUiState.Error).errorResId
                    ErrorScreen(
                        error = stringResource(errorResId),
                        onRetry = { viewModel.retry() }
                    )
                }
                is CategoriesUiState.Success -> {
                    val categories = (uiState as CategoriesUiState.Success).categories
                    val searchText = (uiState as CategoriesUiState.Success).searchText
                    CategoriesContent(
                        categories = categories,
                        modifier = Modifier.fillMaxSize(),
                        searchText = searchText,
                        onSearchTextChanged = { viewModel.onSearchTextChange(it) }
                    )
                }
            }

        }
    }
}



