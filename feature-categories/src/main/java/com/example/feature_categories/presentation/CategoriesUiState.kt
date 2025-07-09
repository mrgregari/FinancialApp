package com.example.feature_categories.presentation

import com.example.core_data.domain.models.Category

sealed class CategoriesUiState {
    object Loading : CategoriesUiState()
    data class Success(
        val categories: List<Category>,
        val searchText: String
    ) : CategoriesUiState()

    data class Error(val errorResId: Int) : CategoriesUiState()
}
