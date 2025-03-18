package com.example.atubu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.style.TextAlign
import com.example.atubu.R


class PlantScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentWaterQtt = 1.5f
        setContent{
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {

                Column (
                    verticalArrangement = Arrangement.Center
                ){
                    PlantAndWater(currentWaterQtt)
                    Instructions("Glisser et déposer pour arroser la plante (TODO)")

                    DrinkSelectionPanel()
                }

            }
        }
    }
}

@Composable
fun Plant(){
    Text(
        text = "Plant"
    )
}

@Composable
fun WaterGauge(currentWaterQtt: Float, minGoal : Float, maxGoal : Float) {
    val maxHeight = 400.dp // Hauteur max de la jauge
    val maxCapacity = 3f // Capacité maximale = 3L
    val filledHeight = maxHeight * (currentWaterQtt / maxCapacity).coerceIn(0f, 1f) // Hauteur en fonction du % d'eau bue

    val markerPositions = listOf(minGoal, maxGoal) // Marqueurs
        .map { it / maxCapacity } // Convertir en % de la jauge

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
                .background(Color.Blue, RoundedCornerShape(12.dp))
                .align(androidx.compose.ui.Alignment.BottomCenter) // L'eau monte depuis le bas
        )
        {
            // Le texte au-dessus de la box bleue
            Text(
                text = "${(currentWaterQtt * 1000).toInt()} ml",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter) // Aligne le texte en haut au centre
                    .padding(top = 2.dp) // Ajoute de l'espace entre le texte et la box bleue
            )
        }


        // marqueurs d'objetifs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val gaugeHeight = size.height
            val gaugeWidth = size.width

            markerPositions.forEach { position ->
                val y = gaugeHeight * (1 - position) // Convertir en coordonnée Y

                drawLine(
                    color = Color.Black, // Couleur des marqueurs
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(gaugeWidth, y),
                    strokeWidth = 4f
                )
            }
        }
    }
}

@Composable
fun PlantAndWater(currentWaterQtt : Float) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically, // Centre verticalement
        horizontalArrangement = Arrangement.SpaceBetween // Plante à gauche, jauge à droite
    ) {
        PlantImage();
        WaterGauge(currentWaterQtt = currentWaterQtt, minGoal = 1f , maxGoal = 2.3f) // Exemple : 75% d'eau bue
    }
}

@Composable
fun PlantImage(){
    val image = painterResource(id = R.drawable.plant)
    Image(
        painter = image,
        contentDescription = "Plant",
        modifier = Modifier
            .size(350.dp)
    )
}

@Composable
fun Instructions(text : String){
    Text(
        text = text,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(16.dp)

    )
}


@Composable
fun DrinkSelectionPanel(){
    Row (
        Modifier.padding(12.dp) ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text("TODOverr1");
        Text("verr2");
        Text("verr3")

    }


}

@Preview(showBackground = true)
@Composable
fun WaterGaugePreview() {
    PlantAndWater(1.5f)
}