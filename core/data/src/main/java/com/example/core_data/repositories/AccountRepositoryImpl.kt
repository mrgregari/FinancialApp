package com.example.core_data.repositories

import com.example.core_data.remote.dto.UpdateAccountDto
import com.example.core_data.remote.remoteDataSource.AccountRemoteDataSource
import com.example.core_data.di.IODispatcher
import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.entity.AccountEntity
import com.example.core_data.mappers.AccountMapper
import com.example.core_domain.models.Account
import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

class AccountRepositoryImpl @Inject constructor(
    private val accountRemoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountMapper,
    private val accountDao: AccountDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : AccountRepository {

    override suspend fun getAccounts(): NetworkResult<List<Account>> {
        return withContext(ioDispatcher) {
            try {
                val dtos = accountRemoteDataSource.getAccounts()
                val remoteEntities = dtos.map { mapper.fromDtoToEntity(it) }
                val localEntities = accountDao.getAll()
                val localMap = localEntities.associateBy { it.id }

                val toUpdateLocally = mutableListOf<AccountEntity>()
                for (remote in remoteEntities) {
                    val local = localMap[remote.id]
                    if (local == null || remote.updatedAt > local.updatedAt) {
                        toUpdateLocally.add(remote)
                    }
                }
                if (toUpdateLocally.isNotEmpty()) {
                    accountDao.insertAll(toUpdateLocally)
                }

                val resultAccounts = accountDao.getAll().map { mapper.fromEntityToDomain(it) }
                NetworkResult.Success(resultAccounts)
            } catch (e: Exception) {
                val localAccounts = accountDao.getAll().map { mapper.fromEntityToDomain(it) }
                NetworkResult.Success(localAccounts)
            }
        }
    }

    @OptIn(ExperimentalTime::class)
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
                accountDao.insertAccount(AccountEntity(
                    id,
                    name,
                    balance,
                    currency,
                    Clock.System.now().toString()
                ))
                NetworkResult.Success(Unit)
                //NetworkResult.Error(e)
            }
        }
    }
}