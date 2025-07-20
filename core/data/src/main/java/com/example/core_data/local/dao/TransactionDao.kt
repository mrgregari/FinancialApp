package com.example.core_data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.core_data.local.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE transactionDate >= :startDate AND transactionDate <= :endDate")
    suspend fun getAllInPeriod(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT t.* FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE c.isIncome = 1 AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate
    """)
    suspend fun getIncomesInPeriod(startDate: String, endDate: String): List<TransactionEntity>

    @Query("""
        SELECT t.* FROM transactions t
        INNER JOIN categories c ON t.categoryId = c.id
        WHERE c.isIncome = 0 AND t.transactionDate >= :startDate AND t.transactionDate <= :endDate
    """)
    suspend fun getExpensesInPeriod(startDate: String, endDate: String): List<TransactionEntity>

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteTransactionById(id: Int)

    @Query("SELECT MIN(id) FROM transactions")
    fun getMinTransactionId(): Int?
}