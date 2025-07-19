package com.example.core_data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.core_domain.usecases.SyncTransactionsUseCase

class TransactionSyncWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val syncTransactionsUseCase: SyncTransactionsUseCase
) : CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {
        Log.d("WORK", "doWork")

        syncTransactionsUseCase()

        return Result.success()
    }
}