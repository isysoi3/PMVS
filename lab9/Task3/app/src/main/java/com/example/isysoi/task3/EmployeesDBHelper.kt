package com.example.isysoi.task3


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class EmployeesDBHelper internal constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE " + TABLE_PERSONAL_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_EMAIL + " TEXT NOT NULL, "
                + COLUMN_HOMETOWN + " TEXT NOT NULL, "
                + COLUMN_BIRTHDATE + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL);")

        db.execSQL( ("CREATE TABLE " + TABLE_WORK_NAME + " ("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_PASSPORT + " TEXT NOT NULL, "
                + COLUMN_JOB + " TEXT NOT NULL, "
                + COLUMN_AVG_SALVALUE + " INTEGER NOT NULL, "
                + COLUMN_PERSONAL_ID+ " INTEGER NOT NULL);"))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    fun insertNewPersonal(name: String, email: String, phone: String,hometown: String,birthdate: String) {
        val values = ContentValues()

        values.put(COLUMN_NAME, name)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PHONE, phone)
        values.put(COLUMN_HOMETOWN, hometown)
        values.put(COLUMN_BIRTHDATE, birthdate)

        writableDatabase.insert(TABLE_PERSONAL_NAME, null, values)
    }

    fun insertNewWorkInfo(passport: String, job: String, avg_salvalue: Int, personalID: Int) {
        val values = ContentValues()

        values.put(COLUMN_PASSPORT, passport)
        values.put(COLUMN_JOB, job)
        values.put(COLUMN_AVG_SALVALUE, avg_salvalue)
        values.put(COLUMN_PERSONAL_ID, personalID)

        writableDatabase.insert(TABLE_WORK_NAME, null, values)
    }

    companion object {
        private val DATABASE_NAME = "employees.db"

        val TABLE_PERSONAL_NAME = "personal"
        val COLUMN_NAME = "name"
        val COLUMN_EMAIL = "email"
        val COLUMN_PHONE = "phone"
        val COLUMN_HOMETOWN = "hometown"
        val COLUMN_BIRTHDATE = "birthdate"

        val TABLE_WORK_NAME = "work"
        val COLUMN_PASSPORT = "passport"
        val COLUMN_JOB = "job"
        val COLUMN_AVG_SALVALUE = "avg_salvalue"
        val COLUMN_PERSONAL_ID = "personalID"
    }
}