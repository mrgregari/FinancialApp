package com.example.feature_expenses.presentation.analytics

import com.example.core_domain.models.Expense
import java.util.Date

sealed class ExpensesAnalyticsUiState {
    object Loading : ExpensesAnalyticsUiState()
    data class Error(val errorResId: Int) : ExpensesAnalyticsUiState()
    data class Success(
        val total: Double,
        val expenses: Map<String, List<Expense>>,
        val startDate: Date?,
        val endDate: Date?,
        val currency: String
    ) : ExpensesAnalyticsUiState()

}