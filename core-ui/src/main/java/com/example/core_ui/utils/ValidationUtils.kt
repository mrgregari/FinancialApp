package com.example.core_ui.utils


object ValidationUtils {
    

    fun validateName(name: String, minLength: Int = 1, maxLength: Int = 50): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Имя не может быть пустым")
            name.length < minLength -> ValidationResult.Error("Имя должно содержать минимум $minLength символов")
            name.length > maxLength -> ValidationResult.Error("Имя не должно превышать $maxLength символов")
            !name.matches(Regex("^[a-zA-Zа-яА-Я0-9\\s]+$")) -> ValidationResult.Error("Имя содержит недопустимые символы")
            else -> ValidationResult.Success
        }
    }
    

    fun validateNumber(value: String, allowNegative: Boolean = false, maxDecimals: Int = 2): ValidationResult {
        return when {
            value.isBlank() -> ValidationResult.Error("Значение не может быть пустым")
            value.toDoubleOrNull() == null -> ValidationResult.Error("Введите корректное число")
            !allowNegative && value.toDouble() < 0 -> ValidationResult.Error("Значение не может быть отрицательным")
            value.contains(".") && value.split(".")[1].length > maxDecimals -> 
                ValidationResult.Error("Максимальное количество знаков после запятой: $maxDecimals")
            else -> ValidationResult.Success
        }
    }

    fun validateCurrency(currency: String): ValidationResult {
        return if (currency.isBlank()) {
            ValidationResult.Error("Выберите валюту")
        } else {
            ValidationResult.Success
        }
    }

}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
} 