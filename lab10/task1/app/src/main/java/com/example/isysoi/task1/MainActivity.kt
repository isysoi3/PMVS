package com.example.isysoi.task1

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import com.example.isysoi.task1.R.id.coordinatesTextView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var locManager: LocationManager

    private var locListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            val format: DateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH)
            coordinatesTextView.text = String.format("Time - %s\n(%.3f, %.3f)\nAltitude: %.2f",
                    format.format(Date(location.time)),
                    location.latitude,
                    location.longitude,
                    location.altitude)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onProviderDisabled(provider: String) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        validatePermissions()
    }

    private fun validatePermissions() {
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this,
                    listOf(android.Manifest.permission.ACCESS_COARSE_LOCATION).toTypedArray(),
                    11)
        }

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                0,
                0f,
                locListener)

        if (locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSuccessToast()
        } else {
            showUnsuccessToast()
            validatePermissions()
        }
    }

    private fun showSuccessToast() {
        val toast = Toast.makeText(this, "Location is enabled", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    private fun showUnsuccessToast() {
        val toast = Toast.makeText(this, "Location is disabled", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

}
