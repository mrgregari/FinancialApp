package com.example.financialapp.data.remote.remoteDataSource

import com.example.financialapp.data.remote.api.FinancialApi
import com.example.financialapp.data.dto.AccountDto
import javax.inject.Inject

/**
 * Account data source
 * Gets data from API
 */
class AccountRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getAccounts(): List<AccountDto> = api.getAccounts()
}