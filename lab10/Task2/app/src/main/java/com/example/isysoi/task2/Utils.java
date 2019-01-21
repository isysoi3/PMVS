

package com.example.isysoi.task2;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


class Utils {

    final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";
    final static String KEY_LOCATION_UPDATES_RESULT_DATE = "location-update-result-date";
    final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    final static String CHANNEL_ID = "channel_01";

    static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply();
    }

    static boolean getRequestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
    }


    static void sendNotification(Context context, String notificationDetails) {

        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.putExtra("from_notification", true);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);


        stackBuilder.addParentStack(MainActivity.class);


        stackBuilder.addNextIntent(notificationIntent);


        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);


        builder.setSmallIcon(R.mipmap.ic_launcher)


                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher))
                .setColor(Color.RED)
                .setContentTitle("Location update")
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);


        builder.setAutoCancel(true);


        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.app_name);

            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);


            mNotificationManager.createNotificationChannel(mChannel);


            builder.setChannelId(CHANNEL_ID);
        }


        mNotificationManager.notify(0, builder.build());
    }


    static String getLocationResultTitle(Context context, List<Location> locations) {
        return DateFormat.getDateTimeInstance().format(new Date());
    }


    private static String getLocationResultText(Context context, List<Location> locations) {
        if (locations.isEmpty()) {
            return context.getString(R.string.unknown_location);
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : locations) {
            sb.append(location.getLatitude());
            sb.append("\n");
            sb.append(location.getLongitude());
            sb.append("\n");
            sb.append(location.getAltitude());
            sb.append("\n");
        }
        return sb.toString();
    }

    static void setLocationUpdatesResult(Context context, List<Location> locations) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT, getLocationResultText(context, locations))
                .apply();

        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(KEY_LOCATION_UPDATES_RESULT_DATE, getLocationResultTitle(context, locations))
                .apply();
    }

    static List<String> getLocationUpdatesResult(Context context) {
        ArrayList<String> tmp = new ArrayList<>();
        tmp.add(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT_DATE, ""));
        tmp.add(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(KEY_LOCATION_UPDATES_RESULT, ""));

        return tmp;
    }
}
