package com.example.atubu.dataInterface

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Day::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun InterfaceDaoRoom() : InterfaceDaoRoom
}