package com.example.isysoi.gestnotes

import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_note.view.*

class NoteFragment: Fragment() {

    private lateinit var activityView: View
    private lateinit var gestureLibrary: GestureLibrary


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activityView = inflater.inflate(R.layout.fragment_note, container, false)

        gestureLibrary = GestureLibraries.fromRawResource(activity!!, R.raw.gestures)
        if (!gestureLibrary.load()) {
            activity?.finish()
        }

        activityView.gestureOverlayView.addOnGesturePerformedListener { _, gesture ->
            val gest = gestureLibrary.recognize(gesture)
            if (gest.size > 0) {
                val input = gest[0]
                if (input.score > 1.0) {
                    var value = ""
                    print(input.name)
                    when (input.name) {
                        "a" -> value = "a"
                        "b" -> value = "b"
                        "c" -> value = "c"
                        "d" -> value = "d"
                        "e" -> value = "e"
                        "g" -> value = "g"
                        "h" -> value = "h"
                        "i" -> value = "i"
                        "f" -> value = "f"
                        "j" -> value = "j"
                        "k" -> value = "k"
                        "l" -> value = "l"
                        "m" -> value = "m"
                        "n" -> value = "n"
                        "o" -> value = "o"
                        "p" -> value = "p"
                        "q" -> value = "q"
                        "r" -> value = "r"
                        "s" -> value = "s"
                        "t" -> value = "t"
                        "u" -> value = "u"
                        "w" -> value = "w"
                        "x" -> value = "x"
                        "y" -> value = "y"
                        "z" -> value = "z"
                        "enter" -> addNote()
                        "remove" -> {
                            activityView.textView.text = ""
                            return@addOnGesturePerformedListener
                        }
                    }
                    activityView.textView.append(value)
                }
            }
        }

        activityView.addNoteButton.setOnClickListener {
            addNote()
        }

        return activityView
    }

    private fun addNote() {
        val bundle = Bundle()
        bundle.putString("key", activityView.textView.text.toString())
        val fragmentReturn = fragmentManager!!.findFragmentByTag(NOTES_TAG)!!
        fragmentReturn.arguments = bundle
        fragmentManager!!.beginTransaction()
                .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left)
                .replace(R.id.container, fragmentManager!!.findFragmentByTag(NOTES_TAG)!!)
                .addToBackStack(NOTES_TAG)
                .commit()
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