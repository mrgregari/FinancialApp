package com.example.feature_account.domain


import com.example.core_data.domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
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
