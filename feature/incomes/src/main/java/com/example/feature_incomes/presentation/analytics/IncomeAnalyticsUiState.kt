package com.example.feature_incomes.presentation.analytics

import com.example.core_domain.models.Income
import java.util.Date

sealed class IncomeAnalyticsUiState {
    object Loading : IncomeAnalyticsUiState()
    data class Error(val errorResId: Int) : IncomeAnalyticsUiState()
    data class Success(
        val total: Double,
        val incomes: Map<String, List<Income>>,
        val startDate: Date?,
        val endDate: Date?,
        val currency: String
    ) : IncomeAnalyticsUiState()
} 