package com.example.atubu.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import com.example.atubu.R
import kotlin.math.roundToInt


class PlantScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            var currentWaterQtt by remember { mutableIntStateOf(0) }
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {

                Column (
                    verticalArrangement = Arrangement.Center

                ){

                    PlantAndWater(currentWaterQtt)
                    Instructions("Glisser et déposer pour arroser la plante (TODO)")

                    DrinkSelectionPanel { addedWater ->
                        currentWaterQtt = (currentWaterQtt+addedWater).toInt() // Ajoute la quantité d'eau à la jauge
                    }

                }

            }
        }
    }
}

@Composable
fun PlantAndWater(currentWaterQtt: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically, // Centre verticalement
        horizontalArrangement = Arrangement.SpaceBetween // Plante à gauche, jauge à droite
    ) {
        PlantImage()
        WaterGauge(currentWaterQtt = currentWaterQtt, minGoal = 1000 , maxGoal = 2300) // Exemple : 75% d'eau bue
    }
}

@Composable
fun WaterGauge(currentWaterQtt: Int, minGoal: Int, maxGoal: Int) {
    val maxHeight = 400.dp // Hauteur max de la jauge
    val maxCapacity = 3000 // Capacité maximale = 3L
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
                .background(Color.Blue, RoundedCornerShape(12.dp))
                .align(Alignment.BottomCenter) // L'eau monte depuis le bas
        )
        {
            // Le texte au-dessus de la box bleue
            Text(
                text = "${(currentWaterQtt)} ml",
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter) // Aligne le texte en haut au centre
                    .padding(top = 2.dp) // Ajoute de l'espace entre le texte et la box bleue
            )
        }


        // marqueurs d'objectifs
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
fun DrinkSelectionPanel(onDropWater: (Float) -> Unit){
    val glassesQuantities = intArrayOf(50, 200, 300, 200, 100)

    Row (
        Modifier
            //.padding(30.dp)
            .fillMaxWidth()
            .horizontalScroll(
                state = rememberScrollState(),
                enabled = true
            )
            //.height(30.dp)
                ,
        horizontalArrangement = Arrangement.SpaceBetween
    ){


        for (qtt in glassesQuantities){
            GlassIcon(qtt){ amount -> onDropWater(amount) }
        }
    }
}

@Composable
fun GlassIcon(qtt : Int, onDropWater: (Float) -> Unit){
    var image = when (qtt){
        // A MODIFIER POUR DES INTERVALLES
        50 -> painterResource(id = R.drawable.verre_petit)
        200 -> painterResource(id = R.drawable.verre)
        300 -> painterResource(id = R.drawable.gourde)
        else -> painterResource(id = R.drawable.verre)
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onDropWater(qtt.toFloat()) // Ajoute la quantité d'eau en cliquant
        }
    ){
        Image(
            painter = image,
            contentDescription = "",
            modifier = Modifier
                .size(100.dp)
        )
        Text(
            text = "$qtt",
            textAlign = TextAlign.Center
        )
    }
}



//--------------------------------chatgpt drag and drop example-------------------------------------------
@Composable
fun DraggableBox() {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(100.dp)
            .background(Color.Blue, shape = RoundedCornerShape(8.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume() // Consommer l'événement pour éviter le conflit avec d'autres gestes
                    offset += dragAmount
                }
            }
    )
}

@Composable
fun DragAndDropScreen() {
    var boxOffset by remember { mutableStateOf(Offset.Zero) }
    val dropZone = remember { mutableStateOf(Rect.Zero) }
    var isDropped by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Zone de Drop
        Box(
            modifier = Modifier
                .size(150.dp)
                .background(if (isDropped) Color.Green else Color.Gray, RoundedCornerShape(8.dp))
                .onGloballyPositioned { coordinates ->
                    dropZone.value = coordinates.boundsInParent()
                }
        )

        // Élement Déplaçable
        Box(
            modifier = Modifier
                .offset { IntOffset(boxOffset.x.roundToInt(), boxOffset.y.roundToInt()) }
                .size(100.dp)
                .background(Color.Blue, shape = RoundedCornerShape(8.dp))
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            isDropped = dropZone.value.contains(boxOffset)
                        }
                    ) { change, dragAmount ->
                        change.consume()
                        boxOffset += dragAmount
                    }
                }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun Glasses() {

    PlantAndWater(1500)
    //DrinkSelectionPanel()
}