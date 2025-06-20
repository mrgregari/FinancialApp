package com.example.financialapp.data.repositories

import com.example.financialapp.data.api.FinancialApi
import com.example.financialapp.data.mappers.AccountMapper
import com.example.financialapp.domain.models.Account
import com.example.financialapp.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val api: FinancialApi,
    private val mapper: AccountMapper
) : AccountRepository {
    override suspend fun getAllAccounts(
    ): Result<List<Account>> {
        return try {
            val dtos = api.getAccounts()
            val result = dtos.map { mapper.fromDto(it) }
            return Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}