package com.example.isysoi.gestnotes

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

const val NOTES_TAG = "NOTES_TAG"
const val NOTE_TAG = "NOTE_TAG"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity_container)

        if(supportFragmentManager.findFragmentByTag(NOTES_TAG) == null
                && supportFragmentManager.findFragmentByTag(NOTE_TAG) == null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, NotesFragment(), NOTES_TAG)
                    .commit()
        }
    }
}
