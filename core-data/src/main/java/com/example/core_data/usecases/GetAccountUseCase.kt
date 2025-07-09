package com.example.core_data.usecases

import com.example.core_data.domain.models.Account
import com.example.core_data.domain.repositories.AccountRepository
import com.example.core_data.network.NetworkResult
import jakarta.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Account>> {
        return repository.getAccounts()
    }
}