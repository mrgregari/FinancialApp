package com.example.financialapp.ui.screens.expenses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ExpensesViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(ExpensesUiState.Loading)
    val uiState: StateFlow<ExpensesUiState> = _uiState

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadExpenses(startDate = startDate)
    }

    fun loadExpenses(startDate: Date? = null, endDate: Date? = null) {
        _uiState.value = ExpensesUiState.Loading
        viewModelScope.launch {
            val accountsResult = getAccountUseCase.invoke()
            accountsResult.fold(
                onSuccess = { accounts ->
                    val account = accounts.firstOrNull()
                    val accountId = account?.id
                    if (accountId != null && accountId != 0) {
                        val result = getExpensesUseCase(accountId, startDate, endDate)
                        _uiState.value = result.fold(
                            onSuccess = { ExpensesUiState.Success(it) },
                            onFailure = { ExpensesUiState.Error(it) }
                        )
                    } else {
                        _uiState.value = ExpensesUiState.Error(Throwable("Нет доступного счёта"))
                    }
                },
                onFailure = { error ->
                    _uiState.value = ExpensesUiState.Error(error)
                }
            )
        }
    }
} 