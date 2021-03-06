package com.example.isysoi.contactshelper

import android.provider.ContactsContract
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.TimeTableItemViewHolder>() {

    var timetable = emptyList<TimeTableItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeTableItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
        return TimeTableItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeTableItemViewHolder, position: Int) {
        holder.idTextView.text = timetable[position].id.toString()
        holder.nameTextView.text = timetable[position].name
        holder.startTimeTextView.text = timetable[position].startTime
    }

    fun elementsChanged(updatedList: List<TimeTableItem>) {
        this.timetable = updatedList
        notifyDataSetChanged()
    }

    override fun getItemCount() = timetable.size

    class TimeTableItemViewHolder(linkView: View) : RecyclerView.ViewHolder(linkView) {
        val idTextView: TextView = linkView.findViewById(R.id.idTextView)
        val nameTextView: TextView = linkView.findViewById(R.id.nameTextView)
        val startTimeTextView: TextView = linkView.findViewById(R.id.startTimeTextView)
    }

}