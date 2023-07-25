package com.example.ssdt.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dataSum")
data class DataSum(
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "idsum")
    val dsid: Int,
    @ColumnInfo (name = "nama")
    var nama: String,
    @ColumnInfo (name = "dataTerkecil")
    var datMin: Int,
    @ColumnInfo (name = "dataTerbesar")
    var datMax: Int,
    @ColumnInfo (name = "dataRata")
    var datAvg: Double)
