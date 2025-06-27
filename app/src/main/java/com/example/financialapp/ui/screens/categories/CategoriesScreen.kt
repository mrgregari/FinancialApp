package com.example.financialapp.ui.screens.categories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financialapp.R
import com.example.financialapp.domain.models.Category
import com.example.financialapp.ui.components.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel: CategoriesViewModel = viewModel(factory = viewModelFactory)
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorResId by viewModel.errorResId.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

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

            when {
                isLoading -> LoadingScreen()
                errorResId != null ->
                    ErrorScreen(
                        error = stringResource(errorResId!!),
                        onRetry = { viewModel.retry() }
                    )

                else -> CategoriesContent(
                    categories = categories,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CategoriesContent(
    categories: List<Category>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            SearchBar()
            HorizontalDivider()
        }

        items(
            items = categories,
            key = { it.id }
        ) { category ->
            CustomListItem(
                modifier = Modifier.height(70.dp),
                emoji = category.icon,
                title = category.name
            )
            HorizontalDivider()
        }
    }
}

