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
fun DrinkSelectionPanel( glasses: List<Int>, onDropWater: (Float) -> Unit, showDialog: ()-> Unit, suppressGlass: (Int) -> Unit){

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
    onDismiss: () -> Unit,
    onAddCustomGlass: (Int) -> Unit)
{
    var customGlassQtt by remember { mutableIntStateOf(0) }
    var errorMessage by remember { mutableStateOf(false) } // Message d'erreur

    if (!showDialog) return

    AlertDialog(
        onDismissRequest = onDismiss,//{ showDialog = false },
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
                if (customGlassQtt > 0) {
                    onAddCustomGlass(customGlassQtt)
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
fun GlassIcon(qtt : Int, supressMode : Boolean, onDropWater: (Float) -> Unit){

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
        Text(
            text = "$qtt",
            textAlign = TextAlign.Center
        )
//        if(PreferenceHelper.getSys(context)){
//            Text(
//                text = "$qtt",
//                textAlign = TextAlign.Center
//            )
//        }
//        else{
//            val imp = qtt/4546
//            Text(
//                text = "$imp",
//                textAlign = TextAlign.Center
//            )
//        }
    }
}

