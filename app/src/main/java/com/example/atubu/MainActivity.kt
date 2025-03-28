package com.example.atubu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.atubu.dataInterface.DataAccessObject
import com.example.atubu.theme.CupcakeTheme

class MainActivity : AppCompatActivity() {


    private lateinit var dataAccessObject: DataAccessObject
    private lateinit var helloView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CupcakeTheme {
                AtubuApp()
            }
        }

//        dataAccessObject = DataAccessObject(applicationContext)
//        helloView = findViewById(R.id.HelloDisplay)
//        helloView.text = dataAccessObject.incrementTest()

    }
}