package com.example.financialapp.ui.screens.incomeshistory

import com.example.financialapp.domain.models.Income
import java.util.Date

sealed class IncomesHistoryUiState {
    object Loading : IncomesHistoryUiState()
    data class Success(
        val incomes: List<Income>,
        val startDate: Date? = null,
        val endDate: Date? = null
    ) : IncomesHistoryUiState()
    data class Error(val throwable: Throwable) : IncomesHistoryUiState()
} 