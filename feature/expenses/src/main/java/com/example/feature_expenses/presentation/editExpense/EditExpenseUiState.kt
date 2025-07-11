package com.example.feature_expenses.presentation.editExpense

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_domain.models.Expense
import com.example.core_ui.utils.TransactionValidationState

sealed class EditExpenseUiState {
    object Loading : EditExpenseUiState()
    data class Error(val errorResId: Int) : EditExpenseUiState()
    data class Success(
        val categories: List<Category>,
        val account: Account,
        val expense: Expense,
        val validationState: TransactionValidationState = TransactionValidationState()
    ) : EditExpenseUiState()
    object Updated : EditExpenseUiState()
    object Deleted : EditExpenseUiState()
} 