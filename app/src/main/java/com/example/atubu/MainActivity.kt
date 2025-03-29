package com.example.atubu

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.atubu.dataInterface.DataAccessObject
import com.example.atubu.dataInterface.Day
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.sql.Date

class MainActivity : AppCompatActivity() {


    private lateinit var dataAccessObject: DataAccessObject
    private lateinit var helloView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dataAccessObject = DataAccessObject.getDAO(applicationContext)
        helloView = findViewById(R.id.HelloDisplay)
        dataAccessObject.getDaysBetween(Date(Long.MIN_VALUE), Date(Long.MAX_VALUE)){
            result ->
            result.onSuccess{
                val obtained = result.getOrNull()
                if (obtained is List<Day>){

                helloView.text = listToString(obtained)
                }
            }
            result.onFailure {
                helloView.text = "Oopsie somthing went wrong"
            }
        }
    }

    fun listToString(lst : List<Day>) : String{
        return lst.joinToString(separator = "\n") { day ->
            "Date: ${day.date}, Drunk: ${day.waterDrunkL}, State: ${day.plantState}"
        }
    }
}