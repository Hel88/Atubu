package com.example.atubu

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import kotlinx.coroutines.delay

class NotificationWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val notificationChannelId = "DemoNotificationChannelId"
    override suspend fun doWork(): Result{
        delay(5000) //simulate background task
        Log.d("DemoWorker", "do work done!")

        return Result.success()
    }



}