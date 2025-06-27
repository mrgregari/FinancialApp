package com.example.financialapp.data.datasources.remote

import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.models.AccountDto
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