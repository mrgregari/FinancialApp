package com.example.financialapp.data.mappers

import com.example.financialapp.data.models.AccountDto
import com.example.financialapp.domain.models.Account
import javax.inject.Inject

class AccountMapper @Inject constructor() {
    fun fromDto(dto: AccountDto) = Account(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency,
    )
}