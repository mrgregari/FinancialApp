package com.example.feature_expenses.presentation.addExpense

import com.example.core_domain.models.Category
import com.example.core_domain.usecases.GetAccountUseCase
import com.example.core_network.network.ErrorHandler
import com.example.core_network.network.NetworkResult
import com.example.core_network.network.NetworkState
import com.example.core_ui.base.BaseViewModel
import com.example.feature_expenses.domain.GetExpensesCategoriesUseCase
import com.example.feature_expenses.presentation.todayExpenses.ExpensesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import com.example.feature_expenses.presentation.addExpense.ExpenseValidator
import com.example.feature_expenses.presentation.addExpense.ExpenseValidationState
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_data.remote.dto.TransactionDto
import com.example.core_data.remote.dto.AccountDto
import com.example.core_data.remote.dto.CategoryDto
import com.example.core_domain.models.Account
import com.example.core_domain.models.Expense
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.feature_expenses.domain.AddExpenseUseCase
import com.example.core_ui.utils.formatToIso8601

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

    private val _startDate = MutableStateFlow<Date?>(null)

    private val _endDate = MutableStateFlow<Date?>(null)

    private val _uiState = MutableStateFlow<AddExpenseUiState>(AddExpenseUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _account = MutableStateFlow<Account?>(null)

    init {
        val calendar = Calendar.getInstance()
        _endDate.value = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        _startDate.value = calendar.time
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
                    val valueError = ExpenseValidator.validateValue(value)?.getMessage()
                    val vs = current.validationState.copy(valueError = valueError)
                    vs.copy(isFormValid = !vs.hasErrors())
                }
                "category" -> {
                    val categoryError = ExpenseValidator.validateCategory(selectedCategory)?.getMessage()
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
            val validationState = ExpenseValidator.validateAll(value, category)
            _uiState.value = current.copy(validationState = validationState)
        }
    }

    fun addExpense(
        value: String,
        selectedCategory: Category?,
        date: Date,
        comment: String
    ) {
        val current = _uiState.value
        if (current is AddExpenseUiState.Success) {
            val validationState = ExpenseValidator.validateAll(value, selectedCategory)
            if (!validationState.isFormValid) {
                _uiState.value = current.copy(validationState = validationState)
                return
            }
            _uiState.value = AddExpenseUiState.Loading
            safeApiCall(
                apiCall = {
                    val accountId = current.account.id
                    val categoryId = selectedCategory!!.id
                    val expense = Expense(
                        id = 0,
                        title = selectedCategory.name,
                        icon = selectedCategory.icon,
                        amount = value.replace(',', '.'),
                        account = current.account.name,
                        currency = current.account.currency,
                        comment = comment.takeIf { it.isNotBlank() },
                        date = formatToIso8601(date)
                    )
                    addExpenseUseCase(expense, accountId, categoryId)
                },
                onSuccess = { _uiState.value = AddExpenseUiState.Updated },
                onError = { errorResId -> _uiState.value = AddExpenseUiState.Error(errorResId) }
            )
        }
    }
}