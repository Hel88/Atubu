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
import androidx.glance.appwidget.GlanceAppWidgetManager
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
            Log.d("DEBUG","Lancement coroutine")
            coroutineScope.launch {
                try {
                    Log.d("DEBUG", "Maj dans coroutine")

                    // Get all GlanceIds for your widget
                    val glanceIds = GlanceAppWidgetManager(context).getGlanceIds(MyAppWidget::class.java)
                    Log.d("DEBUG", "Found ${glanceIds.size} glance IDs")

                    // Update each widget instance individually
                    glanceIds.forEach { glanceId ->
                        Log.d("DEBUG", "Updating widget ID: $glanceId")
                        MyAppWidget().update(context, glanceId)
                    }

                    Log.d("DEBUG", "Après update ALL")

                    // Your existing broadcast code...
                } catch (e: Exception) {
                    Log.e("MyAppWidget", "Error updating widgets: ${e.message}")
                    e.printStackTrace() // Add stack trace for more details
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


