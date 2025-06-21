package com.example.financialapp.ui.screens.expenseshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject
import java.util.Calendar

@HiltViewModel
class ExpensesHistoryViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesHistoryUiState>(ExpensesHistoryUiState.Loading)
    val uiState: StateFlow<ExpensesHistoryUiState> = _uiState

    private var currentStartDate: Date?
    private var currentEndDate: Date?

    init {
        val calendar = Calendar.getInstance()
        currentEndDate = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        currentStartDate = calendar.time
        loadAllExpenses()
    }

    fun updateStartDate(date: Date?) {
        currentStartDate = date
        loadExpensesWithFilter()
    }

    fun updateEndDate(date: Date?) {
        currentEndDate = date
        loadExpensesWithFilter()
    }

    private fun loadAllExpenses() {
        loadExpensesWithFilter()
    }

    private fun loadExpensesWithFilter() {
        _uiState.value = ExpensesHistoryUiState.Loading
        viewModelScope.launch {
            val accountsResult = getAccountUseCase.invoke()
            accountsResult.fold(
                onSuccess = { accounts ->
                    val account = accounts.firstOrNull()
                    val accountId = account?.id
                    if (accountId != null && accountId != 0) {
                        val result = getExpensesUseCase(accountId, currentStartDate, currentEndDate)
                        _uiState.value = result.fold(
                            onSuccess = { ExpensesHistoryUiState.Success(it, currentStartDate, currentEndDate) },
                            onFailure = { ExpensesHistoryUiState.Error(it) }
                        )
                    } else {
                        _uiState.value = ExpensesHistoryUiState.Error(Throwable("Нет доступного счёта"))
                    }
                },
                onFailure = { error ->
                    _uiState.value = ExpensesHistoryUiState.Error(error)
                }
            )
        }
    }
} 