package com.example.atubu

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.updateAll
import com.example.atubu.dataInterface.DataAccessObject
import com.example.atubu.dataInterface.PreferenceHelper
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MyAppWidget: GlanceAppWidget() {


    companion object {
        private val coroutineScope = MainScope()

        fun updateAllWidgets(context: Context) {
            coroutineScope.launch {
                try {
                    // Forcer une mise à jour de toutes les instances
                    MyAppWidget().updateAll(context)

                    // Envoyer un broadcast supplémentaire pour s'assurer que la mise à jour est appliquée
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val componentName = ComponentName(context, MyAppWidgetReceiver::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)

                    if (appWidgetIds.isNotEmpty()) {
                        val intent = Intent(context, MyAppWidgetReceiver::class.java).apply {
                            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
                        }
                        context.sendBroadcast(intent)
                    }
                } catch (e: Exception) {
                    Log.e("MyAppWidget", "Error updating widgets: ${e.message}")
                }
            }
        }
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // Forcer une récupération fraîche des données
        val WATER_QUANTITY_KEY = "water_quantity"

        // Récupérer les données directement (sans cache)
        val dao = DataAccessObject.getDAO(context)
        val currentWaterQtt = (dao.getValue(WATER_QUANTITY_KEY) as? String)?.toIntOrNull() ?: 0
        val minGoal = PreferenceHelper.getMin(context)
        val maxGoal = PreferenceHelper.getMax(context)

        // Ajouter un log pour déboguer
        Log.d("MyAppWidget", "Updating widget with water quantity: $currentWaterQtt")

        provideContent {
            MyContent(currentWaterQtt, minGoal, maxGoal)
        }
    }


    @Composable
    private fun MyContent(currentWaterQtt: Int, minGoal: Int, maxGoal: Int) {

        if(currentWaterQtt < minGoal) {
            Image(
                provider = ImageProvider(R.drawable.plant_dry), // Ton image ici
                contentDescription = "Une plante",
            )
        }
        else if (currentWaterQtt > maxGoal){

            Image(
                provider = ImageProvider(R.drawable.plant_overwatered), // Ton image ici
                contentDescription = "Une plante",
            )

        }
        else{
            Image(
                provider = ImageProvider(R.drawable.plant_healthy), // Ton image ici
                contentDescription = "Une plante",
            )
        }

    }
}


