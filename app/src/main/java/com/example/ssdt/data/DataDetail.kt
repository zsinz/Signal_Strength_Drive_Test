package com.example.ssdt.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity (tableName = "dataDetail")
data class DataDetail(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idDataDetail")
    val ddid : Int,
    @ColumnInfo (name = "idDataSum")
    val dsid : Int,
    @ColumnInfo (name = "tanggalWaktu")
    var waktu: String,
    @ColumnInfo (name = "operatorName")
    var siop: String,
    @ColumnInfo (name = "networkType")
    var jar: String,
    @ColumnInfo (name = "kuatSinyalDbm")
    var sinyal: Int,
    @ColumnInfo (name = "Latitude")
    var lat:Double,
    @ColumnInfo (name = "Longitude")
    var lon:Double
)
