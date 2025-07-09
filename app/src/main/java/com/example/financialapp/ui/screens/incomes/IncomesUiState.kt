package com.example.financialapp.ui.screens.incomes

import com.example.financialapp.domain.models.Income

sealed class IncomesUiState {
    object Loading : IncomesUiState()
    data class Success(
        val incomes: List<Income>,
        val currency: String
    ) : IncomesUiState()
    data class Error(val errorResId: Int) : IncomesUiState()
} 