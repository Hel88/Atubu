package com.example.atubu.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.atubu.theme.*

class CalendarScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ShowGarden()
        }
    }
}

private const val ROWS = 5
private const val COLS = 7

@Composable
@Preview(showBackground = true)
fun ShowGarden() {

    val calendarInputList by remember { mutableStateOf(createCalendarList()) }
    var clickedCalendarItem by remember { mutableStateOf<CalendarInput?>(null) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Column (
            modifier = Modifier.fillMaxWidth().padding(10.dp).align(Alignment.Center)
        )
        {
            Calendar(
                calendarInput = calendarInputList,
                onDayClick = { day ->
                    clickedCalendarItem = calendarInputList.first{it.day == day}
                },
                month = "Avril",
                modifier = Modifier.fillMaxWidth().padding(10.dp).aspectRatio(1.3f),
            )

            val waterDrunk = clickedCalendarItem?.value ?: 0
            Text(
                text = waterDrunk.toString(),
                modifier = Modifier.fillMaxWidth(),
                color = md_theme_light_scrim,
                textAlign = TextAlign.Center,
                style = Typography.bodyLarge
            )
        }
    }
}

@Composable
private fun Calendar(
    modifier: Modifier = Modifier,
    calendarInput: List<CalendarInput>,
    onDayClick:(Int)->Unit,
    strokeWidth:Float = 15f,
    month:String
) {
    var canvasSize by remember {
        mutableStateOf(Size.Zero)
    }
    var clickAnimationOffset by remember {
        mutableStateOf(Offset.Zero)
    }

    var animationRadius by remember {
        mutableStateOf(0f)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text(
            text = month,
            modifier = Modifier.fillMaxWidth(),
            color = md_theme_light_scrim,
            textAlign = TextAlign.Center,
            style = Typography.bodyLarge
        )
        Canvas(
            modifier = Modifier.background(md_theme_light_onPrimary)
                .fillMaxSize()
                .pointerInput(true){
                    detectTapGestures (
                        onTap = {offset ->
                            val column = (offset.x / canvasSize.width * COLS).toInt() + 1
                            val row = (offset.y / canvasSize.height * ROWS).toInt() + 1
                            val day = column + (row-1) * COLS
                            if(day <= calendarInput.size){
                                onDayClick(day)
                                clickAnimationOffset = offset
                                scope.launch {
                                    animate(0f,225f, animationSpec = tween(300)) {value, _ ->
                                    animationRadius = value
                                    }
                                }
                            }
                        }
                    )
                }
        ){
            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)
            val xsteps = canvasWidth / COLS
            val ysteps = canvasHeight / ROWS
            val col = (clickAnimationOffset.x / canvasWidth * COLS).toInt() + 1
            val row = (clickAnimationOffset.y / canvasHeight * ROWS).toInt() + 1

            // Masque de l'animation
            val path = Path().apply {
                moveTo((col - 1)*xsteps, (row - 1)*ysteps)
                lineTo(col * xsteps, (row - 1)*ysteps)
                lineTo(col * xsteps, row * ysteps)
                lineTo((col - 1)*xsteps, row * ysteps)
                close()
            }

            clipPath(path){
                drawCircle(
                    brush = Brush.radialGradient(listOf(md_theme_light_primary.copy(0.8f), md_theme_light_primary.copy(0.2f)),
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

            for(i in 1 until ROWS){
                drawLine(
                    color = md_theme_light_primary,
                    start = Offset(0f, i * ysteps),
                    end = Offset(canvasWidth, i * ysteps),
                    strokeWidth = strokeWidth
                )
            }

            for(j in 1 until COLS){
                drawLine(
                    color = md_theme_light_primary,
                    start = Offset(j * xsteps, 0f),
                    end = Offset(j * xsteps, canvasHeight),
                    strokeWidth = strokeWidth
                )
            }

            val txtHeight = 17.dp.toPx()
            for(i in calendarInput.indices){
                val posX = xsteps * (i% COLS) + strokeWidth
                val posY = (i/COLS) * ysteps + txtHeight + strokeWidth/2
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${i+1}",
                        posX,
                        posY,
                        android.graphics.Paint().apply {
                            textSize = txtHeight
                            color = md_theme_light_scrim.toArgb()
                            isFakeBoldText = true
                        }
                    )
                }
            }
        }
    }
}

private fun createCalendarList(): List<CalendarInput> {
    val calendarInputs = mutableListOf<CalendarInput>()
    for (i in 1..31) {
        calendarInputs.add(
            CalendarInput(
                i,
                500
            )
        )
    }
    return calendarInputs
}

data class CalendarInput(
    val day:Int,
    val value:Int
)