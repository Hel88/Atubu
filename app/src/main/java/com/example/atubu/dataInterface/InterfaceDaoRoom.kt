package com.example.atubu.dataInterface

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.sql.Date

@Dao
interface InterfaceDaoRoom {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg newDay : Day)

    @Query("SELECT * FROM calendar WHERE date == (:currentDate)")
    fun getDay(currentDate : Date) : List<Day>

    @Query("SELECT * FROM calendar WHERE date <= (:end) AND date >= (:begin)")
    fun getAllDaysBetween(begin : Date, end : Date) : List<Day>


}