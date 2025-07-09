package com.example.feature_expenses.presentation

import com.example.core_data.domain.models.Expense

sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    data class Error(val errorResId: Int) : ExpensesUiState()
    data class Success(
        val expenses: List<Expense>,
        val currency: String
    ) : ExpensesUiState()

}