package com.example.feature_expenses.presentation.expensesHistory

import com.example.core_data.domain.models.Expense
import java.util.Date

sealed class ExpensesHistoryUiState {
    object Loading : ExpensesHistoryUiState()
    data class Success(
        val expenses: List<Expense>,
        val startDate: Date?,
        val endDate: Date?,
        val currency: String
    ) : ExpensesHistoryUiState()
    data class Error(val errorResId: Int) : ExpensesHistoryUiState()
} 