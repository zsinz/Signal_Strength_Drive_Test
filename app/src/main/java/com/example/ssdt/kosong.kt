package com.example.ssdt

import androidx.room.ColumnInfo
import androidx.room.Entity

import androidx.room.PrimaryKey



class User(){
    val data1 : Int = 15
    var data2 : Double = 0.0
    fun cek(){
        data2 = data1.toDouble()
    }
}
