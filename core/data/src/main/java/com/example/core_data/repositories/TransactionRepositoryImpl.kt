package com.example.core_data.repositories

import android.annotation.SuppressLint
import com.example.core_data.mappers.TransactionMapper
import com.example.core_data.remote.remoteDataSource.TransactionRemoteDataSource
import com.example.core_data.di.IODispatcher
import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.dao.CategoryDao
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import com.example.core_domain.repositories.TransactionRepository
import com.example.core_network.network.NetworkResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import com.example.core_data.local.entity.TransactionEntity
import java.util.Locale
import java.util.TimeZone

class TransactionRepositoryImpl @Inject constructor(
    private val transactionRemoteDataSource: TransactionRemoteDataSource,
    private val mapper: TransactionMapper,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {

    private fun getDefaultPeriod(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()
        val endDate = calendar.time
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val startDate = calendar.time
        return startDate to endDate
    }

    @SuppressLint("SimpleDateFormat")
    private fun Date.toApiStringStartOfDay(): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun Date.toApiStringEndOfDay(): String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.time = this
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(calendar.time)
    }

    override suspend fun getExpenses(
        accountId: Int,
        startDate: Date?,
        endDate: Date?
    ): NetworkResult<List<Expense>> = withContext(ioDispatcher) {

        val (defaultStart, defaultEnd) = getDefaultPeriod()
        val start = startDate ?: defaultStart
        val end = endDate ?: defaultEnd
        println(start.toApiStringStartOfDay())
        println(end.toApiStringEndOfDay())
        try {
            val dtos = transactionRemoteDataSource.getTransactions(
                accountId = accountId,
                startDate = start.toApiStringStartOfDay(),
                endDate = end.toApiStringEndOfDay()
            )
            val remoteEntities = dtos.map { mapper.fromDtoToEntity(it) }

            val localEntities = transactionDao.getExpensesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }

            val localMap = localEntities.associateBy { it.id }

            val toUpdateLocally = mutableListOf<TransactionEntity>()
            for (remote in remoteEntities) {
                val local = localMap[remote.id]
                if (local == null || remote.updatedAt > local.updatedAt) {
                    toUpdateLocally.add(remote)
                }
            }
            if (toUpdateLocally.isNotEmpty()) {
                transactionDao.insertTransactions(toUpdateLocally)
            }

            val accounts = accountDao.getAll().associateBy { it.id }
            val categories = categoryDao.getAll().associateBy { it.id }
            val resultEntities = transactionDao.getExpensesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }

            val expenses = resultEntities.mapNotNull { entity ->
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
        } catch (e: Exception) {
            val accounts = accountDao.getAll().associateBy { it.id }
            val categories = categoryDao.getAll().associateBy { it.id }
            val resultEntities = transactionDao.getExpensesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }

            val expenses = resultEntities.mapNotNull { entity ->
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
            val dtos = transactionRemoteDataSource.getTransactions(
                accountId = accountId,
                startDate = start.toApiStringStartOfDay(),
                endDate = end.toApiStringEndOfDay()
            )
            val remoteEntities = dtos.map { mapper.fromDtoToEntity(it) }

            val localEntities = transactionDao.getIncomesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }
            val localMap = localEntities.associateBy { it.id }

            val toUpdateLocally = mutableListOf<TransactionEntity>()
            for (remote in remoteEntities) {
                val local = localMap[remote.id]
                if (local == null || remote.updatedAt > local.updatedAt) {
                    toUpdateLocally.add(remote)
                }
            }
            if (toUpdateLocally.isNotEmpty()) {
                transactionDao.insertTransactions(toUpdateLocally)
            }

            val accounts = accountDao.getAll().associateBy { it.id }
            val categories = categoryDao.getAll().associateBy { it.id }
            val resultEntities = transactionDao.getIncomesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }

            val incomes = resultEntities.mapNotNull { entity ->
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
        } catch (e: Exception) {
            val accounts = accountDao.getAll().associateBy { it.id }
            val categories = categoryDao.getAll().associateBy { it.id }
            val resultEntities = transactionDao.getIncomesInPeriod(
                start.toApiStringStartOfDay(),
                end.toApiStringEndOfDay()
            ).filter { it.accountId == accountId }

            val incomes = resultEntities.mapNotNull { entity ->
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
                NetworkResult.Error(e)
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
                NetworkResult.Error(e)
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
                NetworkResult.Error(e)
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
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun deleteTransactionById(id: Int): NetworkResult<Unit> {
        return withContext(ioDispatcher) {
            try {
                transactionRemoteDataSource.deleteTransactionById(id)
                NetworkResult.Success(Unit)
            } catch (e: Exception) {
                NetworkResult.Error(e)
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
                NetworkResult.Error(e)
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
                NetworkResult.Error(e)
            }
        }
    }

    override suspend fun getExpensesLocal(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<Expense> = withContext(ioDispatcher) {
        val transactions = transactionDao.getExpensesInPeriod(startDate, endDate)
            .filter { it.accountId == accountId }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }

        transactions.mapNotNull { entity ->
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
        }
    }

    override suspend fun getIncomesLocal(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<Income> = withContext(ioDispatcher) {
        val transactions = transactionDao.getIncomesInPeriod(startDate, endDate)
            .filter { it.accountId == accountId }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }

        transactions.mapNotNull { entity ->
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
        }
    }

}