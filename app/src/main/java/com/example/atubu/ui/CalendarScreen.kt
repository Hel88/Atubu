package com.example.atubu.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class CalendarScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{
            ShowCalendar()
        }
    }
}

private const val ROWS = 5;
private const val COLS = 7;

@Composable
@Preview(showBackground = true)
private fun ShowCalendar() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Gray),
        contentAlignment = Alignment.TopCenter
    ){
        val calendarInputList by remember { mutableStateOf(createCalendarList()) }
        Calendar(
            modifier = Modifier.fillMaxWidth().padding(10.dp).aspectRatio(1.3f),
            calendarInput = calendarInputList,
            onDayClick = {
                Log.d("CalendarScreen", "Day clicked $it")
            },
            month = "March"
        )
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

    var AnimationRadius by remember {
        mutableStateOf(0f)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text(
            text = month,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.size(40.dp)
        )
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(true){
                    detectTapGestures (
                        onTap = {offset ->
                            val column = (offset.x / canvasSize.width * COLS).toInt() + 1
                            val row = (offset.y / canvasSize.height * ROWS).toInt() + 1
                            val day = column + (row-1) * COLS
                            if(day <= calendarInput.size){

                            }
                        }
                    )
                }
        ){
            val canvasHeight = size.height
            val canvasWidth = size.width
            canvasSize = Size(canvasWidth, canvasHeight)
            val ysteps = canvasHeight / ROWS
            val xsteps = canvasWidth / COLS

            drawRoundRect(
                Color.Black,
                cornerRadius = CornerRadius(10f, 10f),
                style = Stroke(width = strokeWidth)
            )

            for(i in 1 until ROWS){
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, i * ysteps),
                    end = Offset(canvasWidth, i * ysteps),
                    strokeWidth = strokeWidth
                )
            }

            for(j in 1 until COLS){
                drawLine(
                    color = Color.Black,
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
                            color = Color.Black.toArgb()
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