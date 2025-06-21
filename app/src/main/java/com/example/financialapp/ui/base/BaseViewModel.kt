package com.example.financialapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financialapp.data.network.ErrorHandler
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.data.network.NetworkState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {
    
    @Inject
    lateinit var networkState: NetworkState
    
    @Inject
    lateinit var errorHandler: ErrorHandler
    
    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    protected fun initializeNetworkState() {
        observeNetworkState()
    }
    
    private fun observeNetworkState() {
        viewModelScope.launch {
            networkState.observeNetworkState().collect { isAvailable ->
                _isNetworkAvailable.value = isAvailable
                if (!isAvailable) {
                    showError("Нет подключения к интернету")
                }
            }
        }
    }
    
    protected fun showError(message: String) {
        _errorMessage.value = message
    }
    
    protected fun clearError() {
        _errorMessage.value = null
    }
    
    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    protected fun <T> safeApiCall(
        apiCall: suspend () -> NetworkResult<T>,
        onSuccess: (T) -> Unit = {},
        onError: (String) -> Unit = { showError(it) }
    ) {
        viewModelScope.launch {
            setLoading(true)
            try {
                val result = apiCall()
                when (result) {
                    is NetworkResult.Success -> {
                        onSuccess(result.data)
                        clearError()
                    }
                    is NetworkResult.Error -> {
                        val errorMessage = errorHandler.handleException(result.exception)
                        onError(errorMessage)
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            } catch (e: Exception) {
                val errorMessage = errorHandler.handleException(e)
                onError(errorMessage)
            } finally {
                setLoading(false)
            }
        }
    }
} 