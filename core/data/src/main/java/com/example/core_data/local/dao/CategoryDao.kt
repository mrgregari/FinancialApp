package com.example.core_data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core_data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories")
    suspend fun getAll() : List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE isIncome = 1")
    suspend fun getIncomes(): List<CategoryEntity>

    @Query("SELECT * FROM categories WHERE isIncome = 0")
    suspend fun getExpenses(): List<CategoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<CategoryEntity>)
}