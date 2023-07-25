package com.example.ssdt.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface SumDao {
    @Query("SELECT * FROM dataSum")
    fun getAllSum():List<DataSum>
    @Query("UPDATE dataSum SET nama = :nama WHERE idsum IN (:id) ")
    fun rename(id:List<Int>, nama:String)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inSum(dataSum: DataSum)
    @Delete
    fun delSum(dataSum: DataSum)
    @Update
    fun upSum(dataSum: DataSum)
}