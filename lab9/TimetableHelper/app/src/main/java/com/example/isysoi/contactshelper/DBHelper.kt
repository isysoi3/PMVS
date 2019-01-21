package com.example.isysoi.contactshelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context, name: String?,
               factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableSQL = ("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_START_TIME + " TEXT NOT NULL);")

        db.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int,
                           newVersion: Int) {

    }

    fun addNewItemToTimetable(timeTable: TimeTableItem) {
        val values = ContentValues()
        values.put(COLUMN_NAME, timeTable.name)
        values.put(COLUMN_START_TIME, timeTable.startTime)

        writableDatabase.insert(TABLE_NAME, null, values)
        writableDatabase.close()
    }

    fun getTimetable(): ArrayList<TimeTableItem> {
        val querySQL =
                "SELECT * FROM $TABLE_NAME"

        val cursor = writableDatabase.rawQuery(querySQL, null)

        var timeTable = ArrayList<TimeTableItem>()

        if (cursor.moveToFirst()) {
            do {
                val id = Integer.parseInt(cursor.getString(0))
                val name = cursor.getString(1)
                val time = cursor.getString(2)
                timeTable.add(TimeTableItem(id, name, time))
            } while (cursor.moveToNext())

        }
        cursor.close()
        writableDatabase.close()
        return timeTable
    }

    fun updateItem(timetableItem: TimeTableItem) {
        var values = ContentValues()
        values.put(COLUMN_ID, timetableItem.id)
        values.put(COLUMN_NAME, timetableItem.name)
        values.put(COLUMN_START_TIME, timetableItem.startTime)

        writableDatabase.update(TABLE_NAME, values, "id = " +  timetableItem.id, null)
        writableDatabase.close()
    }

    fun deleteAll() {
        val deleteAllFRomTableSQL = "DELETE FROM $TABLE_NAME"

        writableDatabase.execSQL(deleteAllFRomTableSQL)
    }


    companion object {
        private val DATABASE_NAME = "timetableDB.db"
        val TABLE_NAME = "timetable"
        val COLUMN_ID = "id"
        val COLUMN_NAME = "name"
        val COLUMN_START_TIME = "start_time"
    }
}