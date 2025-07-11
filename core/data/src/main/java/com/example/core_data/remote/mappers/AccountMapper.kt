package com.example.core_data.remote.mappers

import com.example.core_data.remote.dto.AccountDto
import com.example.core_domain.models.Account
import jakarta.inject.Inject

class AccountMapper @Inject constructor() {
    fun fromDtoToAccount(dto: AccountDto) = Account(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency,
    )
}