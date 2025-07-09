package com.example.core_ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core_data.network.ErrorHandler
import com.example.core_data.network.NetworkResult
import com.example.core_ui.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.core_data.network.NetworkState
import com.example.core_ui.utils.getErrorResId

/**
 * Base ViewModel that provides network state observation,
 * error handling functionality, and common UI state management.
 * 
 * This abstract class should be extended by other ViewModels to inherit:
 * - Network availability monitoring
 * - Loading state management  
 * - Error handling with resource IDs
 * - Safe API call execution
 * 
 * @param networkState Network state observer for connectivity monitoring
 * @param errorHandler Handler for converting exceptions to user-friendly error messages
 */

abstract class BaseViewModel(
    private val networkState: NetworkState,
    protected val errorHandler: ErrorHandler
): ViewModel() {

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> = _isNetworkAvailable.asStateFlow()
    
    private val _errorResId = MutableStateFlow<Int?>(null)
    val errorResId: StateFlow<Int?> = _errorResId.asStateFlow()
    
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
                    showError(R.string.no_internet)
                }
            }
        }
    }
    
    protected fun showError(resId: Int) {
        _errorResId.value = resId
    }
    
    protected fun clearError() {
        _errorResId.value = null
    }
    
    protected fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }

    protected fun <T> safeApiCall(
        apiCall: suspend () -> NetworkResult<T>,
        onSuccess: (T) -> Unit = {},
        onError: (Int) -> Unit = { showError(it) }
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
                        val errorType = errorHandler.getErrorType(result.exception)
                        val errorResId = getErrorResId(errorType)
                        onError(errorResId)
                    }
                    is NetworkResult.Loading -> {
                    }
                }
            } catch (e: Exception) {
                val errorType = errorHandler.getErrorType(e)
                val errorResId = getErrorResId(errorType)
                onError(errorResId)
            } finally {
                setLoading(false)
            }
        }
    }
} 