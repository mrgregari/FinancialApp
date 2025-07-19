package com.example.core_data.sync

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class SyncReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("ALARM", "alarm")

        context?.let {
            val workRequest = OneTimeWorkRequestBuilder<TransactionSyncWorker>().build()
            WorkManager.getInstance(it).enqueue(workRequest)
        }

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SyncReceiver::class.java)
        }
    }
}
