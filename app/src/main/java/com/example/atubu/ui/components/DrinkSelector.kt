package com.example.atubu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.atubu.R



@Composable
fun DrinkSelectionPanel( glasses: List<Int>, onDropWater: (Float) -> Unit, showDialog: ()-> Unit, suppressGlass: (Int) -> Unit, isMetricSystem : Boolean){

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
                GlassIcon(
                    qtt = qtt,
                    supressMode = supressMode,
                    isMetricSystem = isMetricSystem,
                    ){ amount -> onDropWater(amount) }

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

                Button(onClick = showDialog) { Text(text = "+", fontSize = 20.sp) }
                Button(onClick = { supressMode = true }) { Text(text = "-", fontSize = 20.sp) }
            }else{
                Button(onClick = { supressMode = false }) { Text(text = "ok") }

            }
        }

    }
}

@Composable
fun AddWaterDialog(
    showDialog: Boolean,
    isMetricSystem : Boolean,
    onDismiss: () -> Unit,
    onAddCustomGlass: (Int) -> Unit)
{
    var customGlassText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf(false) } // Message d'erreur

    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,//{ showDialog = false },
        title = { Text("Ajouter un verre personnalisé") },
        text = {
            Column {
                if (isMetricSystem){
                    Text("Entrez la quantité en ml :")
                }else{
                    Text("Entrez la quantité en oz :")
                }
                OutlinedTextField(
                    value = customGlassText,
                    onValueChange = { text ->
                        customGlassText = text
                        errorMessage = false
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.NumberPassword)
                )
                if (errorMessage) {
                    Text("Quantité invalide")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val qtt = customGlassText.toIntOrNull()
                if (qtt != null && qtt > 0) {
                    if (isMetricSystem){
                        onAddCustomGlass(qtt)
                    }else{
                        // si quantité en oz, on la convertis en mL pour l'ajout dans la db
                        onAddCustomGlass((qtt*29.574).toInt())
                    }
                }else{
                    errorMessage = true
                }
            }) {
                Text("Ajouter")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Annuler")
            }
        }
    )
}

@Composable
fun GlassIcon(qtt : Int, supressMode : Boolean, isMetricSystem : Boolean, onDropWater: (Float) -> Unit){

    val context = LocalContext.current // Récupérer le contexte dans un composable
    var image = painterResource(id = R.drawable.verre_petit)
    if (qtt >50 && qtt<=300){image = painterResource(id = R.drawable.verre)
    }
    if (qtt>300){image = painterResource(id = R.drawable.gourde)
    }

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
        var qttText = "$qtt"
        if (!isMetricSystem){qttText = "%.0f".format((qtt/29.574).toFloat())}
        Text(
            text = qttText,
            textAlign = TextAlign.Center
        )
    }
}

