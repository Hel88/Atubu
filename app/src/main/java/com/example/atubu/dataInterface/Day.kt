package com.example.atubu.dataInterface

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
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

@Entity(tableName = "calendar")
data class Day(
    @PrimaryKey val date: Date?,
    @ColumnInfo(name = "water_drunk_l") val waterDrunkL: Float?,
    @ColumnInfo(name = "plant_state") val plantState : Int?,
)
