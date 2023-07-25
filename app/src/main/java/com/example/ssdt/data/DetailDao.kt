package com.example.ssdt.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface DetailDao {
    @Query("SELECT * FROM dataDetail WHERE idDataSum IN (:id) ")
    fun getDetail(id:IntArray):List<DataDetail>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inDetail(dataDetail: DataDetail)
    @Update
    fun upDetail(dataDetail: DataDetail)
}