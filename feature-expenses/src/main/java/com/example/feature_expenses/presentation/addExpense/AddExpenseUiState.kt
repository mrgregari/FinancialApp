package com.example.feature_expenses.presentation.addExpense

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category

sealed class AddExpenseUiState {
    object Loading : AddExpenseUiState()
    data class Error(val errorResId: Int) : AddExpenseUiState()
    data class Success(
        val categories: List<Category>,
        val account: Account,
        val validationState: ExpenseValidationState = ExpenseValidationState()
    ) : AddExpenseUiState()

    object Updated : AddExpenseUiState()

}
