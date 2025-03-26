package com.example.atubu.dataInterface

import android.content.Context

class DataAccessObject(private val context : Context) {
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


}