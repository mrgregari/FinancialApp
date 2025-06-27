package com.example.financialapp.data.repositories

import com.example.financialapp.data.datasources.remote.AccountRemoteDataSource
import com.example.financialapp.data.mappers.AccountMapper
import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.models.Account
import com.example.financialapp.domain.repositories.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountMapper
) : AccountRepository {

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return try {
            val dtos = accountRemoteDataSource.getAccounts()
            val accounts = dtos.map { mapper.fromDtoToAccount(it) }
            NetworkResult.Success(accounts)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}