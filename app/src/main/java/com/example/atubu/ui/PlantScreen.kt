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
import androidx.compose.ui.text.style.TextAlign
import com.example.atubu.R
import kotlinx.coroutines.launch


class PlantScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentWaterQtt = 1.5f
        setContent{
            Surface (
                modifier = Modifier.fillMaxSize()
            ) {

                MenuDisplay {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        PlantAndWater(currentWaterQtt)
                        Instructions("Glisser et déposer pour arroser la plante (TODO)")
                        DrinkSelectionPanel()
                    }
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
fun DetailedDrawerExample(){

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
                .align(Alignment.BottomCenter) // L'eau monte depuis le bas
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
                    start = Offset(0f, y),
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
fun MenuDisplay(content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            DismissibleDrawerSheet {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)

                    Text("Section 1", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Item 1") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Item 2") },
                        selected = false,
                        onClick = { /* Handle click */ }
                    )

                    Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        badge = { Text("20") }, // Placeholder
                        onClick = { /* Handle click */ }
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

            content() // On insère le contenu ici pour qu'il soit visible sous le drawer
        }
    }
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

@Composable
fun WaterGaugePreview() {
    PlantAndWater(1.5f)
}