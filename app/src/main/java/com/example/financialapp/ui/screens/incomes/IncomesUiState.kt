package com.example.financialapp.ui.screens.incomes

import com.example.financialapp.domain.models.Income

sealed class IncomesUiState {
    object Loading : IncomesUiState()
    data class Success(val expenses: List<Income>) : IncomesUiState()
    data class Error(val throwable: Throwable) : IncomesUiState()
}