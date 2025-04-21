package com.example.atubu.notifications

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.atubu.dataInterface.DataAccessObject
import kotlin.jvm.java
import android.app.PendingIntent
import android.widget.RemoteViews
import androidx.compose.material3.Button
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import java.util.Calendar
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.atubu.notifications.NotificationWorker
import java.util.concurrent.TimeUnit

public class NotificationInterface(private val context : Context) {

    private lateinit var workManager: WorkManager
    private val constraints = Constraints.Builder()
        .build()
    private val workName = "NotifWorker"

    public fun singleNotification(title: String, text: String) {

        val inputData = Data.Builder()
            .putString("Title", title)
            .putString("Text", text)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        workManager = WorkManager.getInstance(context)
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    public fun dailyNotification(title: String, text: String, hour: Int, minute: Int, second: Int) {

        val inputData = Data.Builder()
            .putString("Title", title)
            .putString("Text", text)
            .putInt("Hour", hour)
            .putInt("Minute", minute)
            .putInt("Second", second)
            .build()

        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance()// Set Execution around 05:00:00 AM
        dueDate.set(Calendar.HOUR_OF_DAY, hour)
        dueDate.set(Calendar.MINUTE, minute)
        dueDate.set(Calendar.SECOND, second)
        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }
        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        workManager = WorkManager.getInstance(context)

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
            .build()

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )
    }

    public fun scheduledelayNotifications(title: String, text: String) {


        var minHour = DataAccessObject.getDAO(context).getValue("minHour").toIntOrNull();
        if(minHour == null){
            minHour = 8;
        }
        var maxHour = DataAccessObject.getDAO(context).getValue("maxHour").toIntOrNull();
        if (maxHour == null){
            maxHour = 23;
        }
        var delay =  DataAccessObject.getDAO(context).getValue("maxHour").toIntOrNull();
        if (delay == null){
            delay = 3;
        }

        val inputData = Data.Builder()
            .putString("Title", title)
            .putString("Text", text)
            .putInt("minHour", minHour)
            .putInt("maxHour", maxHour)
            .putInt("delay", delay)
            .build()

        workManager = WorkManager.getInstance(context)

        val dailyWorkRequest = OneTimeWorkRequestBuilder<DelayNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setInitialDelay(delay.toLong(), TimeUnit.HOURS)
            .build()

        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            dailyWorkRequest
        )


    }
}
