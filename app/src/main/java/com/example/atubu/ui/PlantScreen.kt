package com.example.atubu.ui

import android.app.AlertDialog
import android.content.Context
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import com.example.atubu.R
import com.example.atubu.dataInterface.PreferenceHelper
import kotlin.math.min
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun PlantScreen(){

    // PARAMETRES
    val textDisplayedSetting = true
    // ----------

    var currentWaterQtt by remember { mutableIntStateOf(0) } // Quantité d'eau actuelle
    var history by remember { mutableStateOf(listOf<Int>()) }  // Liste des valeurs ajoutées


    var showDialog by remember { mutableStateOf(false) }
    var customGlassQtt by remember { mutableIntStateOf(0) }
    var glassesQuantities by remember { mutableStateOf(listOf(50, 200, 500, 100)) }

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

    fun suppressGlass(quantity : Int){
        println("suppress $quantity")
        if (quantity in glassesQuantities){
            glassesQuantities = glassesQuantities - quantity
        }else{
            println("$quantity not in list of glasses")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            val context = LocalContext.current // Récupérer le contexte dans un composable
            PlantAndWater(currentWaterQtt, PreferenceHelper.getMin(context), PreferenceHelper.getMax(context), textDisplayedSetting)
            ResetButtons({ emptyGauge() }, {revertAction()})
            }
        Column (
            modifier = Modifier.padding(bottom = 16.dp), // Ajoute un peu d'espace en bas
            verticalArrangement = Arrangement.Bottom
        ) {
            Instructions("Cliquer pour arroser la plante")
            DrinkSelectionPanel (
                glassesQuantities,
                onDropWater ={ addedWater -> addWater(addedWater)},
                showDialog ={showDialog = true},
                suppressGlass = {quantity -> suppressGlass(quantity) })
            }
        }


    if (showDialog) {
        var errorMessage by remember { mutableStateOf(false) } // Message d'erreur
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Ajouter un verre personnalisé") },
            text = {
                Column {
                    Text("Entrez la quantité en ml :")
                    OutlinedTextField(
                        value = customGlassQtt.toString(),
                        onValueChange = { text ->
                            customGlassQtt = text.toIntOrNull() ?: 0
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                    if (errorMessage) {
                        Text("Quantité invalide")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (customGlassQtt > 0 && customGlassQtt !in glassesQuantities) {
                        glassesQuantities = glassesQuantities + customGlassQtt
                        errorMessage = false
                        showDialog = false
                    }else{
                        errorMessage = true
                    }
                }) {
                    Text("Ajouter")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}





@Composable
fun PlantAndWater(currentWaterQtt: Int, minGoal: Int, maxGoal: Int, textDisplayedSetting : Boolean) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween // Plante à gauche, jauge à droite
    ) {
        PlantImage(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal)
        WaterGauge(currentWaterQtt = currentWaterQtt, minGoal = minGoal , maxGoal = maxGoal, textDisplayedSetting)
    }
}

@Composable
fun WaterGauge(currentWaterQtt: Int, minGoal: Int, maxGoal: Int, textDisplayedSetting : Boolean) {
    var color = colorResource(R.color.blue)
    if(currentWaterQtt < minGoal){
        color = colorResource(R.color.yellow_dry)
    }else if (currentWaterQtt > maxGoal){
        color = colorResource(R.color.brown_overwatered)
    }
    val maxHeight = 400.dp // Hauteur max de la jauge
    val maxCapacity = 3000 // Capacité maximale = 3L
    val filledHeight = maxHeight * (currentWaterQtt.toFloat() / maxCapacity.toFloat()).coerceIn(0f, 1f)
    val context = LocalContext.current // Récupérer le contexte dans un composable

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

            // Le texte au-dessus de la box bleue
            if (PreferenceHelper.getBool(context)){
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


        // marqueurs d'objectifs
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

                if (PreferenceHelper.getBool(context)){
                    // texte avec la quantité au dessus de la ligne
                    val textLayoutResult: TextLayoutResult =
                        textMeasurer.measure(
                            text = AnnotatedString( "${(position * maxCapacity).toInt()} mL"),
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
fun DrinkSelectionPanel( glasses: List<Int>, onDropWater: (Float) -> Unit, showDialog: ()-> Unit, suppressGlass: (Int) -> Unit){
    //val glassesQuantities = intArrayOf(50, 200, 500, 200, 100)
    var supressMode by remember { mutableStateOf(false) }

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
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){


        for (qtt in glasses){
            Box(

            ){
                GlassIcon(qtt, supressMode){ amount -> onDropWater(amount) }

                if (supressMode){

                    Button(
                        modifier = Modifier
                            .alpha(0.9f)
                            .align(Alignment.Center)
                            .width(80.dp)
                        ,
                        onClick = { suppressGlass(qtt) }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.delete_icon),
                            contentDescription = "Icone supprimer"
                        )
                    }
                }
            }

        }


        Column {

        if (!supressMode){

        Button(onClick = showDialog) { Text(text = "+", fontSize = 20.sp)}
            Button(onClick = { supressMode = true }) { Text(text = "-", fontSize = 20.sp)}
        }else{
            Button(onClick = { supressMode = false }) { Text(text = "ok")}

        }
        }

    }
}

@Composable
fun GlassIcon(qtt : Int, supressMode : Boolean, onDropWater: (Float) -> Unit){

    var image =painterResource(id = R.drawable.verre_petit)
    if (qtt >50 && qtt<=300){image =painterResource(id = R.drawable.verre)}
    if (qtt>300){image =painterResource(id = R.drawable.gourde)}

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            if (!supressMode){
                onDropWater(qtt.toFloat()) // Ajoute la quantité d'eau en cliquant
            }
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

    //PlantAndWater(9000, 1000, 2300)
    //DrinkSelectionPanel()
    WaterGauge(200, 1000,2100, true)
}