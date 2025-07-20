package com.example.core_data.repositories

import com.example.core_data.di.IODispatcher
import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.dao.CategoryDao
import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.local.entity.TransactionEntity
import com.example.core_data.mappers.TransactionMapper
import com.example.core_data.remote.remoteDataSource.TransactionRemoteDataSource
import com.example.core_data.utils.toLocalApiStringEndOfDay
import com.example.core_data.utils.toLocalApiStringStartOfDay
import com.example.core_domain.models.Account
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteDataSource: TransactionRemoteDataSource,
    private val mapper: TransactionMapper,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
    private val transactionSyncManager: TransactionSyncManager
) : TransactionRepository {

    private fun getDefaultPeriod(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = calendar.time
        return startDate to endDate
    }


    override suspend fun getExpenses(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): NetworkResult<List<Expense>> = withContext(ioDispatcher) {

        val (defaultStart, defaultEnd) = getDefaultPeriod()
        val start = startDate ?: defaultStart
        val end = endDate ?: defaultEnd
        try {
            syncTransactionsWithRemote(listOf(accountDao.getAll().first { it.id == accountId }.let {
                Account(
                    id = it.id,
                    name = it.name,
                    balance = it.balance,
                    currency = it.currency,
                    updatedAt = it.updatedAt
                )
            }))
        } catch (e: Exception) {
        }
        val transactions = transactionDao.getExpensesInPeriod(
            start.toLocalApiStringStartOfDay(),
            end.toLocalApiStringEndOfDay()
        )
            .filter { it.accountId == accountId && !it.isDeleted }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }
        val expenses = transactions.mapNotNull { entity ->
            val account = accounts[entity.accountId]
            val category = categories[entity.categoryId]
            if (account != null && category != null && !category.isIncome) {
                mapper.fromEntityToExpense(
                    entity,
                    accountName = account.name,
                    currency = account.currency,
                    categoryName = category.name,
                    icon = category.icon
                )
            } else null
        }.sortedByDescending { it.date }
        NetworkResult.Success(expenses)

    }

    override suspend fun getIncomes(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): NetworkResult<List<Income>> = withContext(ioDispatcher) {
        val (defaultStart, defaultEnd) = getDefaultPeriod()
        val start = startDate ?: defaultStart
        val end = endDate ?: defaultEnd
        try {
            syncTransactionsWithRemote(listOf(accountDao.getAll().first { it.id == accountId }.let {
                Account(
                    id = it.id,
                    name = it.name,
                    balance = it.balance,
                    currency = it.currency,
                    updatedAt = it.updatedAt
                )
            }))
        } catch (e: Exception) {
        }
        val transactions = transactionDao.getIncomesInPeriod(
            start.toLocalApiStringStartOfDay(),
            end.toLocalApiStringEndOfDay()
        ).filter { it.accountId == accountId && !it.isDeleted }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }
        val incomes = transactions.mapNotNull { entity ->
            val account = accounts[entity.accountId]
            val category = categories[entity.categoryId]
            if (account != null && category != null && category.isIncome) {
                mapper.fromEntityToIncome(
                    entity,
                    accountName = account.name,
                    currency = account.currency,
                    categoryName = category.name,
                    icon = category.icon
                )
            } else null
        }.sortedByDescending { it.date }
        NetworkResult.Success(incomes)
    }

    override suspend fun addExpense(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromExpenseToCreateDto(expense, accountId, categoryId)
                val response = transactionRemoteDataSource.postTransaction(dto)
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(Exception("Ошибка при добавлении транзакции: ${response.code()}"))
                }
            } catch (e: Exception) {
                val minId = transactionDao.getMinTransactionId() ?: 0
                val newId = if (minId < 0) minId - 1 else -1
                transactionDao.insertTransaction(
                    TransactionEntity(
                        id = newId,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = expense.amount,
                        transactionDate = expense.date,
                        comment = expense.comment,
                        updatedAt = expense.updatedAt
                    )
                )
                NetworkResult.Success(Unit)
                //NetworkResult.Error(e)
            }
        }
    }

    override suspend fun addIncome(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromIncomeToCreateDto(income, accountId, categoryId)
                val response = transactionRemoteDataSource.postTransaction(dto)
                if (response.isSuccessful) {
                    NetworkResult.Success(Unit)
                } else {
                    NetworkResult.Error(Exception("Ошибка при добавлении транзакции: ${response.code()}"))
                }
            } catch (e: Exception) {
                val minId = transactionDao.getMinTransactionId() ?: 0
                val newId = if (minId < 0) minId - 1 else -1
                transactionDao.insertTransaction(
                    TransactionEntity(
                        id = newId,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = income.amount,
                        transactionDate = income.date,
                        comment = income.comment,
                        updatedAt = income.updatedAt
                    )
                )
                NetworkResult.Success(Unit)
            }
        }
    }

    override suspend fun updateExpense(
        expense: Expense,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromExpenseToUpdateDto(expense, accountId, categoryId)
                transactionRemoteDataSource.updateTransaction(expense.id, dto)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                transactionDao.updateTransaction(
                    TransactionEntity(
                        id = expense.id,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = expense.amount,
                        transactionDate = expense.date,
                        comment = expense.comment,
                        updatedAt = expense.updatedAt
                    )
                )
                NetworkResult.Success(Unit)
            }
        }
    }

    override suspend fun getExpenseById(id: Int): NetworkResult<Expense> {
        return withContext(ioDispatcher) {
            try {
                val dto = transactionRemoteDataSource.getTransactionById(
                    transactionId = id
                )
                val result = mapper.fromDtoToExpense(dto)
                NetworkResult.Success(result)
            } catch (e: Exception) {
                val transaction = transactionDao.getTransactionById(id)
                val account =
                    accountDao.getAll().firstOrNull({ it.id == transaction!!.accountId })!!
                val category =
                    categoryDao.getAll().firstOrNull({ it.id == transaction!!.categoryId })!!
                val result = mapper.fromEntityToExpense(
                    transaction!!,
                    accountName = account.name,
                    currency = account.currency,
                    categoryName = category.name,
                    icon = category.icon
                )
                NetworkResult.Success(result)
            }
        }
    }

    override suspend fun deleteTransactionById(id: Int): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                transactionRemoteDataSource.deleteTransactionById(id)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                val transaction = transactionDao.getTransactionById(id)
                transactionDao.updateTransaction(transaction!!.copy(isDeleted = true))
                NetworkResult.Success(Unit)
            }
        }
    }

    override suspend fun getIncomeById(id: Int): NetworkResult<Income> {
        return withContext(ioDispatcher) {
            try {
                val dto = transactionRemoteDataSource.getTransactionById(
                    transactionId = id
                )
                val result = mapper.fromDtoToIncome(dto)
                NetworkResult.Success(result)
            } catch (e: Exception) {
                val transaction = transactionDao.getTransactionById(id)
                val account =
                    accountDao.getAll().firstOrNull({ it.id == transaction!!.accountId })!!
                val category =
                    categoryDao.getAll().firstOrNull({ it.id == transaction!!.categoryId })!!
                val result = mapper.fromEntityToIncome(
                    transaction!!,
                    accountName = account.name,
                    currency = account.currency,
                    categoryName = category.name,
                    icon = category.icon
                )
                NetworkResult.Success(result)
            }
        }
    }

    override suspend fun updateIncome(
        income: Income,
        accountId: Int,
        categoryId: Int
    ): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                val dto = mapper.fromIncomeToUpdateDto(income, accountId, categoryId)
                transactionRemoteDataSource.updateTransaction(income.id, dto)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                transactionDao.updateTransaction(
                    TransactionEntity(
                        id = income.id,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = income.amount,
                        transactionDate = income.date,
                        comment = income.comment,
                        updatedAt = income.updatedAt
                    )
                )
                NetworkResult.Success(Unit)
            }
        }
    }


    override suspend fun syncTransactionsWithRemote(accounts: List<Account>): NetworkResult<Unit> =
        withContext(ioDispatcher) {
            try {
                println("Sync")
                transactionSyncManager.syncTransactionsWithRemote(accounts)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                NetworkResult.Error(e)
            }
        }
}