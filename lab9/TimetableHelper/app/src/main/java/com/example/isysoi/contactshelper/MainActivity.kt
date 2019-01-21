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


        val dbHandler = DBHelper(this, null, null, 1)
        val timetable = dbHandler.getTimetable()

        if (timetable.isEmpty()) {
            dbHandler.addNewItemToTimetable(TimeTableItem(0, "Test1", "14:00"))
            dbHandler.addNewItemToTimetable(TimeTableItem(1, "Test2", "15:00"))
            dbHandler.addNewItemToTimetable(TimeTableItem(2, "Test2", "16:00"))
            dbHandler.addNewItemToTimetable(TimeTableItem(3, "Test3", "11:00"))
            dbHandler.addNewItemToTimetable(TimeTableItem(4, "Test4", "12:00"))
        }

        showTimetable.setOnClickListener(this)
        addNewTimetableItem.setOnClickListener(this)
        //editTimetable.setOnClickListener(this)

    }


}
