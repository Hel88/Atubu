package com.example.atubu.dataInterface

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "calendar")
data class Day(
    @PrimaryKey val date: Date?,
    @ColumnInfo(name = "water_drunk_l") val waterDrunkL: Float?,
    @ColumnInfo(name = "plant_state") val plantState : Int?,
)
