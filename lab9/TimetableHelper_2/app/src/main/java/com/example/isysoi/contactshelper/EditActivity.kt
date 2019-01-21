package com.example.isysoi.contactshelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        if (intent.extras == null) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val item = intent.getSerializableExtra("Item") as TimeTableItem
        nameAdd.setText(item.name)
        startTimeAdd.setText(item.startTime)
        finishTimeAdd.setText(item.finishTime)
        FIOAdd.setText(item.teacherFIO)
        roomAdd.setText(item.room.toString())

        val dbHandler = DBHelper(this, null, null, 2)

        saveEditedItem.setOnClickListener {
            val name = nameAdd.text.toString()
            val startTime = startTimeAdd.text.toString()
            val finishTime = finishTimeAdd.text.toString()
            val FIO = FIOAdd.text.toString()
            val room = roomAdd.text.toString().toInt()

            dbHandler.updateItem(TimeTableItem(item.id, name, startTime, finishTime, FIO, room))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}