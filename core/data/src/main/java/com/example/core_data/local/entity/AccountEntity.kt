package com.example.core_data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val updatedAt: String // для синхронизации
)