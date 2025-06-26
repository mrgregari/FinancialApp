package com.example.financialapp.ui.screens.categories

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.financialapp.ui.components.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
    viewModelFactory: ViewModelProvider.Factory
) {
    val viewModel : CategoriesViewModel = viewModel(factory = viewModelFactory)
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isNetworkAvailable by viewModel.isNetworkAvailable.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Мои статьи") },
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
                isVisible = !isNetworkAvailable,
                onDismiss = { }
            )

            if (isLoading) {
                LoadingScreen()
            } else if (errorMessage != null) {
                ErrorScreen(
                    error = errorMessage!!,
                    onRetry = { viewModel.retry() }
                )
            } else {
                CategoriesContent(
                    categories = categories,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun CategoriesContent(
    categories: List<com.example.financialapp.domain.models.Category>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        stickyHeader {
            SearchBar()
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
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
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

