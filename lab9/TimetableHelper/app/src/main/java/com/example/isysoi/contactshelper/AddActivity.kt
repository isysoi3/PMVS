package com.example.isysoi.contactshelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        val dbHandler = DBHelper(this, null, null, 1)

        addNewItem.setOnClickListener {
            val name = nameAdd.text.toString()
            val startTime = timeAdd.text.toString()

            dbHandler.addNewItemToTimetable(TimeTableItem(0, name, startTime))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}