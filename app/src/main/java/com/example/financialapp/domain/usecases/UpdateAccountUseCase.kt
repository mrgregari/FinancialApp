package com.example.financialapp.domain.usecases

import com.example.financialapp.data.network.NetworkResult
import com.example.financialapp.domain.repositories.AccountRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(
        id: Int,
        name: String,
        balance: String,
        currency: String
    ): NetworkResult<Unit> {
        return repository.updateAccount(id, name, balance, currency)
    }
}
