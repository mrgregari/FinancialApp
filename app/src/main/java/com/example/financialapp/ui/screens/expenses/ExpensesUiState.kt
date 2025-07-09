package com.example.financialapp.ui.screens.expenses

import com.example.financialapp.domain.models.Expense

sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    data class Error(val errorResId: Int) : ExpensesUiState()
    data class Success(
        val expenses: List<Expense>,
        val currency: String
    ) : ExpensesUiState()

}