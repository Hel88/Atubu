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
import com.example.atubu.theme.AtubuTheme
import kotlin.jvm.java
import android.app.PendingIntent
import android.widget.RemoteViews
import androidx.compose.material3.Button
import androidx.core.app.NotificationManagerCompat
import com.example.atubu.theme.AtubuTheme
import com.example.atubu.notifications.NotificationInterface


class MainActivity : AppCompatActivity() {


    private lateinit var dataAccessObject: DataAccessObject
    private lateinit var helloView: TextView

    private lateinit var notifInterface : NotificationInterface

//    private val channelId = "i.apps.notifications" // Unique channel ID for notifications
//    private val description = "Test notification"  // Description for the notification channel
//    private val notificationId = 1234 // Unique identifier for the notification







    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AtubuTheme {
                AtubuApp(DataAccessObject.getDAO(this))
            }
        }
        notifInterface = NotificationInterface(applicationContext)
        notifInterface.singleNotification("Hydrates-toi !","N'oublie pas de boire de l'eau.")






//        dataAccessObject = DataAccessObject(applicationContext)
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
//            .setContentTitle("Hydrates-toi !")
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




