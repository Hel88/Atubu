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
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.os.Handler
import android.os.Looper
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
    override fun onStop() {
        super.onStop()
        forceWidgetUpdate()
    }

    override fun onPause() {
        super.onPause()
        forceWidgetUpdate()
    }

    override fun onResume() {
        super.onResume()
        forceWidgetUpdate()
    }

    private fun forceWidgetUpdate() {
        // 1. Mettre à jour via Glance
        MyAppWidget.updateAllWidgets(applicationContext)

        // 2. Forcer une mise à jour via AppWidgetManager (méthode traditionnelle)
        val appWidgetManager = AppWidgetManager.getInstance(applicationContext)
        val componentName = ComponentName(applicationContext, MyAppWidgetReceiver::class.java)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

        if (appWidgetIds.isNotEmpty()) {
            // Envoyer un broadcast pour forcer la mise à jour
            val intent = Intent(applicationContext, MyAppWidgetReceiver::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
            }
            applicationContext.sendBroadcast(intent)

            // 3. Forcer un rafraîchissement direct des widgets
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, android.R.id.list)
        }

        // 4. Utiliser un Handler avec délai pour s'assurer que la mise à jour est appliquée
        Handler(Looper.getMainLooper()).postDelayed({
            MyAppWidget.updateAllWidgets(applicationContext)
        }, 500) // 500ms de délai
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




