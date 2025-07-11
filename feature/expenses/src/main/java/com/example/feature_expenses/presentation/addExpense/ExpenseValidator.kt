package com.example.feature_expenses.presentation.addExpense

import com.example.core_domain.models.Category

object ExpenseValidator {
    fun validateValue(value: String): ExpenseValidationError? =
        when {
            value.isBlank() -> ExpenseValidationError.EmptyValue
            value.replace(",", ".").toDoubleOrNull() == null -> ExpenseValidationError.InvalidValue
            hasMoreThanTwoDecimals(value) -> ExpenseValidationError.InvalidValue
            else -> null
        }

    private fun hasMoreThanTwoDecimals(value: String): Boolean {
        val normalized = value.replace(",", ".")
        val parts = normalized.split(".")
        return parts.size == 2 && parts[1].length > 2
    }

    fun validateCategory(category: Category?): ExpenseValidationError? =
        if (category == null) ExpenseValidationError.EmptyCategory else null

    fun validateAll(value: String, category: Category?): ExpenseValidationState {
        val valueError = validateValue(value)?.getMessage()
        val categoryError = validateCategory(category)?.getMessage()
        val isFormValid = valueError == null && categoryError == null
        return ExpenseValidationState(
            valueError = valueError,
            categoryError = categoryError,
            isFormValid = isFormValid
        )
    }
} 