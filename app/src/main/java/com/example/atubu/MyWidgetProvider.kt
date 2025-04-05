package com.example.atubu

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.updateAll
import com.example.atubu.MyAppWidget
import com.example.atubu.dataInterface.DataAccessObject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyAppWidget()


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Forcer une mise à jour supplémentaire via Glance
        MainScope().launch {
            glanceAppWidget.updateAll(context)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        // Réagir à d'autres actions si nécessaire
        if (intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            MainScope().launch {
                glanceAppWidget.updateAll(context)
            }
        }
    }
}
