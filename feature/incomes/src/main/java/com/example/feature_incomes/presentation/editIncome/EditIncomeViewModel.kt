package com.example.feature_incomes.presentation.editIncome

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_domain.models.Income
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.core_ui.utils.TransactionValidator
import com.example.core_ui.utils.formatToIso8601Local
import com.example.feature_incomes.domain.DeleteIncomeUseCase
import com.example.feature_incomes.domain.GetIncomeByIdUseCase
import com.example.feature_incomes.domain.GetIncomesCategoriesUseCase
import com.example.feature_incomes.domain.UpdateIncomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject

class EditIncomeViewModel @Inject constructor(
    val getIncomesCategoriesUseCase: GetIncomesCategoriesUseCase,
    val getAccountUseCase: GetAccountUseCase,
    val getIncomeByIdUseCase: GetIncomeByIdUseCase,
    val updateIncomeUseCase: UpdateIncomeUseCase,
    val deleteIncomeUseCase: DeleteIncomeUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(
    networkState,
    errorHandler
) {
    private val _uiState = MutableStateFlow<EditIncomeUiState>(EditIncomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentIncome: Income? = null
    private var currentAccount: Account? = null

    init {
        initializeNetworkState()
    }

    fun loadIncome(incomeId: Int) {
        _uiState.value = EditIncomeUiState.Loading
        safeApiCall(
            apiCall = { getIncomeByIdUseCase(incomeId) },
            onSuccess = { income ->
                currentIncome = income
                loadCategoriesWithIncome(income)
            },
            onError = { errorResId ->
                _uiState.value = EditIncomeUiState.Error(errorResId)
            }
        )
    }

    private fun loadCategoriesWithIncome(income: Income) {
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            currentAccount = account
                            getIncomesCategoriesUseCase()
                        } else {
                            NetworkResult.Error(Throwable("Нет доступного счёта"))
                        }
                    }
                    is NetworkResult.Error -> accountsResult
                    is NetworkResult.Loading -> NetworkResult.Loading
                }
            },
            onSuccess = { categories ->
                val account = currentAccount
                if (account == null) {
                    _uiState.value = EditIncomeUiState.Error(com.example.core_ui.R.string.account_not_found)
                    return@safeApiCall
                }
                _uiState.value = EditIncomeUiState.Success(
                    categories = categories,
                    account = account,
                    income = income
                )
            },
            onError = { errorResId ->
                _uiState.value = EditIncomeUiState.Error(errorResId)
            }
        )
    }

    fun validateField(field: String, value: String, selectedCategory: Category? = null) {
        val current = _uiState.value
        if (current is EditIncomeUiState.Success) {
            val validationState = when (field) {
                "value" -> {
                    val valueError = TransactionValidator.validateValue(value)?.getMessage()
                    val vs = current.validationState.copy(valueError = valueError)
                    vs.copy(isFormValid = !vs.hasErrors())
                }
                "category" -> {
                    val categoryError = TransactionValidator.validateCategory(selectedCategory)?.getMessage()
                    val vs = current.validationState.copy(categoryError = categoryError)
                    vs.copy(isFormValid = !vs.hasErrors())
                }
                else -> current.validationState
            }
            _uiState.value = current.copy(validationState = validationState)
        }
    }

    fun validateAllFields(value: String, category: Category?) {
        val current = _uiState.value
        if (current is EditIncomeUiState.Success) {
            val validationState = TransactionValidator.validateAll(value, category)
            _uiState.value = current.copy(validationState = validationState)
        }
    }

    fun updateIncome(
        value: String,
        selectedCategory: Category?,
        date: Date,
        comment: String
    ) {
        val current = _uiState.value
        val income = currentIncome
        val category = selectedCategory
        if (current is EditIncomeUiState.Success && income != null) {
            val validationState = TransactionValidator.validateAll(value, category)
            if (!validationState.isFormValid) {
                _uiState.value = current.copy(validationState = validationState)
                return
            }
            _uiState.value = EditIncomeUiState.Loading
            safeApiCall(
                apiCall = {
                    val updatedIncome = income.copy(
                        title = category?.name ?: income.title,
                        icon = category?.icon ?: income.icon,
                        amount = value.replace(',', '.'),
                        comment = comment.takeIf { it.isNotBlank() },
                        date = formatToIso8601Local(date)
                    )
                    updateIncomeUseCase(updatedIncome, current.account.id, category?.id ?: 0)
                },
                onSuccess = { _uiState.value = EditIncomeUiState.Updated },
                onError = { errorResId -> _uiState.value = EditIncomeUiState.Error(errorResId) }
            )
        }
    }

    fun deleteIncome() {
        val income = currentIncome
        if (income != null) {
            _uiState.value = EditIncomeUiState.Loading
            safeApiCall(
                apiCall = { deleteIncomeUseCase(income.id) },
                onSuccess = { _uiState.value = EditIncomeUiState.Deleted },
                onError = { errorResId -> _uiState.value = EditIncomeUiState.Error(errorResId) }
            )
        }
    }
} 