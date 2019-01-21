package com.example.isysoi.contactshelper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import kotlinx.android.synthetic.main.activity_show.*


class ShowActivity : Activity() {

    private var itemsList = ArrayList<TimeTableItem>()
    private var timeTableAdapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        val dbHandler = DBHelper(this, null, null, 1)

        setRecyclerView()
        itemsList = dbHandler.getTimetable()
        timeTableAdapter?.elementsChanged(itemsList)
    }

    private fun setRecyclerView() {

        timetableRecyclerView.layoutManager = LinearLayoutManager(this)
        timeTableAdapter = RecyclerAdapter()
        timetableRecyclerView.adapter = timeTableAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val item = itemsList[position]

                val intent = Intent(this@ShowActivity, EditActivity::class.java)
                intent.putExtra("Item", item)
                startActivity(intent)
            }
        }).attachToRecyclerView(timetableRecyclerView)
    }

}