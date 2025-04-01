package com.example.atubu

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
import com.example.atubu.theme.CupcakeTheme
import kotlin.jvm.java
import android.app.PendingIntent
import android.widget.RemoteViews
import androidx.compose.material3.Button
import androidx.core.app.NotificationManagerCompat
import android.Manifest
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.atubu.notifications.NotificationWorker
import com.example.atubu.ui.permission.RuntimePermissionsDialog

class MainActivity : AppCompatActivity() {


    private lateinit var dataAccessObject: DataAccessObject
    private lateinit var helloView: TextView

//    private val channelId = "i.apps.notifications" // Unique channel ID for notifications
//    private val description = "Test notification"  // Description for the notification channel
//    private val notificationId = 1234 // Unique identifier for the notification



    private lateinit var workManager: WorkManager
    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()
    private val workName = "NotifWorker"



    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        WorkManager.getInstance(applicationContext)
        setContent {
            CupcakeTheme {

                AtubuApp()
            }
        }



       val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setConstraints(constraints)
            .build()

        workManager = WorkManager.getInstance(applicationContext)
        workManager.enqueueUniqueWork(
            workName,
            ExistingWorkPolicy.REPLACE,
            workRequest)



//        dataAccessObject = DataAccessObject(applicationContext)
//        helloView = findViewById(R.id.HelloDisplay)
//        helloView.text = dataAccessObject.incrementTest()

    }




//
//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "notif_channel_id",
//                "Nom du canal",
//                NotificationManager.IMPORTANCE_DEFAULT
//            ).apply {
//                description = "Canal utilisé pour envoyer des notifications"
//            }
//
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
//
//    /**
//     * Build and send a notification with a custom layout and action.
//     */
//    @SuppressLint("MissingPermission")
//    private fun sendNotification() {
//
//        // Build the notification
//        val builder = NotificationCompat.Builder(this, "notif_channel_id")
//            .setSmallIcon(R.drawable.ic_launcher_foreground) // Remplace avec ton icône
//            .setContentTitle("Hydrate-toi !")
//            .setContentText("N'oublie pas de boire de l'eau.")
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//            .setAutoCancel(true)
//
//        // Display the notification
//        with(NotificationManagerCompat.from(this)) {
//            notify(notificationId, builder.build())
//        }
//    }
}




