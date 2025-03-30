package com.example.atubu.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.atubu.R
import com.example.atubu.dataInterface.PreferenceHelper


@Preview
@Composable
fun StartSettingScreen(
    modifier: Modifier = Modifier
){



    val image = painterResource(id = R.drawable.person)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

    ) {
        UserProfileSection()
        NotifPart()
        HydratPart()
    }

}



@Composable
fun UserProfileSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.AccountCircle, contentDescription = "User", modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "User", fontSize = 20.sp, fontWeight = FontWeight.Medium)
    }
}
@Composable
fun NotifPart() {
    var checked by remember { mutableStateOf(true) }


    Column(){
        Text(text = "Notifications", fontSize = 30.sp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ){
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Activer les notifications",fontSize = 15.sp)

    }
    var selectedReminderType by remember { mutableStateOf("regular")}
    Column()
    {
        Row (verticalAlignment = Alignment.CenterVertically)
        {
            RadioButton(
                selected = selectedReminderType == "regular",
                onClick = { selectedReminderType = "regular" }
            )
            Column {
                Text(text = "Rappels réguliers", fontWeight = FontWeight.Bold)
                Text(text = "Toutes les X heures", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))

        }

        Row (verticalAlignment = Alignment.CenterVertically)
        {
            RadioButton(
                selected = selectedReminderType == "smart",
                onClick = { selectedReminderType = "smart" }
            )
            Column {
                Text(text = "Rappels intelligents", fontWeight = FontWeight.Bold)
                Text(text = "Quand on n'a pas bu d'eau depuis X heures", fontSize = 12.sp, color = Color.Gray)
            }
            Spacer(modifier = Modifier.weight(1f))

        }
        Spacer(modifier = Modifier.width(20.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Heure de Début",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // Permet un grand espace
            )

            TextField(
                value = "23h",
                onValueChange = { },
                modifier = Modifier
                    .width(65.dp) // Boîte plus compacte
                    .height(50.dp), // Hauteur réduite
                textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                singleLine = true
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Heure de Fin",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // Permet un grand espace
            )

            TextField(
                value = "23h",
                onValueChange = { },
                modifier = Modifier
                    .width(65.dp) // Boîte plus compacte
                    .height(50.dp), // Hauteur réduite
                textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                singleLine = true
            )
        }
    }
}

@Composable
fun HydratPart() {
    var checked by remember { mutableStateOf(true) }
    val context = LocalContext.current // Récupérer le contexte dans un composable
    var selectedReminderType by remember { mutableStateOf("regular")}

    // Charger la valeur enregistrée
    LaunchedEffect(Unit) {
        checked = PreferenceHelper.getBool(context)
        if (PreferenceHelper.getSys(context)){
            selectedReminderType = "regular"
        }
        else{
            selectedReminderType = "smart"
        }

    }

    Column(){
        Text(text = "Hydratation", fontSize = 30.sp)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 8.dp)
    ){
        Switch(
            checked = checked,
            onCheckedChange = {
                checked = it
                PreferenceHelper.setBool(context,checked)
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text("Afficher les quantités d'eau",fontSize = 15.sp)
    }

    var textValueMin by remember { mutableStateOf("") }
    var textValueMax by remember { mutableStateOf("") }
    var showDialogMin by remember { mutableStateOf(false) }
    var showDialogMax by remember { mutableStateOf(false) }

    Column()
    {

        MyAlertDialogMin(
            showDialogMin = showDialogMin,
            onDismiss = { showDialogMin = false },
            onConfirm = {
                showDialogMin = false
            }
        )
        MyAlertDialogMax(
            showDialogMax = showDialogMax,
            onDismiss = { showDialogMax = false },
            onConfirm = {
                showDialogMax = false
            }
        )

        Column(){
            Text(text = "Unité de mesure", fontSize = 25.sp)
        }
        Row (verticalAlignment = Alignment.CenterVertically)
        {
            RadioButton(
                selected = selectedReminderType == "regular",
                onClick = {
                    selectedReminderType = "regular"
                    PreferenceHelper.setSys(context, true)
                }
            )
            Column {
                Text(text = "Système métrique", fontWeight = FontWeight.Bold)

            }
            Spacer(modifier = Modifier.weight(1f))

        }

        Row (verticalAlignment = Alignment.CenterVertically)
        {
            RadioButton(
                selected = selectedReminderType == "smart",
                onClick = {
                    selectedReminderType = "smart"
                    PreferenceHelper.setSys(context, false)
                }
            )
            Column {
                Text(text = "Système impérial", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.weight(1f))

        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(){
            Text(text = "Objectif journalier", fontSize = 25.sp)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantité minimale",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // Permet un grand espace
            )

            TextField(
                value = textValueMin,
                onValueChange = { newText ->
                    textValueMin = newText // Met à jour l'affichage du TextField
                    if(textValueMin.toInt() > PreferenceHelper.getMax(context)){
                        showDialogMin = true
                    }
                    else {
                        newText.toIntOrNull()?.let { newValue ->  // Convertir en Int si possible
                            PreferenceHelper.setMin(context, newValue) // Stocker dans SharedPreferences
                        }
                    }

                },
                modifier = Modifier
                    .width(65.dp) // Boîte plus compacte
                    .height(50.dp), // Hauteur réduite
                textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                singleLine = true
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quantité Maximale",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f) // Permet un grand espace
            )


            TextField(
                value = textValueMax,
                onValueChange = { newText ->
                    textValueMax = newText // Met à jour l'affichage du TextField
                    if(textValueMax.toInt() < PreferenceHelper.getMin(context)){
                        showDialogMax = true
                    }
                    else {
                        newText.toIntOrNull()?.let { newValue ->  // Convertir en Int si possible
                            PreferenceHelper.setMax(context, newValue) // Stocker dans SharedPreferences
                        }
                    }
                },
                modifier = Modifier
                    .width(65.dp) // Boîte plus compacte
                    .height(50.dp), // Hauteur réduite
                textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center),
                singleLine = true
            )
        }
    }
}

@Composable
fun MyAlertDialogMin(showDialogMin: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (showDialogMin) {
        AlertDialog(
            onDismissRequest = { onDismiss() }, // Quand on clique à l'extérieur
            title = { Text("Erreur") },
            text = { Text("Vous ne pouvez pas entrer une quantité min supérieure à max") },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text("ok")
                }
            },
        )
    }
}
@Composable
fun MyAlertDialogMax(showDialogMax: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (showDialogMax) {
        AlertDialog(
            onDismissRequest = { onDismiss() }, // Quand on clique à l'extérieur
            title = { Text("Erreur") },
            text = { Text("Vous ne pouvez pas entrer une quantité max inférieur à min") },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text("ok")
                }
            },
        )
    }
}

