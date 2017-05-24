package com.diplomski.student.rideabike;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class StartActivity extends Activity {//stavio Activity
   // Global variables
    private GestureDetector gestureDetector;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.diplomski.student.rideabike.R.layout.activity_start);

         notifyThis("Ride a bike", "Thanks for using Ride a bike");
        gestureDetector = new GestureDetector(new SwipeGestureDetector());

    }


    //Start swap
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        // Do something
    }

    private void onRightSwipe() {
        // Do something
        // Animations
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
        //transition using XML view animations
        overridePendingTransition(R.animator.slide_in_left, R.animator.slide_out_right);
    }


    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH) {
                    return false;
                }

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    StartActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    StartActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

  // End Swap





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
   // Animations
   // Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
    //startActivity(intent);
    //finish();
    //transition using XML view animations
    //overridePendingTransition(R.anim.slideinfromright, R.anim.slideouttoleft);



    public void mRideButtOnClick(View view) {

        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);

    }

    public void mRouteButtOnClick(View view) {

        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);

    }



}