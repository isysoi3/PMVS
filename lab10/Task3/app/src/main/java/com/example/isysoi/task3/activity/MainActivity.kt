package com.example.isysoi.task3.activity

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.isysoi.task3.R
import com.example.isysoi.task3.model.Singer
import com.example.isysoi.task3.adapter.SingerAdapter
import com.example.isysoi.task3.database.AppDatabase
import com.example.isysoi.task3.database.SingerEntity
import com.example.isysoi.task3.service.Client
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private var isNetworkEnabled = true
    private lateinit var adapter: SingerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = SingerAdapter(this)
        singeresRecyclerView.adapter = adapter

        refreshButton.setOnClickListener {
            checkNetworkConnection()
            if (isNetworkEnabled) {
                reloadData()
            } else {
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }
        }

        showDatabaseButton.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        checkNetworkConnection()
    }

    private fun showToastToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }

    private fun checkNetworkConnection() {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        isNetworkEnabled = cm.activeNetworkInfo != null
        if (!isNetworkEnabled) {
            showToastToast("The internet doesn't work. Some functionality may not work")
        }
    }


    fun reloadData() {
        Client.service.getSingeres().enqueue(object : Callback<List<Singer>> {
            override fun onResponse(call: Call<List<Singer>>, response: Response<List<Singer>>) {

                if (response.isSuccessful) {
                    AppDatabase.getInstance(this@MainActivity)?.singerDao()?.deleteAllSingeres()
                    response.body()?.forEach {
                        AppDatabase.getInstance(this@MainActivity)
                                ?.singerDao()
                                ?.insert(listOf(SingerEntity(it.name, it.songName, it.count, it.listeners)))

                    }
                    adapter.singeres.clear()
                    adapter.singeres = (response.body() as ArrayList<Singer>?)!!
                }
            }

            override fun onFailure(call: Call<List<Singer>>, t: Throwable) {}
        })
    }

}
