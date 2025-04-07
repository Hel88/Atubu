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
import com.example.atubu.MyAppWidget
import com.example.atubu.R
import com.example.atubu.dataInterface.DataAccessObject
import com.example.atubu.dataInterface.Day
import com.example.atubu.dataInterface.PreferenceHelper
import com.example.atubu.ui.components.AddWaterDialog
import com.example.atubu.ui.components.DrinkSelectionPanel
import com.example.atubu.ui.components.PlantAndWater
import kotlin.math.min
import kotlinx.coroutines.launch
import java.sql.Date
import kotlin.math.roundToInt

@Composable
fun PlantScreen(){


    // DONNEES DE LA DB
    val context = LocalContext.current // accès DB
    val minGoal = PreferenceHelper.getMin(context) // Quantité min jauge
    val maxGoal = PreferenceHelper.getMax(context) // Quantité max jauge
    val textDisplayedSetting = PreferenceHelper.getBool(context) // Paramètre texte affiché
    val isMetricSystem = PreferenceHelper.getSys(context) // Paramètre système métrique

    val WATER_QUANTITY_KEY = "water_quantity"

    fun InitWater() : Int{
        var str = DataAccessObject.getDAO(context).getValue(WATER_QUANTITY_KEY)
        if (str == "Not found") {
            str = "0"
        }
        return str.toInt()
    }

    // Quantité d'eau consommée
    var currentWaterQtt by remember { mutableIntStateOf(InitWater()) } // Quantité d'eau actuelle.
    var history by remember { mutableStateOf(listOf<Int>()) }  // Liste des valeurs ajoutées

    // Verres
    val GLASS_QUANTITY_KEY = "glass_quantity"

    fun InitGlasses() : List<Int>{
        var str = DataAccessObject.getDAO(context).getValue(GLASS_QUANTITY_KEY)
        if (str == "Not found") {
            str = "[50, 200, 500]"
        }
        val glassesQuantities = str.trim('[', ']').split(",").map { it.trim().toInt() }
        return glassesQuantities
    }


    var glassesQuantities  by remember { mutableStateOf(InitGlasses())}//listOf(50, 200, 500)) } // quantités des verres.
    var showDialog by remember { mutableStateOf(false) } // Pop up créer verre custom
    var errorMessage by remember { mutableStateOf(false) } // Message d'erreur pop up



    // -------- FONCTIONS QUI ACCEDENT A LA DB------------------------------------


    // modification du niveau d'eau de la jauge
    fun setCurrentWater(newQtt : Int){
        currentWaterQtt = newQtt
        // update DB
        DataAccessObject.getDAO(context).saveValue(WATER_QUANTITY_KEY,newQtt.toString())
        val MILLISEC_IN_DAY = 24*60*60*1000
        val currentMillis = System.currentTimeMillis()
        val beginDay : Date = Date(currentMillis - (currentMillis%MILLISEC_IN_DAY) +MILLISEC_IN_DAY)
        DataAccessObject.getDAO(context).insertDay(Day(beginDay,newQtt.toFloat(),0))
    }

    // Création verre qtt custom
    fun addGlass(quantity : Int){
        if (quantity in glassesQuantities) {// message d'erreur si cette quantité d'eau existe deja
            errorMessage = true
            return
        }
        glassesQuantities = glassesQuantities + quantity

        var value = glassesQuantities.toString()
        DataAccessObject.getDAO(context).saveValue(GLASS_QUANTITY_KEY,value)

        showDialog = false
    }

    // Suppression verre qtt
    fun suppressGlass(quantity : Int){
        if (quantity !in glassesQuantities) return // si ce verre n'est pas dans la liste
        glassesQuantities = glassesQuantities - quantity
        var value = glassesQuantities.toString()
        DataAccessObject.getDAO(context).saveValue(GLASS_QUANTITY_KEY,value)
    }

    // --------------------------------------------------------------------------



    fun addWater(addedWater: Float) {
        setCurrentWater((currentWaterQtt + addedWater).toInt())
        history = history + addedWater.toInt() // ajout à l'historique
        // Mettre à jour le widget
        MyAppWidget.updateAllWidgets(context)
    }

    fun revertAction(){
        if (history.isNotEmpty()){
            setCurrentWater(currentWaterQtt - history.last())
            history = history.dropLast(1)
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

            PlantAndWater(currentWaterQtt, minGoal, maxGoal, textDisplayedSetting, isMetricSystem)
            ResetButtons(
                empty = { setCurrentWater((0))},
                revert = {revertAction()})
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
                suppressGlass = {quantity -> suppressGlass(quantity) },
                isMetricSystem= isMetricSystem)
            }
        }


    if (showDialog) {
        AddWaterDialog(
            showDialog = showDialog,
            onDismiss = {showDialog = false},
            isMetricSystem = isMetricSystem,
            onAddCustomGlass = { quantity -> addGlass(quantity)})
    }
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
fun ResetButtons(empty: () -> Unit, revert : () -> Unit){
    val context = LocalContext.current // accès DB
    Row (
    ){
        Button(onClick = { empty() // Mettre à jour le widget
            MyAppWidget.updateAllWidgets(context)}) {
            Text(text = "Vider")
        }
        Button(onClick = { revert()
            MyAppWidget.updateAllWidgets(context)
        }) {
            Text(text = "Annuler")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Glasses() {

    PlantAndWater(9000, 1000, 2300, true, true)
    //DrinkSelectionPanel()
}