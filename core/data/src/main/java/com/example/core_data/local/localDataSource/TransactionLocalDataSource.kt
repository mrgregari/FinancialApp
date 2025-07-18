package com.example.core_data.local.localDataSource

import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.dao.CategoryDao
import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.local.entity.TransactionEntity
import com.example.core_data.mappers.TransactionMapper
import com.example.core_domain.models.Expense
import com.example.core_domain.models.Income
import javax.inject.Inject

class TransactionLocalDataSource @Inject constructor(
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val mapper: TransactionMapper
) {
    suspend fun getExpensesLocal(accountId: Int, startDate: String, endDate: String): List<Expense> {
        val transactions = transactionDao.getExpensesInPeriod(startDate, endDate)
            .filter { it.accountId == accountId }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }

        return transactions.mapNotNull { entity ->
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

    suspend fun getIncomesLocal(accountId: Int, startDate: String, endDate: String): List<Income> {
        val transactions = transactionDao.getIncomesInPeriod(startDate, endDate)
            .filter { it.accountId == accountId }
        val accounts = accountDao.getAll().associateBy { it.id }
        val categories = categoryDao.getAll().associateBy { it.id }

        return transactions.mapNotNull { entity ->
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

    suspend fun getAllLocalEntities(accountId: Int?): List<TransactionEntity> {
        val all = transactionDao.getAllTransactions()
        return if (accountId == null) all else all.filter { it.accountId == accountId }
    }
}