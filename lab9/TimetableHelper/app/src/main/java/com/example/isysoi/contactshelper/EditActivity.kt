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
        timeAdd.setText(item.startTime)

        val dbHandler = DBHelper(this, null, null, 1)

        saveEditedItem.setOnClickListener {
            val name = nameAdd.text.toString()
            val startTime = timeAdd.text.toString()

            dbHandler.updateItem(TimeTableItem(item.id, name, startTime))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}