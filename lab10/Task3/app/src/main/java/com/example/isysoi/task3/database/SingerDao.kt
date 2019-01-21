package com.example.isysoi.task3.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface SingerDao {

    @Insert
    fun insert(singers: List<SingerEntity>)

    @Query("DELETE FROM singeres")
    fun deleteAllSingeres()

    @Query("SELECT * FROM singeres")
    fun getAllSingeres(): List<SingerEntity>
}
