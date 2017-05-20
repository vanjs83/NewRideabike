package com.diplomski.student.rideabike; /**
 * Created by Student on 18.10.2016..
 */

import java.io.Serializable;
import java.util.ArrayList;


class LatLngPoint implements Serializable {
    double lat;
    double lng;
    long secFromStart;
    public LatLngPoint(double _lat, double _lng, long _secFromStart){
        lat = _lat;
        lng = _lng;
        secFromStart = _secFromStart;
    }
}


   public class Route  implements Serializable {
       //   thePlayersList.add(new Player(1));
      //   thePlayersList.add(new Player(2));
     // Players[] thePlayers = thePlayersList.toArray();
    ArrayList<LatLngPoint> points = new ArrayList<LatLngPoint>();
    String nameRoute; String clock; double speed = 0; double maxSpeed = 0;
       double averageSpeed = 0;double totalSpeed = 0; int speedFactor = 0;
       double totalCalories = 0;
       double latitude = 0;
       double longitude = 0;
       String date;
       double distance = 0;
       int temp = 0;
       double wind;
       long startTime;
       long endTime;
       String time;
}
