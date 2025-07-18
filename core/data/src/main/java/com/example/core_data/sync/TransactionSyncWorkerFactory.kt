package com.example.core_data.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.core_domain.usecases.SyncTransactionsUseCase
import javax.inject.Inject

class TransactionSyncWorkerFactory @Inject constructor(
    private val syncTransactionsUseCase: SyncTransactionsUseCase
): WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker {
        return TransactionSyncWorker(
            appContext,
            workerParameters,
            syncTransactionsUseCase
        )
    }
}
