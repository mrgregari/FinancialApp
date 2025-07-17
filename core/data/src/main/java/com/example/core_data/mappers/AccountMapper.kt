package com.example.core_data.mappers

import com.example.core_data.local.entity.AccountEntity
import com.example.core_data.remote.dto.AccountDto
import com.example.core_domain.models.Account
import jakarta.inject.Inject

class AccountMapper @Inject constructor() {
    fun fromDtoToAccount(dto: AccountDto) = Account(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency
    )

    fun fromEntityToDomain(entity: AccountEntity) = Account(
        id = entity.id,
        name = entity.name,
        balance = entity.balance,
        currency = entity.currency
    )

    fun fromDomainToEntity(domain: Account, time: String) = AccountEntity(
        id = domain.id,
        name = domain.name,
        balance = domain.balance,
        currency = domain.currency,
        updatedAt = time
    )

    fun fromDtoToEntity(dto: AccountDto) = AccountEntity(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency,
        updatedAt = dto.updatedAt
    )
}