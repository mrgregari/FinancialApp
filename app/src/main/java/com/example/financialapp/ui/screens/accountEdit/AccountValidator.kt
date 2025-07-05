package com.example.financialapp.ui.screens.accountEdit

import com.example.financialapp.ui.utils.ValidationResult
import com.example.financialapp.ui.utils.ValidationUtils

object AccountValidator {
    
    fun validateName(name: String): ValidationError? {
        return when (ValidationUtils.validateName(name)) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> ValidationError.EmptyName
        }
    }
    
    fun validateBalance(balance: String): ValidationError? {
        return when (ValidationUtils.validateNumber(balance, allowNegative = false, maxDecimals = 2)) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> {
                when {
                    balance.isBlank() -> ValidationError.EmptyBalance
                    else -> ValidationError.InvalidBalance
                }
            }
        }
    }
    
    fun validateCurrency(currency: String): ValidationError? {
        return when (ValidationUtils.validateCurrency(currency)) {
            is ValidationResult.Success -> null
            is ValidationResult.Error -> ValidationError.EmptyCurrency
        }
    }
    
    fun validateAll(name: String, balance: String, currency: String): ValidationState {
        val nameError = validateName(name)?.getMessage()
        val balanceError = validateBalance(balance)?.getMessage()
        val currencyError = validateCurrency(currency)?.getMessage()
        
        val isFormValid = nameError == null && balanceError == null && currencyError == null
                && name.isNotBlank() && balance.isNotBlank() && currency.isNotBlank()
        
        return ValidationState(
            nameError = nameError,
            balanceError = balanceError,
            currencyError = currencyError,
            isFormValid = isFormValid
        )
    }
}