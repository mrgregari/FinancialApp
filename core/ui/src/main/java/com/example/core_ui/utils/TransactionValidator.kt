package com.example.core_ui.utils

import com.example.core_domain.models.Category


data class TransactionValidationState(
    val valueError: String? = null,
    val categoryError: String? = null,
    val isFormValid: Boolean = false
) {
    fun hasErrors() = valueError != null || categoryError != null
}

sealed class TransactionValidationError {
    object EmptyValue : TransactionValidationError()
    object InvalidValue : TransactionValidationError()
    object EmptyCategory : TransactionValidationError()

    fun getMessage(): String = when (this) {
        is EmptyValue -> "Введите сумму"
        is InvalidValue -> "Некорректная сумма"
        is EmptyCategory -> "Выберите статью"
    }
}

object TransactionValidator {
    fun validateValue(value: String): TransactionValidationError? =
        when {
            value.isBlank() -> TransactionValidationError.EmptyValue
            value.replace(",", ".").toDoubleOrNull() == null -> TransactionValidationError.InvalidValue
            hasMoreThanTwoDecimals(value) -> TransactionValidationError.InvalidValue
            else -> null
        }

    private fun hasMoreThanTwoDecimals(value: String): Boolean {
        val normalized = value.replace(",", ".")
        val parts = normalized.split(".")
        return parts.size == 2 && parts[1].length > 2
    }

    fun validateCategory(category: Category?): TransactionValidationError? =
        if (category == null) TransactionValidationError.EmptyCategory else null

    fun validateAll(value: String, category: Category?): TransactionValidationState {
        val valueError = validateValue(value)?.getMessage()
        val categoryError = validateCategory(category)?.getMessage()
        val isFormValid = valueError == null && categoryError == null
        return TransactionValidationState(
            valueError = valueError,
            categoryError = categoryError,
            isFormValid = isFormValid
        )
    }
} 