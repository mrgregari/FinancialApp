package com.example.feature_incomes.presentation.incomesHistory

import com.example.core_domain.models.Income
import java.util.Date

sealed class IncomesHistoryUiState {
    object Loading : IncomesHistoryUiState()
    data class Success(
        val incomes: List<Income>,
        val startDate: Date?,
        val endDate: Date?,
        val currency: String
    ) : IncomesHistoryUiState()
    data class Error(val errorResId: Int) : IncomesHistoryUiState()
} 