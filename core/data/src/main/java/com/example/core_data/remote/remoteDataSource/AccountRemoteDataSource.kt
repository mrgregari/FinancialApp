package com.example.core_data.remote.remoteDataSource

import com.example.core_data.remote.dto.AccountDto
import com.example.core_data.remote.dto.UpdateAccountDto
import com.example.core_data.remote.api.FinancialApi
import jakarta.inject.Inject
import retrofit2.HttpException

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