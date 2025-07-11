package com.example.feature_incomes.presentation.addIncome

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_ui.utils.TransactionValidationState

sealed class AddIncomeUiState {
    object Loading : AddIncomeUiState()
    data class Error(val errorResId: Int) : AddIncomeUiState()
    data class Success(
        val categories: List<Category>,
        val account: Account,
        val validationState: TransactionValidationState = TransactionValidationState()
    ) : AddIncomeUiState()
    object Updated : AddIncomeUiState()
} 