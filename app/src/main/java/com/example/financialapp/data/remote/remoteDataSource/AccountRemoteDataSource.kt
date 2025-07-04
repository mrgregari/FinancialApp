package com.example.financialapp.data.remote.remoteDataSource

import com.example.financialapp.data.remote.api.FinancialApi
import com.example.financialapp.data.remote.dto.AccountDto
import com.example.financialapp.data.remote.dto.UpdateAccountDto
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Account data source
 * Gets data from API
 */
class AccountRemoteDataSource @Inject constructor(
    private val api: FinancialApi
) {
    suspend fun getAccounts(): List<AccountDto> = api.getAccounts()

    suspend fun updateAccount(accountId: Int, update: UpdateAccountDto) {
        val response = api.updateAccount(accountId, update)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
    }
}