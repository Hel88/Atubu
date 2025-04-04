package com.example.atubu.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atubu.R

@Composable
fun PlantAndWater(currentWaterQtt: Int, minGoal: Int, maxGoal: Int, textDisplayedSetting : Boolean, metricSystem : Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Plante à gauche, jauge à droite
    ) {
        PlantImage(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal)
        WaterGauge(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal, textDisplayedSetting, metricSystem)
    }
}

@Composable
fun WaterGauge(currentWaterQtt: Int, minGoal: Int, maxGoal: Int, textDisplayedSetting : Boolean, metricSystem : Boolean) {

    // Couleur de la jauge
    var color = colorResource(R.color.blue)
    if(currentWaterQtt < minGoal){
        color = colorResource(R.color.yellow_dry)
    }else if (currentWaterQtt > maxGoal){
        color = colorResource(R.color.brown_overwatered)
    }


    val maxHeight = 400.dp // Hauteur max de la jauge
    var maxCapacity = 3000 // Capacité maximale = 3L
    if (maxGoal > maxCapacity) {
        maxCapacity = maxGoal + 100
    }
    val filledHeight = maxHeight * (currentWaterQtt.toFloat() / maxCapacity.toFloat()).coerceIn(0f, 1f)

    val markerPositions = listOf(minGoal, maxGoal) // Marqueurs
        .map { it.toFloat() / maxCapacity.toFloat() } // Convertir en % de la jauge

    Box(
        modifier = Modifier
            .width(40.dp)
            .height(maxHeight)
            .background(Color.Gray, RoundedCornerShape(12.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(filledHeight) // La jauge se remplit selon "progress"
                .background(color, RoundedCornerShape(12.dp))
                .align(Alignment.BottomCenter) // L'eau monte depuis le bas
        )
        {
            // Texte pour la quantité d'eau bue
            if (textDisplayedSetting){
                Text(
                    text = "${(currentWaterQtt)}",
                    color = Color.White,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.TopCenter) // Aligne le texte en haut au centre
                        .padding(top = 2.dp) // Ajoute de l'espace entre le texte et la box bleue
                )

            }
        }


        // quantités d'objectif min/max
        val textMeasurer = rememberTextMeasurer()
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gaugeHeight = size.height
            val gaugeWidth = size.width

            markerPositions.forEach { position ->
                val y = gaugeHeight * (1 - position) // Convertir en coordonnée Y

                drawLine(
                    color = Color.DarkGray, // Couleur des marqueurs
                    start = Offset(0f, y),
                    end = Offset(gaugeWidth, y),
                    strokeWidth = 4f
                )
                // texte avec la quantité au dessus de la ligne
                if (textDisplayedSetting){

                    // Système impérial
                    var qttText = AnnotatedString( "${(position * maxCapacity/1000).toFloat()} L")
                    if (!metricSystem){

                        qttText = AnnotatedString( "%.2f gal Imp".format((position * maxCapacity/4546).toFloat()))
                    }

                    val textLayoutResult: TextLayoutResult =
                        textMeasurer.measure(
                            text = qttText,
                            style = TextStyle(fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                        )
                    val textOffsetX = 5.0f   // à gauche
                    val textOffsetY = y - 30  // Décaler légèrement au-dessus de la ligne
                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(textOffsetX, textOffsetY)
                    )
                }
            }
        }
    }
}




@Composable
fun PlantImage(currentWaterQtt: Int, minGoal: Int, maxGoal: Int){
    var image = painterResource(id = R.drawable.plant_healthy)
    if (currentWaterQtt < minGoal){
        image = painterResource(id = R.drawable.plant_dry)
    }else if (currentWaterQtt > maxGoal){
        image = painterResource(id = R.drawable.plant_overwatered)
    }
    Image(
        painter = image,
        contentDescription = "Plant",
        modifier = Modifier
            .size(250.dp)
    )
}