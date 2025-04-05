package com.example.atubu.ui

import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.atubu.dataInterface.DataAccessObject
import com.example.atubu.dataInterface.Day
import kotlinx.coroutines.launch
import com.example.atubu.theme.*
import java.sql.Date
import java.time.LocalDate
import android.util.Log

private const val ROWS = 6
private const val COLS = 7

class CalendarScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dao = DataAccessObject.getDAO(this)

        setContent{
            ShowGarden(dao)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ShowGarden(
    dao : DataAccessObject
) {
    dao.insertDay(Day(Date(2025-1900,3,1),500f, 1))
    dao.insertDay(Day(Date(2025-1900,3,2),700f, 2))

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Column (
            modifier = Modifier.fillMaxWidth().padding(10.dp).align(Alignment.Center)
        )
        {
            CalendarDisplay(
                dao = dao,
            )
        }
    }
}

@Composable
private fun CalendarDisplay(
    strokeWidth: Float = 15f,
    month : Int = Calendar.getInstance().get(Calendar.MONTH),
    year : Int = Calendar.getInstance().get(Calendar.YEAR),
    firstDay : Int = LocalDate.of(year, month + 1, 1).dayOfWeek.value - 1,
    dao : DataAccessObject
) {

    var currentMonth by remember { mutableIntStateOf(month) }
    var currentYear by remember { mutableIntStateOf(year) }
    var currentFirst by remember { mutableIntStateOf(firstDay) }

    val calendarInput by remember { mutableStateOf(createCalendarList(
        currentMonth,
        currentYear,
        currentYear ==  Calendar.getInstance().get(Calendar.YEAR) && currentMonth == Calendar.getInstance().get(Calendar.MONTH),
        dao))
    }
    var clickedCalendarItem by remember { mutableStateOf<CalendarInput?>(null) }

    var canvasSize by remember { mutableStateOf(Size.Zero) }
    var clickAnimationOffset by remember { mutableStateOf(Offset.Zero) }
    var animationRadius by remember { mutableFloatStateOf(0f) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxWidth().padding(10.dp).aspectRatio(1.1f),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Black left arrow button
            Button(
                onClick = {
                    currentMonth--
                    if (currentMonth < 0) {
                        currentMonth = 11
                        currentYear--
                    }
                    currentFirst = LocalDate.of(currentYear, currentMonth + 1, 1).dayOfWeek.value - 1
                },
                colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary)
            ) {
                Text("<")
            }

            Text(
                text = getMonthString(currentMonth) + " " + currentYear,
                color = md_theme_light_scrim,
                textAlign = TextAlign.Center,
                style = Typography.bodyLarge
            )

            // Black right arrow button
            Button(
                onClick = {
                    currentMonth++
                    if (currentMonth > 11) {
                        currentMonth = 0
                        currentYear++
                    }
                    currentFirst = LocalDate.of(currentYear, currentMonth + 1, 1).dayOfWeek.value - 1
                },
                colors = ButtonDefaults.buttonColors(containerColor = md_theme_light_primary)
            ) {
                Text(">")
            }
        }

        Canvas(
            modifier = Modifier.background(md_theme_light_onPrimary)
                .fillMaxSize()
                .pointerInput(true) {
                    detectTapGestures(
                        onTap = { offset ->
                            val column = (offset.x / canvasSize.width * COLS).toInt() + 1
                            val row = (offset.y / canvasSize.height * ROWS).toInt() + 1
                            val day = (column + (row - 1) * COLS) - currentFirst
                            if (day <= calendarInput.size && day > 0) {
                                clickedCalendarItem = calendarInput.first{it.day == day}
                                clickAnimationOffset = offset
                                scope.launch {
                                    animate(0f, 225f, animationSpec = tween(300)) { value, _ ->
                                        animationRadius = value
                                    }
                                }
                            }
                        }
                    )
                }
        ) {
            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)
            val xsteps = canvasWidth / COLS
            val ysteps = canvasHeight / ROWS
            val col = (clickAnimationOffset.x / canvasWidth * COLS).toInt() + 1
            val row = (clickAnimationOffset.y / canvasHeight * ROWS).toInt() + 1

            // Masque de l'animation
            val path = Path().apply {
                moveTo((col - 1) * xsteps, (row - 1) * ysteps)
                lineTo(col * xsteps, (row - 1) * ysteps)
                lineTo(col * xsteps, row * ysteps)
                lineTo((col - 1) * xsteps, row * ysteps)
                close()
            }

            clipPath(path) {
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(md_theme_light_primary.copy(0.8f), md_theme_light_primary.copy(0.2f)),
                        center = clickAnimationOffset,
                        radius = animationRadius + 0.1f,
                    ),
                    radius = animationRadius + 0.1f,
                    center = clickAnimationOffset
                )
            }

            drawRoundRect(
                md_theme_light_primary,
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = strokeWidth)
            )

            for (i in 1 until ROWS) {
                drawLine(
                    color = md_theme_light_primary,
                    start = Offset(0f, i * ysteps),
                    end = Offset(canvasWidth, i * ysteps),
                    strokeWidth = strokeWidth
                )
            }

            for (j in 1 until COLS) {
                drawLine(
                    color = md_theme_light_primary,
                    start = Offset(j * xsteps, 0f),
                    end = Offset(j * xsteps, canvasHeight),
                    strokeWidth = strokeWidth
                )
            }

            val txtHeight = 17.dp.toPx()

            for (i in calendarInput.indices) {
                val posX = xsteps * ((i + currentFirst) % COLS) + strokeWidth
                val posY = ((i + currentFirst) / COLS) * ysteps + txtHeight + strokeWidth / 2

                /*
                Box(
                    modifier = Modifier.size()
                )*/

                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${i + 1}",
                        posX,
                        posY,
                        android.graphics.Paint().apply {
                            textSize = txtHeight
                            color = if (calendarInput[i].isToday) md_theme_light_primary.toArgb() else md_theme_light_scrim.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }

    var waterDrunk = ""
    if(clickedCalendarItem?.value != null){
        waterDrunk = clickedCalendarItem?.day.toString() + " " + getMonthString(currentMonth) + " : " + clickedCalendarItem?.value.toString() + "mL"
    }

    Text(
        text = waterDrunk,
        modifier = Modifier.fillMaxWidth(),
        color = md_theme_light_scrim,
        textAlign = TextAlign.Center,
        style = Typography.bodyLarge
    )
}

private fun createCalendarList(
    month : Int,
    year : Int,
    isThisMonth : Boolean,
    dao : DataAccessObject
): List<CalendarInput> {

    val calendar = Calendar.getInstance()

    calendar.set(year, month, 1)

    val calendarInputs = mutableListOf<CalendarInput>()

    for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
        calendarInputs.add(
            CalendarInput(
                i,
                if(isThisMonth) i == calendar.get(Calendar.DAY_OF_MONTH)+1 else false,
                0f,
                1000
            )
        )
    }

    dao.getDaysBetween(Date(year-1900,month,1), Date(year-1900,month,calendar.getActualMaximum(Calendar.DAY_OF_MONTH)),
        callback = { result ->
            Log.d("message","Date : $year $month")
            Log.d("message","Result : $result $result.size")
            if (result.isSuccess) {
                val days = result.getOrNull()
                if (days != null) {
                    for(i in days.indices){
                        Log.d("message", "$i ${days[i].date} ${days[i].waterDrunkL}")
                        calendarInputs[i].value = days[i].waterDrunkL
                    }
                }
            }
        }
    )

    return calendarInputs
}

private fun getMonthString(
    month: Int,
): String {
    // C'est horrible, à changer pour utiliser ce qu'il y a dans strings.xml
    when (month) {
        0 -> return "Janvier"
        1 -> return "Février"
        2 -> return "Mars"
        3 -> return "Avril"
        4 -> return "Mai"
        5 -> return "Juin"
        6 -> return "Juillet"
        7 -> return "Août"
        8 -> return "Septembre"
        9 -> return "Octobre"
        10 -> return "Novembre"
        11 -> return "Décembre"
    }
    return "Unknown"
}

data class CalendarInput(
    val day:Int?,
    val isToday:Boolean = false,
    var value:Float?,
    val objective:Int
)