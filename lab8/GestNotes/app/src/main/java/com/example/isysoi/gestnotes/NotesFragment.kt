package com.example.isysoi.gestnotes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_notes.view.*

class NotesFragment: Fragment() {

    private var currentNotes: ArrayList<Note> = arrayListOf()
    private var noteAdapter: RecyclerAdapter? = null
    private lateinit var activityView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityView = inflater.inflate(R.layout.fragment_notes, container, false)



        setRecyclerView()

        val bundle = arguments
        if (bundle != null) {
            val msg = bundle.getString("key")
            if (msg != null) {
                currentNotes.add(Note(msg))
                noteAdapter?.elementsChanged(currentNotes)
            }
        }

        activityView.addNewNoteButton.setOnClickListener {
            fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                    .replace(R.id.container, NoteFragment())
                    .addToBackStack(null)
                    .commit()
        }

        return activityView
    }

    private fun setRecyclerView() {

        activityView.notesRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        noteAdapter = RecyclerAdapter()
        activityView.notesRecyclerView.adapter = noteAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                currentNotes.removeAt(position)
                noteAdapter?.elementsChanged(currentNotes)
            }
        }).attachToRecyclerView(activityView.notesRecyclerView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }


}