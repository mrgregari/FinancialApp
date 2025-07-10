package com.example.core_data.repositories

import com.example.core_data.remote.dto.UpdateAccountDto
import com.example.core_data.remote.mappers.AccountMapper
import com.example.core_data.remote.remoteDataSource.AccountRemoteDataSource
import com.example.core_data.di.IODispatcher
import com.example.core_domain.models.Account
import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountMapper,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return withContext(ioDispatcher) {
            try {
                val dtos = accountRemoteDataSource.getAccounts()
                val accounts = dtos.map { mapper.fromDtoToAccount(it) }
                NetworkResult.Success(accounts)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun updateAccount(
        id: Int,
        name: String,
        balance: String,
        currency: String
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                accountRemoteDataSource.updateAccount(
                    id,
                    UpdateAccountDto(name, balance, currency)
                )
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
    }
}