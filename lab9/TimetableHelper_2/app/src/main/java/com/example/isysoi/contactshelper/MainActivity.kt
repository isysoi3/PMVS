package com.example.isysoi.contactshelper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.showTimetable -> startActivity(Intent(this, ShowActivity::class.java))
            R.id.addNewTimetableItem-> startActivity(Intent(this, AddActivity::class.java))
            //R.id.editTimetable -> startActivity(Intent(this, EditActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val dbHandler = DBHelper(this, null, null, 2)
        val timetable = dbHandler.getTimetable()

        if (timetable.isEmpty()) {
            dbHandler.addNewItemToTimetable(TimeTableItem(0, "Test1", "14:00", "15:00", "TestFIO1", 11))
            dbHandler.addNewItemToTimetable(TimeTableItem(1, "Test2", "15:00", "16:00", "TestFIO2", 123))
            dbHandler.addNewItemToTimetable(TimeTableItem(2, "Test2", "16:00", "17:00", "TestFIO3", 11))
            dbHandler.addNewItemToTimetable(TimeTableItem(3, "Test3", "11:00", "13:00", "TestFIO4", 2321))
            dbHandler.addNewItemToTimetable(TimeTableItem(4, "Test4", "12:00", "14:00", "TestFIO5", 123412))
        }

        showTimetable.setOnClickListener(this)
        addNewTimetableItem.setOnClickListener(this)
        //editTimetable.setOnClickListener(this)

    }


}
