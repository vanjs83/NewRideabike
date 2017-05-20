package com.diplomski.student.rideabike;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.view.View;



public class StartActivity extends MainActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.diplomski.student.rideabike.R.layout.activity_start);

          notifyThis("Ride a bike", "Thanks for using Ride a bike");

    }
    public  void notifyThis(String title, String message) {

            NotificationCompat.Builder b = new NotificationCompat.Builder(this);
                     b.setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_directions_bike)
                    .setTicker("{your tiny message}")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setContentInfo("INFO");

                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
                b.setContentIntent(contentIntent);

            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(1, b.build());

    }


    public void mRideButtOnClick(View view) {


        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }



    public void mRouteButtOnClick(View view) {

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }

}