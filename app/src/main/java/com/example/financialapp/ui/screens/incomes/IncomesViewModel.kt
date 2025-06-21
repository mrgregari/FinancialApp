package com.example.financialapp.ui.screens.incomes

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
class IncomesViewModel @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
    private val getIncomesUseCase: GetIncomesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<IncomesUiState>(IncomesUiState.Loading)
    val uiState: StateFlow<IncomesUiState> = _uiState

    init {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startDate = calendar.time
        loadIncomes(startDate = startDate)
    }

    private fun loadIncomes(startDate: Date? = null, endDate: Date? = null) {
        _uiState.value = IncomesUiState.Loading
        viewModelScope.launch {
            val accountsResult = getAccountUseCase.invoke()
            accountsResult.fold(
                onSuccess = { accounts ->
                    val account = accounts.firstOrNull()
                    val accountId = account?.id
                    if (accountId != null && accountId != 0) {
                        val result = getIncomesUseCase(accountId, startDate, endDate)
                        _uiState.value = result.fold(
                            onSuccess = { IncomesUiState.Success(it) },
                            onFailure = { IncomesUiState.Error(it) }
                        )
                    } else {
                        _uiState.value = IncomesUiState.Error(Throwable("Нет доступного счёта"))
                    }
                },
                onFailure = { error ->
                    _uiState.value = IncomesUiState.Error(error)
                }
            )
        }
    }
}