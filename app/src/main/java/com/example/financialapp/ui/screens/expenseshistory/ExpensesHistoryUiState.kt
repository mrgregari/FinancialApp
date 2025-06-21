package com.example.financialapp.ui.screens.expenseshistory

import com.example.financialapp.domain.models.Expense
import java.util.Date

sealed class ExpensesHistoryUiState {
    object Loading : ExpensesHistoryUiState()
    data class Success(
        val expenses: List<Expense>,
        val startDate: Date? = null,
        val endDate: Date? = null
    ) : ExpensesHistoryUiState()
    data class Error(val throwable: Throwable) : ExpensesHistoryUiState()
} 