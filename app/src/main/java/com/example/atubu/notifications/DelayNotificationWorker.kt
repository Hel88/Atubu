package com.example.atubu.notifications

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings.System.getString
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.atubu.MainActivity
import android.Manifest
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
//import com.example.atubu.Manifest
import kotlinx.coroutines.delay
import kotlin.properties.Delegates
import com.example.atubu.R
import java.util.Calendar
import java.util.concurrent.TimeUnit


class DelayNotificationWorker(private val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    private val notificationChannelId = "NotificationWorkerChannelId"

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(notificationChannelId, "NotificationWorker",
                NotificationManager.IMPORTANCE_DEFAULT)

            // Register the channel with the system.

            val notificationManager: NotificationManager =
                getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification() : Notification {
        createNotificationChannel();

        val title = inputData.getString("Title")
        val text = inputData.getString("Text")

        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java)

        var pendingIntentFlag by Delegates.notNull<Int>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntentFlag = PendingIntent.FLAG_IMMUTABLE
        } else {
            pendingIntentFlag = PendingIntent.FLAG_UPDATE_CURRENT
        }
        val mainActivityPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            mainActivityIntent,
            pendingIntentFlag
        )

        return NotificationCompat.Builder(applicationContext,
            notificationChannelId).setSmallIcon(R.drawable.goute)
            .setContentTitle(title)//applicationContext.getString(R.string.app_name))
            .setContentText(text)
            .build()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            0, createNotification()
        )
    }



    override suspend fun doWork(): Result {
        if(ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED

        ){


            val minHour = inputData.getInt("minHour", 22)

            val maxHour = inputData.getInt("maxHour", 8)

            val currentDate = Calendar.getInstance()
            val currenthour = currentDate.get(Calendar.HOUR_OF_DAY)
            if (minHour <= currenthour && maxHour >= currenthour) {
                with(NotificationManagerCompat.from(applicationContext)) {
                    notify(0, createNotification())
                }
            }
        }
        delay(2000)
        val minHour = inputData.getInt("minHour", 22)
        val maxHour = inputData.getInt("maxHour", 8)
        val delay = inputData.getInt("delay", 0)


        val title = inputData.getString("Title")
        val text = inputData.getString("Text")

        val inputData = Data.Builder()
            .putString("Title", title)
            .putString("Text", text)
            .putInt("minHour", minHour)
            .putInt("maxHour", maxHour)
            .putInt("delay", delay)
            .build()

        val constraints = Constraints.Builder().build()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DelayNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(delay.toLong(), TimeUnit.HOURS)
            .build()

        var workManager = WorkManager.getInstance(context)
        val workName = "NotifDelay"

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )

        return Result.success()

    }

}
