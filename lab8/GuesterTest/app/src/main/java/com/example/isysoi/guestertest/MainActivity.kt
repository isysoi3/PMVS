package com.example.isysoi.guestertest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast

class MainActivity : AppCompatActivity(), GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private lateinit var detector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        detector = GestureDetectorCompat(this, this)
        detector.setOnDoubleTapListener(this)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        this.detector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
        Toast.makeText(applicationContext, "onDoubleTapEvent", Toast.LENGTH_SHORT).show()
        return onDoubleTapEvent(e)
    }

    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        Toast.makeText(applicationContext, "onSingleTapConfirmed", Toast.LENGTH_SHORT).show()
        return onSingleTapConfirmed(e)
    }

    override fun onDoubleTap(e: MotionEvent?): Boolean {
        Toast.makeText(applicationContext, "onDoubleTap", Toast.LENGTH_SHORT).show()
        return onDoubleTap(e)
    }

    override fun onShowPress(e: MotionEvent?) {
        Toast.makeText(this, "onShowPress", Toast.LENGTH_SHORT).show()
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        Toast.makeText(this, "onSingleTapUp", Toast.LENGTH_SHORT).show()
        return onSingleTapUp(e)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        Toast.makeText(this, "onDown", Toast.LENGTH_SHORT).show()
        return onDoubleTap(e)
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        Toast.makeText(this, "onFling", Toast.LENGTH_SHORT).show()
        return onFling(e1, e2, velocityX, velocityY)
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
        Toast.makeText(this, "onScroll", Toast.LENGTH_SHORT).show()
        return onScroll(e1, e2, distanceX, distanceY)
    }

    override fun onLongPress(e: MotionEvent?) {
        Toast.makeText(this, "onLongPress", Toast.LENGTH_SHORT).show()

    }



}
