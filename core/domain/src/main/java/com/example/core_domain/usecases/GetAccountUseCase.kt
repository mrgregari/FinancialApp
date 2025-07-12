package com.example.core_domain.usecases

import com.example.core_domain.models.Account
import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import jakarta.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(): NetworkResult<List<Account>> {
        return repository.getAccounts()
    }
}