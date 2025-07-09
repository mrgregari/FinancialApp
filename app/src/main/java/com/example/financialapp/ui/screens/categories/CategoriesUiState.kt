package com.example.financialapp.ui.screens.categories

import com.example.financialapp.domain.models.Category

sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    data class Success(
        val categories: List<Category>,
        val searchText: String
    ) : CategoriesUiState()

    data class Error(val errorResId: Int) : CategoriesUiState()
}
