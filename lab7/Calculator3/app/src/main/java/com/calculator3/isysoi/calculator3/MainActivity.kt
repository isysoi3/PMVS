package com.calculator3.isysoi.calculator3

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.calculator3.isysoi.calculator3.length.LengthFragment

const val SPEED_TAG = "SPEED_TAG"
const val LENGTH_TAG = "LENGTH_TAG"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragments_calculator_container)

        if(supportFragmentManager.findFragmentByTag(SPEED_TAG) == null
                && supportFragmentManager.findFragmentByTag(LENGTH_TAG) == null){
            supportFragmentManager.beginTransaction()
                    .add(R.id.calculator_container, LengthFragment(), LENGTH_TAG)
                    .commit()
        }


    }


}
