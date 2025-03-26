package com.example.atubu.dataInterface

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import java.sql.Date
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class DataAccessObject(private val context : Context) {


    private val database : AppDatabase  = AppDatabase.getDatabase(context)

    private  val roomDAO : InterfaceDaoRoom = database.interfaceDaoRoom()

    fun saveValue(key: String, value: String) {
        val sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(key, value)
            apply()
        }
    }

    public fun getValue(key : String) : String{
        val sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val defaultValue = "Not found"
        val dataValue = sharedPref.getString(key, defaultValue)?: defaultValue
        return dataValue
    }

    public fun incrementTest() : String{
        if (getValue("Test") == "Not found") {
            saveValue("Test", "0,1")
            return "0,1"
        }else{
            val retVal : String = getValue("Test")
            val both = retVal.split(",")
            val a = both[0].toInt() + 1
            val b = both[1].toInt() + 1
            val newboth = "$a,$b"
            saveValue("Test", newboth)
            return retVal
        }
    }


    public fun roomTest() : String{
        val newday : Day = Day(
            date = Date(ThreadLocalRandom.current().nextInt() * 1000L),
            waterDrunkL = Random.nextFloat(),
            plantState = Random.nextInt(0,100)
        );

        roomDAO.insertAll(newday);
        val lst = roomDAO.getAllDaysBetween(Date(Long.MIN_VALUE), Date(Long.MAX_VALUE))
        return lst.joinToString(separator = "\n"){
            day ->"Date: ${day.date}, Drunk: ${day.waterDrunkL}, State: ${day.plantState}/5"
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : DataAccessObject?= null

        fun getDAO(context: Context) : DataAccessObject{
            return INSTANCE ?: synchronized(this) {
                val instance = DataAccessObject(context);
                INSTANCE = instance
                instance
            }
        }
    }

}
