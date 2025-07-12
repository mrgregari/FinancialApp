package com.example.feature_incomes.presentation.editIncome

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_domain.models.Income
import com.example.core_ui.utils.TransactionValidationState

sealed class EditIncomeUiState {
    object Loading : EditIncomeUiState()
    data class Error(val errorResId: Int) : EditIncomeUiState()
    data class Success(
        val categories: List<Category>,
        val account: Account,
        val income: Income,
        val validationState: TransactionValidationState = TransactionValidationState()
    ) : EditIncomeUiState()
    object Updated : EditIncomeUiState()
    object Deleted : EditIncomeUiState()
} 