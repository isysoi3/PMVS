package com.example.isysoi.task3.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = [SingerEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun singerDao(): SingerDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        private val DATABASE_NAME = "singeresdb.db"

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class.java) {
                    instance = Room.databaseBuilder(context,
                        AppDatabase::class.java,
                            DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }
    }
}