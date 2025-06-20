package com.example.financialapp.ui.screens.expenses

import com.example.financialapp.domain.models.Expense

sealed class ExpensesUiState {
    object Loading : ExpensesUiState()
    data class Success(val expenses: List<Expense>) : ExpensesUiState()
    data class Error(val throwable: Throwable) : ExpensesUiState()
} 