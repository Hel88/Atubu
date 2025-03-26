package com.example.atubu.ui

import android.R.attr.content
import android.R.id.content
import android.annotation.SuppressLint
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import com.example.atubu.R
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PlantScreen(
    onNextButtonClicked: () -> Unit = {},
){
    var minGoal = 1000
    var maxGoal = 2300
    val currentWaterQtt = 1.5f
    Surface (
        modifier = Modifier.fillMaxSize()
    )
    {
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val image = painterResource(id = R.drawable.friend)
        var currentWaterQtt by remember { mutableIntStateOf(0) } // Quantité d'eau actuelle
        var history by remember { mutableStateOf(listOf<Int>()) }  // Liste des valeurs ajoutées

        fun addWater(addedWater: Float) {
            currentWaterQtt = (currentWaterQtt + addedWater).toInt()
            history = history + addedWater.toInt() // ajout à l'historique
        }

        fun emptyGauge(){
            currentWaterQtt = 0
        }
        fun revertAction(){
            if (history.isNotEmpty()){
                currentWaterQtt -= history.last()
                history = history.dropLast(1)
            }
        }

        ModalNavigationDrawer(
            drawerContent = {
                DismissibleDrawerSheet {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Spacer(Modifier.height(12.dp))
                        Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)

                        NavigationDrawerItem(
                            label = { Text("Amis") },
                            selected = false,
                            icon = {Icon(
                                painter = image,
                                contentDescription = "friend",
                                modifier = Modifier.size(24.dp)
                            )},

                            onClick = { /* Handle click */ }
                        )
                        NavigationDrawerItem(
                            label = { Text("Succès") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Star, contentDescription = null) },
                            onClick = { /* Handle click */ }
                        )

                        NavigationDrawerItem(
                            label = { Text("Paramètres") },
                            selected = false,
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },

                            onClick = onNextButtonClicked
                        )

                        Spacer(Modifier.height(12.dp))
                    }
                }
            },
            drawerState = drawerState
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                // ------------------------- On insère le contenu ici pour qu'il soit visible sous le drawer
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    PlantAndWater(currentWaterQtt)
                    ResetButtons({ emptyGauge() }, {revertAction()})
                    Instructions("Glisser et déposer pour arroser la plante (TODO)")
                    DrinkSelectionPanel()

                }
            }   
        }
    }

}


@Composable
fun PlantAndWater(currentWaterQtt: Int, minGoal: Int, maxGoal: Int) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Plante à gauche, jauge à droite
    ) {
        PlantImage(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal)
        WaterGauge(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal)
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
                    start = Offset(0f, y),
                    end = Offset(gaugeWidth, y),
                    strokeWidth = 4f
                )
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
    val image = when (qtt){
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

@Composable
fun ResetButtons(empty: () -> Unit, revert : () -> Unit){
    Row (
    ){
        Button(onClick = { empty() }    ) {
            Text(text = "Vider")
        }
        Button(onClick = { revert()}    ) {
            Text(text = "Annuler")
        }
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

    PlantAndWater(9000, 1000, 2300)
    //DrinkSelectionPanel()
}