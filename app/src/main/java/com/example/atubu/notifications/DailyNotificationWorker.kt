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


class DailyNotificationWorker(private val context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

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

            with(NotificationManagerCompat.from(applicationContext)) {
                notify(0, createNotification())
            }
        }
        delay(2000)
        val hour = inputData.getInt("Hour", 12)
        val minute = inputData.getInt("Minute", 0)
        val second = inputData.getInt("Second", 0)

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()    // Set Execution around 05:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, hour)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, second)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }


        val title = inputData.getString("Title")
        val text = inputData.getString("Text")

        val inputData = Data.Builder()
            .putString("Title", title)
            .putString("Text", text)
            .putInt("Hour", hour)
            .putInt("Minute", minute)
            .putInt("Second", second)
            .build()

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
        val constraints = Constraints.Builder().build()

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .build()

        var workManager = WorkManager.getInstance(context)
        val workName = "NotifWorker"

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )

        return Result.success()

    }

}
