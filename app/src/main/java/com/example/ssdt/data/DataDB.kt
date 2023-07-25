package com.example.ssdt.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DataDetail::class, DataSum::class], version = 1)
abstract class DataDB : RoomDatabase() {
    abstract fun SumDao(): SumDao
    abstract fun DetailDao(): DetailDao
}