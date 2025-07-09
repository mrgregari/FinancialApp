package com.example.financialapp.ui.screens.incomesHistory

import com.example.financialapp.domain.models.Income
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