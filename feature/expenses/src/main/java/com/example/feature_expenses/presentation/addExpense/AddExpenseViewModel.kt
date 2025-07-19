package com.example.feature_expenses.presentation.addExpense

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
import com.example.core_ui.utils.formatToIso8601Local
import com.example.feature_expenses.domain.AddExpenseUseCase
import com.example.feature_expenses.domain.GetExpensesCategoriesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date
import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class AddExpenseViewModel @Inject constructor(
    val getExpensesCategoriesUseCase: GetExpensesCategoriesUseCase,
    val getAccountUseCase: GetAccountUseCase,
    val addExpenseUseCase: AddExpenseUseCase,
    networkState: NetworkState,
    errorHandler: ErrorHandler
) : BaseViewModel(
    networkState,
    errorHandler
) {

    private val _uiState = MutableStateFlow<AddExpenseUiState>(AddExpenseUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)

    init {
        initializeNetworkState()
        loadCategories()
    }

    fun retry() {
        loadCategories()
    }

    private fun loadCategories() {
        safeApiCall(
            apiCall = {
                val accountsResult = getAccountUseCase.invoke()
                when (accountsResult) {
                    is NetworkResult.Success -> {
                        val account = accountsResult.data.firstOrNull()
                        val accountId = account?.id
                        if (accountId != null && accountId != 0) {
                            _account.value = account
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
                _uiState.value = AddExpenseUiState.Success(
                    categories = categories,
                    account = _account.value!!
                )
            },
            onError = { errorResId ->
                _uiState.value = AddExpenseUiState.Error(errorResId)
            }
        )
    }

    fun validateField(field: String, value: String, selectedCategory: Category? = null) {
        val current = _uiState.value
        if (current is AddExpenseUiState.Success) {
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
        if (current is AddExpenseUiState.Success) {
            val validationState = TransactionValidator.validateAll(value, category)
            _uiState.value = current.copy(validationState = validationState)
        }
    }

    @OptIn(ExperimentalTime::class)
    fun addExpense(
        value: String,
        selectedCategory: Category?,
        date: Date,
        comment: String
    ) {
        val current = _uiState.value
        if (current is AddExpenseUiState.Success) {
            val validationState = TransactionValidator.validateAll(value, selectedCategory)
            if (!validationState.isFormValid) {
                _uiState.value = current.copy(validationState = validationState)
                return
            }
            _uiState.value = AddExpenseUiState.Loading
            safeApiCall(
                apiCall = {
                    val accountId = current.account.id
                    val categoryId = selectedCategory!!.id
                    println(date)
                    println(formatToIso8601Local(date))
                    val expense = Expense(
                        id = 0,
                        title = selectedCategory.name,
                        icon = selectedCategory.icon,
                        amount = value.replace(',', '.'),
                        account = current.account.name,
                        currency = current.account.currency,
                        comment = comment.takeIf { it.isNotBlank() },
                        date = formatToIso8601Local(date),
                        updatedAt = Clock.System.now().toString()
                    )
                    addExpenseUseCase(expense, accountId, categoryId)
                },
                onSuccess = { _uiState.value = AddExpenseUiState.Updated },
                onError = { errorResId -> _uiState.value = AddExpenseUiState.Error(errorResId) }
            )
        }
    }
}