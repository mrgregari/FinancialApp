package com.example.feature_expenses.presentation.addExpense

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_ui.utils.TransactionValidationState

sealed class AddExpenseUiState {
    object Loading : AddExpenseUiState()
    data class Error(val errorResId: Int) : AddExpenseUiState()
    data class Success(
        val categories: List<Category>,
        val account: Account,
        val validationState: TransactionValidationState = TransactionValidationState()
    ) : AddExpenseUiState()

    object Updated : AddExpenseUiState()

}
