package com.example.feature_expenses.presentation.editExpense

import com.example.core_domain.models.Account
import com.example.core_domain.models.Category
import com.example.core_domain.models.Expense
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.core_ui.utils.TransactionValidator
import com.example.core_ui.utils.formatToIso8601
import com.example.feature_expenses.domain.GetExpenseByIdUseCase
import com.example.feature_expenses.domain.GetExpensesCategoriesUseCase
import com.example.feature_expenses.domain.UpdateExpenseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject

class EditExpenseViewModel @Inject constructor(
    val getExpensesCategoriesUseCase: GetExpensesCategoriesUseCase,
    val getAccountUseCase: GetAccountUseCase,
    val getExpenseByIdUseCase: GetExpenseByIdUseCase,
    val updateExpenseUseCase: UpdateExpenseUseCase,
    /*val deleteExpenseUseCase: DeleteExpenseUseCase,*/
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(
    networkState,
    errorHandler
) {
    private val _uiState = MutableStateFlow<EditExpenseUiState>(EditExpenseUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentExpense: Expense? = null
    private var currentAccount: Account? = null

    init {
        initializeNetworkState()
    }

    fun loadExpense(expenseId: Int) {
        _uiState.value = EditExpenseUiState.Loading
        safeApiCall(
            apiCall = { getExpenseByIdUseCase(expenseId) },
            onSuccess = { expense ->
                currentExpense = expense
                loadCategoriesWithExpense(expense)
            },
            onError = { errorResId ->
                _uiState.value = EditExpenseUiState.Error(errorResId)
            }
        )
    }

    private fun loadCategoriesWithExpense(expense: Expense) {
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            currentAccount = account
                            getExpensesCategoriesUseCase()
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
                    _uiState.value = EditExpenseUiState.Error(com.example.core_ui.R.string.account_not_found)
                    return@safeApiCall
                }
                _uiState.value = EditExpenseUiState.Success(
                    categories = categories,
                    account = account,
                    expense = expense
                )
            },
            onError = { errorResId ->
                _uiState.value = EditExpenseUiState.Error(errorResId)
            }
        )
    }

    fun validateField(field: String, value: String, selectedCategory: Category? = null) {
        val current = _uiState.value
        if (current is EditExpenseUiState.Success) {
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
        if (current is EditExpenseUiState.Success) {
            val validationState = TransactionValidator.validateAll(value, category)
            _uiState.value = current.copy(validationState = validationState)
        }
    }

    fun updateExpense(
        value: String,
        selectedCategory: Category?,
        date: Date,
        comment: String
    ) {
        val current = _uiState.value
        val expense = currentExpense
        val category = selectedCategory
        if (current is EditExpenseUiState.Success && expense != null) {
            val validationState = TransactionValidator.validateAll(value, category)
            if (!validationState.isFormValid) {
                _uiState.value = current.copy(validationState = validationState)
                return
            }
            _uiState.value = EditExpenseUiState.Loading
            safeApiCall(
                apiCall = {
                    val updatedExpense = expense.copy(
                        title = category?.name ?: expense.title,
                        icon = category?.icon ?: expense.icon,
                        amount = value.replace(',', '.'),
                        comment = comment.takeIf { it.isNotBlank() },
                        date = formatToIso8601(date)
                    )
                    updateExpenseUseCase(updatedExpense, current.account.id, category?.id ?: 0)
                },
                onSuccess = { _uiState.value = EditExpenseUiState.Updated },
                onError = { errorResId -> _uiState.value = EditExpenseUiState.Error(errorResId) }
            )
        }
    }

    /*
    fun deleteExpense() {
        val expense = currentExpense
        if (expense != null) {
            _uiState.value = EditExpenseUiState.Loading
            safeApiCall(
                apiCall = { deleteExpenseUseCase(expense) },
                onSuccess = { _uiState.value = EditExpenseUiState.Deleted },
                onError = { errorResId -> _uiState.value = EditExpenseUiState.Error(errorResId) }
            )
        }
    }

     */
} 