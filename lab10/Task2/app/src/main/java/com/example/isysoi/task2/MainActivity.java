

package com.example.isysoi.task2;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends FragmentActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final long UPDATE_INTERVAL = 30;


    private static final long FASTEST_UPDATE_INTERVAL = 15;


    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL;


    private LocationRequest mLocationRequest;


    private FusedLocationProviderClient mFusedLocationClient;

    private Button startButton;
    private Button mRemoveUpdatesButton;

    private TextView startedTextView;
    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private TextView altitudeTextView;
    private TextView elapsedTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.isysoi.task2.R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        mRemoveUpdatesButton = findViewById(R.id.stopButton);
        startedTextView = findViewById(R.id.startedTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        altitudeTextView = findViewById(R.id.altitudeTextView);
        elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);

        if (!checkPermissions()) {
            requestPermissions();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateButtonsState(Utils.getRequestingLocationUpdates(this));
        updateText(Utils.getLocationUpdatesResult(this));
    }

    @Override
    protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates(null);
            } else {
                Intent intent = new Intent();
                intent.setAction(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null);
                intent.setData(uri);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(Utils.KEY_LOCATION_UPDATES_RESULT) || s.equals(Utils.KEY_LOCATION_UPDATES_RESULT_DATE)) {
            updateText(Utils.getLocationUpdatesResult(this));
        } else if (s.equals(Utils.KEY_LOCATION_UPDATES_REQUESTED)) {
            updateButtonsState(Utils.getRequestingLocationUpdates(this));
        }
    }


    public void requestLocationUpdates(View view) {
        try {
            Utils.setRequestingLocationUpdates(this, true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            Utils.setRequestingLocationUpdates(this, false);
            e.printStackTrace();
        }
    }


    private void updateText(List<String> info) {
        List<String> tmp = Arrays.asList(info.get(1).split("\n"));
        startedTextView.setText(info.get(0));
        elapsedTimeTextView.setText(String.valueOf(UPDATE_INTERVAL));

        if (tmp.size() >= 3) {
            latitudeTextView.setText(tmp.get(0));
            longitudeTextView.setText(tmp.get(1));
            altitudeTextView.setText(tmp.get(2));
        }

    }

    public void removeLocationUpdates(View view) {
        Utils.setRequestingLocationUpdates(this, false);
        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }


    private void updateButtonsState(boolean requestingLocationUpdates) {
        startButton.setEnabled(!requestingLocationUpdates);
        mRemoveUpdatesButton.setEnabled(requestingLocationUpdates);
    }
}
