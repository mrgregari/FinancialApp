package com.example.feature_account.domain

import com.example.core_domain.repositories.AccountRepository
import com.example.core_network.network.NetworkResult
import javax.inject.Inject


class SyncAccountsUseCase @Inject constructor(
    private val repository: AccountRepository
) {
    suspend operator fun invoke(

    ): NetworkResult<Unit> {
        return try {
            val remoteAccounts = repository.getAccountsFromRemote()
            val localAccounts = repository.getAccountsFromLocal()

            val localMap = localAccounts.associateBy { it.id }
            val remoteMap = remoteAccounts.associateBy { it.id }

            for ((id, remote) in remoteMap) {
                val local = localMap[id]
                if (local == null) {
                    repository.insertAccountToLocal(remote)
                } else if (remote.updatedAt > local.updatedAt) {
                    repository.insertAccountToLocal(remote)
                } else if (local.updatedAt > remote.updatedAt) {
                    repository.updateAccountRemote(local)
                }
            }

            for ((id, local) in localMap) {
                if (id !in remoteMap) {
                    repository.updateAccountRemote(local)
                }
            }

            NetworkResult.Success(Unit)
        } catch (e: Exception) {
            NetworkResult.Error(e)
        }
    }
}
