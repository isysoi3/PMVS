package com.example.isysoi.task3.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.isysoi.task3.R
import com.example.isysoi.task3.model.Singer
import com.example.isysoi.task3.adapter.SingerAdapter
import com.example.isysoi.task3.database.AppDatabase
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)


        val adapter = SingerAdapter(this)
        singeresRecyclerView.adapter = adapter

        val singeres = AppDatabase.getInstance(this)!!.singerDao().getAllSingeres()
        singeres.mapTo(adapter.singeres) {
            Singer(it.name, it.songName, it.count, it.listeners)
        }
    }
}
