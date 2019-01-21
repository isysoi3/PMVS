package com.example.isysoi.gestnotes

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class Note(val body: String)

class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.NoteViewHolder>() {

    var notes = emptyList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.linkTextView.text = notes[position].body
    }

    fun elementsChanged(updatedList: List<Note>) {
        this.notes = updatedList
        notifyDataSetChanged()
    }

    override fun getItemCount() = notes.size

    class NoteViewHolder(linkView: View) : RecyclerView.ViewHolder(linkView) {
        val linkTextView: TextView = linkView.findViewById(R.id.noteDescription)
    }

}