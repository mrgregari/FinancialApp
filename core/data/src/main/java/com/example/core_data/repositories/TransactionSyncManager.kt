package com.example.core_data.repositories

import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.mappers.TransactionMapper
import com.example.core_data.remote.remoteDataSource.TransactionRemoteDataSource
import com.example.core_domain.models.Account
import com.example.core_data.local.entity.TransactionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionSyncManager @Inject constructor(
    private val transactionRemoteDataSource: TransactionRemoteDataSource,
    private val transactionDao: TransactionDao,
    private val mapper: TransactionMapper
) {
    suspend fun syncTransactionsWithRemote(accounts: List<Account>) {
        for (account in accounts) {
            val remoteAll = transactionRemoteDataSource.getTransactions(
                accountId = account.id,
                startDate = "0000-01-01",
                endDate = "9999-12-31"
            )

            val localEntities = transactionDao.getAllTransactions().filter { it.accountId == account.id }
            println(localEntities.joinToString())
            val localMap = localEntities.associateBy { it.id }
            val remoteMap = remoteAll.associateBy { it.id }

            // 0. Удаляем все локальные транзакции с isDeleted == true и пробуем удалить их с сервера
            for (local in localEntities.filter { it.isDeleted }) {
                if (local.id > 0) {
                    try {
                        transactionRemoteDataSource.deleteTransactionById(local.id)
                    } catch (e: Exception) {
                        // Можно залогировать ошибку, если нужно
                    }
                }
                transactionDao.deleteTransactionById(local.id)
            }

            val toInsertOrUpdateLocally = mutableListOf<TransactionEntity>()
            for (remote in remoteAll) {
                // Найти все локальные с временным (отрицательным) id и совпадающими уникальными полями
                val localTemps = localEntities.filter {
                    it.id < 0 &&
                    it.amount == remote.amount &&
                    it.transactionDate == remote.transactionDate &&
                    it.accountId == remote.account.id &&
                    it.categoryId == remote.category.id &&
                    it.comment == remote.comment
                }
                for (localTemp in localTemps) {
                    transactionDao.deleteTransactionById(localTemp.id)
                }
                val local = localMap[remote.id]
                if (local == null) {
                    toInsertOrUpdateLocally.add(
                        TransactionEntity(
                            id = remote.id,
                            accountId = account.id,
                            categoryId = remote.category.id,
                            amount = remote.amount,
                            transactionDate = remote.transactionDate,
                            comment = remote.comment,
                            updatedAt = remote.updatedAt,
                            isDeleted = false,
                            isSynced = true
                        )
                    )
                } else if (remote.updatedAt > local.updatedAt && !local.isDeleted) {
                    toInsertOrUpdateLocally.add(
                        local.copy(
                            amount = remote.amount,
                            transactionDate = remote.transactionDate,
                            comment = remote.comment,
                            updatedAt = remote.updatedAt,
                            isDeleted = false,
                            isSynced = true
                        )
                    )
                }
            }
            if (toInsertOrUpdateLocally.isNotEmpty()) {
                transactionDao.insertTransactions(toInsertOrUpdateLocally)
            }

            // 2. Сначала отправляем на сервер только временные транзакции (id < 0, isSynced == false, isDeleted == false)
            val unsyncedLocals = localEntities.filter { it.id < 0 && !it.isSynced && !it.isDeleted }.toMutableList()
            for (local in unsyncedLocals.toList()) { // toList чтобы избежать ConcurrentModificationException
                val response = transactionRemoteDataSource.postTransaction(
                    mapper.fromEntityToCreateDto(local)
                )
                if (response.isSuccessful) {
                    println("posted transaction")
                    val created = response.body()!!
                    // Удаляем только по id
                    transactionDao.deleteTransactionById(local.id)
                    // Вставляем новую с id от сервера и isSynced = true
                    transactionDao.insertTransaction(
                        TransactionEntity(
                            id = created.id,
                            accountId = created.account.id,
                            categoryId = created.category.id,
                            amount = created.amount,
                            transactionDate = created.transactionDate,
                            comment = created.comment,
                            updatedAt = created.updatedAt,
                            isDeleted = false,
                            isSynced = true
                        )
                    )
                    unsyncedLocals.remove(local)
                }
            }

            // 3. Теперь обновляем только те, у кого id > 0 и isSynced == false, isDeleted == false
            for (local in localEntities.filter { it.id > 0 && !it.isSynced && !it.isDeleted }) {
                transactionRemoteDataSource.updateTransaction(
                    local.id,
                    mapper.fromEntityToUpdateDto(local)
                )
                transactionDao.updateTransaction(local.copy(isSynced = true))
            }

            // 3. Для каждой локальной транзакции с isSynced=true, которой нет среди remote:
            for (local in localEntities.filter { it.isSynced && it.id !in remoteMap }) {
                if (!local.isDeleted) {
                    // Удалить локально (значит, удалена на сервере)
                    transactionDao.deleteTransactionById(local.id)
                }
            }
        }
    }
} 