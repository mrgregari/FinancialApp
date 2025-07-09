package com.example.core_data.data.mappers

import com.example.core_data.data.dto.AccountDto
import jakarta.inject.Inject
import com.example.core_data.domain.models.Account

class AccountMapper @Inject constructor() {
    fun fromDtoToAccount(dto: AccountDto) = Account(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency,
    )
}