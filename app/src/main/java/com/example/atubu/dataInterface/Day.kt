package com.example.atubu.dataInterface

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import android.content.Context
import android.content.SharedPreferences
import java.sql.Date


class Converters{

    @TypeConverter
    fun fromDate(date: Date?) : Long?{
        return date?.time;
    }

    @TypeConverter
    fun toDate(time : Long?) : Date?{
        return  time?.let { Date(it) }
    }
}

object PreferenceHelper{
    private const val PREF_NAME = "MyAppPrefs"
    private const val MIN_QTT = "quantite_min"
    private const val MAX_QTT = "quantite_max"
    private const val BOOL_DISPLAY = "display_qtt"
    private const val BOOL_SYS = "systeme_metimp"


    fun setSys(context: Context, display: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(BOOL_SYS, display).apply()
    }

    fun getSys(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(BOOL_SYS, true)
    }

    fun setBool(context: Context, display: Boolean) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(BOOL_DISPLAY, display).apply()
    }

    fun getBool(context: Context): Boolean {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(BOOL_DISPLAY, true)
    }

    fun setMin(context: Context, qtt: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(MIN_QTT, qtt).apply()
    }

    fun getMin(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(MIN_QTT, 1300)
    }

    fun setMax(context: Context, qtt: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(MAX_QTT, qtt).apply()
    }

    fun getMax(context: Context): Int {
        val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(MAX_QTT, 2000)
    }

}

@Entity(tableName = "calendar")
data class Day(
    @PrimaryKey val date: Date?,
    @ColumnInfo(name = "water_drunk_l") val waterDrunkL: Float?,
    @ColumnInfo(name = "plant_state") val plantState : Int?,
)
