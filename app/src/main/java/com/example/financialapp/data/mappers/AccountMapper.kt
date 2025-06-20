package com.example.financialapp.data.mappers

import com.example.financialapp.data.models.AccountDTO
import com.example.financialapp.domain.models.Account
import javax.inject.Inject

class AccountMapper @Inject constructor() {
    fun fromDto(dto: AccountDTO) = Account(
        id = dto.id,
        name = dto.name,
        balance = dto.balance,
        currency = dto.currency,
    )
}