package com.example.feature_expenses.presentation.addExpense

data class ExpenseValidationState(
    val valueError: String? = null,
    val categoryError: String? = null,
    val isFormValid: Boolean = false
) {
    fun hasErrors() = valueError != null || categoryError != null
} 