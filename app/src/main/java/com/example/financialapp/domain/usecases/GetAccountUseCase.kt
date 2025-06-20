package com.example.financialapp.domain.usecases

import com.example.financialapp.domain.models.Account
import com.example.financialapp.domain.repositories.AccountRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: AccountRepository
) {

    suspend operator fun invoke() : Result<List<Account>> {
        return repository.getAllAccounts()
    }
}