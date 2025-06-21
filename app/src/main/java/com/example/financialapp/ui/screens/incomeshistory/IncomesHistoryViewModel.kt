package com.example.financialapp.ui.screens.incomeshistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.domain.usecases.GetAccountUseCase
import com.example.financialapp.domain.usecases.GetIncomesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class IncomesHistoryViewModel @Inject constructor(
    private val getIncomesUseCase: GetIncomesUseCase,
    private val getAccountUseCase: GetAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<IncomesHistoryUiState>(IncomesHistoryUiState.Loading)
    val uiState: StateFlow<IncomesHistoryUiState> = _uiState

    private var currentStartDate: Date?
    private var currentEndDate: Date?

    init {
        val calendar = Calendar.getInstance()
        currentEndDate = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        currentStartDate = calendar.time
        loadAllIncomes()
    }

    fun updateStartDate(date: Date?) {
        currentStartDate = date
        loadIncomesWithFilter()
    }

    fun updateEndDate(date: Date?) {
        currentEndDate = date
        loadIncomesWithFilter()
    }

    private fun loadAllIncomes() {
        loadIncomesWithFilter()
    }

    private fun loadIncomesWithFilter() {
        _uiState.value = IncomesHistoryUiState.Loading
        viewModelScope.launch {
            val accountsResult = getAccountUseCase.invoke()
            accountsResult.fold(
                onSuccess = { accounts ->
                    val account = accounts.firstOrNull()
                    val accountId = account?.id
                    if (accountId != null && accountId != 0) {
                        val result = getIncomesUseCase(accountId, currentStartDate, currentEndDate)
                        _uiState.value = result.fold(
                            onSuccess = { IncomesHistoryUiState.Success(it, currentStartDate, currentEndDate) },
                            onFailure = { IncomesHistoryUiState.Error(it) }
                        )
                    } else {
                        _uiState.value = IncomesHistoryUiState.Error(Throwable("Нет доступного счёта"))
                    }
                },
                onFailure = { error ->
                    _uiState.value = IncomesHistoryUiState.Error(error)
                }
            )
        }
    }
} 