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
        if (newVersion > oldVersion) {
            db.execSQL("DELETE FROM $TABLE_NAME;")

            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_FINISH_TIME TEXT;")
            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_TEACHER_FIO TEXT;")
            db.execSQL( "ALTER TABLE $TABLE_NAME ADD COLUMN $COLUMN_ROOM INTEGER;")
        }
    }

    fun addNewItemToTimetable(timeTable: TimeTableItem) {
        val values = ContentValues()
        values.put(COLUMN_NAME, timeTable.name)
        values.put(COLUMN_START_TIME, timeTable.startTime)
        values.put(COLUMN_FINISH_TIME, timeTable.finishTime)
        values.put(COLUMN_TEACHER_FIO, timeTable.teacherFIO)
        values.put(COLUMN_ROOM, timeTable.room)

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
                val startTime = cursor.getString(2)
                val finishTime = cursor.getString(3)
                val teacherFIO = cursor.getString(4)
                val room = Integer.parseInt(cursor.getString(5))
                timeTable.add(TimeTableItem(id, name, startTime, finishTime, teacherFIO, room))
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
        values.put(COLUMN_FINISH_TIME, timetableItem.finishTime)
        values.put(COLUMN_TEACHER_FIO, timetableItem.teacherFIO)
        values.put(COLUMN_ROOM, timetableItem.room)

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
        val COLUMN_FINISH_TIME = "finish_time"
        val COLUMN_TEACHER_FIO = "teacher_FIO"
        val COLUMN_ROOM = "room"
    }
}