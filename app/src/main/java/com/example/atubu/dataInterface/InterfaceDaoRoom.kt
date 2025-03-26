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
    fun getDate(currentDate : Day);

    @Query("SELECT * FROM calendar WHERE date <= (:end) AND date >= (:begin)")
    fun getAllBetween(begin : Date, end : Date) : List<Day>


}