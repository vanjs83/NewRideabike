package com.diplomski.student.rideabike;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class StartActivity extends Activity {//stavio Activity
    public double editWeight=0; //edit weight
    public int editAge=0; //edit age


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.diplomski.student.rideabike.R.layout.activity_start);
          notifyThis("Ride a bike", "Thanks for using Ride a bike");
            //editParametars();


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

    /*
    public void editParametars(){

        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.alertdialog, null);

        // variable for input age text
         input = (EditText) textEntryView.findViewById(R.id.age);
        //variable for input weight text
         input1 = (EditText) textEntryView.findViewById(R.id.weigth);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
             builder.setView(textEntryView);
             builder.setIcon(R.mipmap.bike);
             builder.setCancelable(false);
             builder.setTitle("Ride a bike!");
             builder.setMessage("Enter yours parameters");
             builder.setPositiveButton("Next",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)  {
                                     //Edit person age and weight
                                      editAge = Integer.parseInt(input.getText().toString());
                                      editWeight = Double.parseDouble(input1.getText().toString());
                            }
                        });
         builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
             public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
     //   AlertDialog alertDialog =  builder.create();
     //   alertDialog.show();
           builder.show();

    }
*/


    public void mRideButtOnClick(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void mRouteButtOnClick(View view) {

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);

    }



}