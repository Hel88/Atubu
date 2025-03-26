package com.example.atubu

import android.content.Context
import android.content.SharedPreferences

class DataAccessObject(private val context : Context) {
    fun saveValue(date: String, value: String) {
        val sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(date, value)
            apply()
        }
    }

    public fun getValue(date : String) : String{
        val sharedPref = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        val defaultValue = "Not found"
        val dataValue = sharedPref.getString(date, defaultValue)?: defaultValue
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
}