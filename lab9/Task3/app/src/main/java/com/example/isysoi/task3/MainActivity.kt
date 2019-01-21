package com.example.isysoi.task3

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_AVG_SALVALUE
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_BIRTHDATE
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_EMAIL
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_HOMETOWN
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_JOB
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_NAME
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_PASSPORT
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_PERSONAL_ID
import com.example.isysoi.task3.EmployeesDBHelper.Companion.COLUMN_PHONE
import com.example.isysoi.task3.EmployeesDBHelper.Companion.TABLE_PERSONAL_NAME
import com.example.isysoi.task3.EmployeesDBHelper.Companion.TABLE_WORK_NAME
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var db: SQLiteDatabase? = null
    private var revertSort: Boolean? = false
    val dbHelper: EmployeesDBHelper = EmployeesDBHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = dbHelper.writableDatabase

        var count = "SELECT COUNT(*) FROM $TABLE_PERSONAL_NAME"
        var cursor = db!!.rawQuery(count, null)
        cursor.moveToFirst()

        if (cursor.getInt(0) < 1) {
            dbHelper.insertNewPersonal("test1", "email", "phone", "Minsk", "21.02.1993")
            dbHelper.insertNewPersonal("test2", "email", "phone", "Paris", "10.04.1973")
            dbHelper.insertNewPersonal("test3", "email", "phone", "Praga", "4.09.1995")
            dbHelper.insertNewPersonal("test4", "email", "phone", "Minsk", "21.06.2005")
        }

        count = "SELECT COUNT(*) FROM $TABLE_WORK_NAME"
        cursor = db!!.rawQuery(count, null)
        cursor.moveToFirst()

        if (cursor.getInt(0) < 1) {
            dbHelper.insertNewWorkInfo("123412asdasd", "clerk", 2141112, 1)
            dbHelper.insertNewWorkInfo("dasf12e41", "programmer", 43124124, 2)
            dbHelper.insertNewWorkInfo("sd1we23", "teacher", 42332, 3)
            dbHelper.insertNewWorkInfo("asdfsr123e", "manager", 423, 4)
        }

        cursor.close()


        personalTextView.movementMethod = ScrollingMovementMethod()
        workInfoTextView.movementMethod = ScrollingMovementMethod()

        groupButton!!.setOnClickListener {
            group()
        }

        sortButton!!.setOnClickListener {
            sort()
        }

        rowSelectButton.setOnClickListener {
            if (rowSelectEditText!!.text.toString() != "") {
                showRowsThatMore()
            } else {
                showInfo()
            }
        }
    }

    private fun showInfo() {
        var cursor = db!!.query(
                TABLE_PERSONAL_NAME,
                arrayOf(COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_HOMETOWN, COLUMN_BIRTHDATE),
                null,
                null,
                null,
                null,
                null)

        personalTextView.text = ""
        while (cursor.moveToNext()) {
            personalTextView!!.append(cursor.getString(0)
                    + " " + cursor.getString(1)
                    + " " + cursor.getString(2)
                    + " " + cursor.getString(3)
                    + " " + cursor.getString(4) + '\n'.toString())
        }

        cursor = db!!.query(
                TABLE_WORK_NAME,
                arrayOf(COLUMN_PASSPORT, COLUMN_JOB, COLUMN_AVG_SALVALUE, COLUMN_PERSONAL_ID),
                null,
                null,
                null,
                null,
                null)

        workInfoTextView.text = ""
        while (cursor.moveToNext()) {
            workInfoTextView!!.append(cursor.getString(0)
                    + " " + cursor.getString(1)
                    + " " + cursor.getString(2)
                    + " " + cursor.getString(3) + '\n'.toString())
        }

        cursor = db!!.rawQuery("SELECT SUM(" + COLUMN_AVG_SALVALUE + ") FROM "
                + TABLE_WORK_NAME + ";", null)
        if (cursor.moveToFirst()) {
            sumTextView!!.text = cursor.getInt(0).toString()
        }

        cursor.close()
    }

    private fun sort() {
        val cursor: Cursor
        if (revertSort!!) {
            cursor = db!!.query(
                    TABLE_PERSONAL_NAME,
                    arrayOf(COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_HOMETOWN, COLUMN_BIRTHDATE),
                    null, null, null, null,
                    COLUMN_NAME + " DESC")




            revertSort = false
        } else {
            cursor = db!!.query(
                    TABLE_PERSONAL_NAME,
                    arrayOf(COLUMN_NAME, COLUMN_EMAIL, COLUMN_PHONE, COLUMN_HOMETOWN, COLUMN_BIRTHDATE),
                    null, null, null, null,
                    COLUMN_NAME + " ASC")

            revertSort = true
        }
        personalTextView!!.text = ""
        while (cursor.moveToNext()) {
            personalTextView!!.append(cursor.getString(0)
                    + " " + cursor.getString(1)
                    + " " + cursor.getString(2)
                    + " " + cursor.getString(3)
                    + " " + cursor.getString(4) + '\n'.toString())
        }
        cursor.close()
    }

    private fun showRowsThatMore() {
        val cursor = db!!.rawQuery("SELECT " + COLUMN_JOB + ", " + COLUMN_AVG_SALVALUE
                + ", " + COLUMN_PERSONAL_ID + " FROM " + TABLE_WORK_NAME
                + " WHERE " + COLUMN_AVG_SALVALUE + " > " + rowSelectEditText!!.text.toString() + ";", null)


        workInfoTextView!!.text = ""
        if (cursor.moveToFirst()) {
            do {
                workInfoTextView!!.append(cursor.getString(0)
                        + " " + cursor.getString(1)
                        + " " + cursor.getString(2) + '\n'.toString())
            } while (cursor.moveToNext())
        }
        cursor.close()
    }

    override fun onStart() {
        super.onStart()

        showInfo()
    }


    private fun group() {
        val cursor: Cursor

        cursor = db!!.rawQuery("SELECT " + COLUMN_HOMETOWN + ", COUNT(*)"
                + "FROM " + TABLE_PERSONAL_NAME
                + " GROUP BY " + COLUMN_HOMETOWN, null)


        personalTextView!!.text = ""
        if (cursor.moveToFirst()) {
            do {
                personalTextView!!.append(cursor.getString(0)
                        + " " + cursor.getInt(1) + '\n'.toString())
            } while (cursor.moveToNext())
        }

        cursor.close()
    }

}

