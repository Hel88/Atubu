package com.example.atubu.dataInterface

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext
import java.sql.Date
import java.util.concurrent.DelayQueue
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random

class DataAccessObject(private val context : Context) {


    private val database : AppDatabase  = Room.databaseBuilder(
        context, AppDatabase::class.java,
        "app_database"
    ).build()


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


    public fun getDay(date : Date, callback : (Result<Day>) -> Unit){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = roomDAO.getDay(date)[0]

                withContext(Dispatchers.Main){
                    callback(Result.success(result))
            }
        }catch (e : Exception){
            callback(Result.failure(e))
        }

        }
    }

    public  fun insertDay(day : Day){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                roomDAO.insertAll(day)
            }catch (e : Exception){

            }
        }
    }


    public fun getDaysBetween(begin:Date,end: Date, callback: (Result<List<Day>>) -> Unit ){
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val result = roomDAO.getAllDaysBetween(begin,end)
                withContext(Dispatchers.Main) {
                    callback(Result.success(result))
                }
            }catch (e : Exception){
                callback(Result.failure(e))
            }
        }
    }


    public suspend fun roomTest() : String{
        val newday : Day = Day(
            date = Date(ThreadLocalRandom.current().nextInt() * 1000L),
            waterDrunkL = Random.nextFloat(),
            plantState = Random.nextInt(0,100)
        );
        val lst : List<Day>
        withContext(Dispatchers.IO) {
            roomDAO.insertAll(newday);
            lst = roomDAO.getAllDaysBetween(Date(Long.MIN_VALUE), Date(Long.MAX_VALUE))

        }
        return lst.joinToString(separator = "\n") { day ->
            "Date: ${day.date}, Drunk: ${day.waterDrunkL}, State: ${day.plantState}"
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
