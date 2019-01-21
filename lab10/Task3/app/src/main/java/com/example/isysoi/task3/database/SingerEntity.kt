package com.example.isysoi.task3.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "singeres")
class SingerEntity(
        val name: String,
        val songName: String,
        val count: Int,
        val listeners: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}