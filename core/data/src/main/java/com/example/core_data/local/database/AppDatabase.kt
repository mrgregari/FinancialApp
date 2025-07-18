package com.example.core_data.local.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.core_data.local.dao.AccountDao
import com.example.core_data.local.dao.CategoryDao
import com.example.core_data.local.dao.TransactionDao
import com.example.core_data.local.entity.AccountEntity
import com.example.core_data.local.entity.CategoryEntity
import com.example.core_data.local.entity.TransactionEntity

@Database(
    entities = [AccountEntity::class, CategoryEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val LOCK = Any()

        private const val DB_NAME = "finance.db"

        fun getInstance(context: Context): AppDatabase {
            INSTANCE?.let {
                return it
            }
            synchronized(LOCK) {
                INSTANCE?.let {
                    return it
                }
            }
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DB_NAME
            )
                .build()
            INSTANCE = db
            return db
        }


    }
}