package com.example.feature_incomes.presentation.todayIncomes

import com.example.core_domain.models.Income

sealed class IncomesUiState {
    object Loading : IncomesUiState()
    data class Success(
        val incomes: List<Income>,
        val currency: String
    ) : IncomesUiState()
    data class Error(val errorResId: Int) : IncomesUiState()
} 