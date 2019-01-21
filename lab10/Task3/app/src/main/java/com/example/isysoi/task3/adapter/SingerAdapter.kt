package com.example.isysoi.task3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.isysoi.task3.R
import com.example.isysoi.task3.model.Singer

class SingerAdapter(val context: Context) : RecyclerView.Adapter<SingerAdapter.SingerView>() {

    var singeres = ArrayList<Singer>()
        set(value) {
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerView {
        val view = LayoutInflater.from(context).inflate(R.layout.singer_item, parent, false) as LinearLayout
        val tmp = SingerView(view)
        return tmp
    }

    override fun getItemCount() = singeres.size

    override fun onBindViewHolder(holder: SingerView, position: Int) {
        val singer = singeres[position]
        holder.name.text = "Singer ${singer.name}"
        holder.songName.text = "Song name ${singer.songName}"
        holder.listeners.text = "Play count: ${singer.listeners}"
        holder.listeningCount.text = "Listening count: ${singer.count}"
    }

    class SingerView(layout: LinearLayout) : RecyclerView.ViewHolder(layout){
        val name = layout.findViewById<TextView>(R.id.singerNameTextView)
        val songName = layout.findViewById<TextView>(R.id.singerSongNameTextView)
        val listeners = layout.findViewById<TextView>(R.id.singerListenersTextView)
        val listeningCount = layout.findViewById<TextView>(R.id.singerListeningCountTextView)
    }
}