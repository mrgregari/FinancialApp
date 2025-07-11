package com.example.feature_expenses.presentation.addExpense

sealed class ExpenseValidationError {
    object EmptyValue : ExpenseValidationError()
    object InvalidValue : ExpenseValidationError()
    object EmptyCategory : ExpenseValidationError()

    fun getMessage(): String = when (this) {
        is EmptyValue -> "Введите сумму"
        is InvalidValue -> "Некорректная сумма"
        is EmptyCategory -> "Выберите статью"
    }
} 