package com.diplomski.student.rideabike;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class WeatherActivity extends AppCompatActivity implements LocationListener {

    //Creates variable
    private GestureDetector gestureDetector;
    int temperature = 0, Windchill=0, Winddirections=0, humidity, rising , visibility;
    double Windspeed =0, pressure;
    String city = "city", country = "country", text="text", date="distance", Stringrising, Sunrise ,Sunset;
    Location currentlocation;
    LocationManager locationManager;
    TextView Wtemp, Wcity, Wcountry, Wtext, Wdate, Wwspeed, Wwchill, Wwdirection, Whumidity, Wpressure, Wrising, Wvisibility, Wsunrise, Wsunset;

    // Creates variable for view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        gestureDetector = new GestureDetector(new SwipeGestureDetector());//Class for move
           Wtemp = (TextView) findViewById(R.id.temp);
           Wcity = (TextView) findViewById(R.id.town);
           Wtext = (TextView) findViewById(R.id.text);
           Wcountry = (TextView) findViewById(R.id.country);
           Wdate = (TextView) findViewById(R.id.date);
           Wwspeed = (TextView) findViewById(R.id.speedkmh);
           Wwchill = (TextView) findViewById(R.id.chilldeg);
           Wwdirection= (TextView) findViewById(R.id.directionsdeg);
           Whumidity = (TextView) findViewById(R.id.humidity);
           Wpressure = (TextView) findViewById(R.id.pressure);
           Wrising = (TextView) findViewById(R.id.rising);
           Wvisibility = (TextView) findViewById(R.id.visibility);
           Wsunrise = (TextView) findViewById(R.id.Sunrise);
           Wsunset = (TextView) findViewById(R.id.Sunset);



              //location for temperature
         locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
           locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    public void getWeather(Location location) throws IOException {
        // To call the async task do it like this
        Location[] myTaskParams = {location};
        weatherTask task = (weatherTask) new weatherTask().execute(myTaskParams);
    }


    private class weatherTask extends AsyncTask<Location, Void, Void> {
        @Override
        protected Void doInBackground(Location... params) {
            String unit = "c";
            try {
                Location location = params[0];
                String lat = String.format(Locale.ENGLISH,"%f", location.getLatitude());
                lat = lat.replace(",", ".");
                String lng = String.format(Locale.ENGLISH,"%f", location.getLongitude());
                lng = lng.replace(",", ".");
                String q = String.format("select * from weather.forecast where u='%s' and woeid in (select woeid from geo.places(1) where text=\"(%s,%s)\")", unit, lat, lng);
                //  q = Uri.encode(q, "!';:@&=+$,/?%#[]");
                //  q = URLEncoder.encode(q, "utf-8");
                q = URLEncoder.encode(q, "UTF-8").replace("+", "%20");//dekodira u url
                String b = "&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                String url = String.format("https://query.yahooapis.com/v1/public/yql?q=%s%s", q, b);


                StringBuilder response = new StringBuilder();
                URL obj = null;

                obj = new URL(url);

                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    //print result
                    System.out.println(response.toString());
                    try {
                        JSONObject jObject = new JSONObject(response.toString());
                        JSONObject query = jObject.getJSONObject("query");
                        JSONObject results = query.getJSONObject("results");
                        JSONObject channel = results.getJSONObject("channel");
                        JSONObject item = channel.getJSONObject("item");
                        JSONObject condition = item.getJSONObject("condition");
                        JSONObject locations = channel.getJSONObject("location");
                        JSONObject wind = channel.getJSONObject("wind");
                        JSONObject atmosphere = channel.getJSONObject("atmosphere");
                        JSONObject astronomy= channel.getJSONObject("astronomy");
                        temperature = condition.getInt("temp");
                        text = condition.getString("text");
                        date = condition.getString("date").substring(4,16);
                        city = locations.getString("city");
                        country = locations.getString("country");
                         Windspeed = wind.getDouble("speed");
                         Windchill = wind.getInt("chill");
                         Winddirections = wind.getInt("direction");
                          humidity = atmosphere.getInt("humidity");
                          rising = atmosphere.getInt("rising");
                          pressure = atmosphere.getDouble("pressure");
                          visibility = atmosphere.getInt("visibility");
                          Sunrise = astronomy.getString("sunrise");
                          Sunset = astronomy.getString("sunset");


                       // speedWind=wind.getDouble("speed");
                    } catch (JSONException e) {
                        // Oops
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
/*
    @Override
    public void onResume() {
        super.onResume();
     /*
        if (currentlocation != null) {
            try {
                getWeather(currentlocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       // if( currentlocation != null)
//            Wtemp.setText(String.format(Locale.ENGLISH,"%d\u00B0", temperature));
    }
*/

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        // Do something
        //Animations
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
        //transition using XML view animations
        overridePendingTransition(R.animator.slide_in_right, R.animator.slide_out_left);
    }

    private void onRightSwipe() {
        // Do something

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
                    WeatherActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    WeatherActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            try {
                getWeather(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        switch (rising){
            case 0:
                Stringrising = "steady";
                break;
            case 1:
                Stringrising = "rising";
                break;
            case 2:
                Stringrising = "falling";
                break;
        }

         Wtemp.setText(String.format(Locale.ENGLISH,"%d\u00B0", temperature));
         Wcity.setText(String.format(Locale.ENGLISH, "%s", city));
         Wtext.setText(String.format(Locale.ENGLISH, "%s",text));
         Wcountry.setText(String.format(Locale.ENGLISH, "%s", country));
         Wdate.setText(String.format(Locale.ENGLISH,"%s", date));
          Wwchill.setText(String.format(Locale.ENGLISH, "%d\u00B0", Windchill));
          Wwdirection.setText(String.format(Locale.ENGLISH, "%d\u00B0", Winddirections));
          Wwspeed.setText(String.format(Locale.ENGLISH, "%.2f", Windspeed));
           Whumidity.setText(String.format(Locale.ENGLISH, "%d%%", humidity));
           Wrising.setText(String.format(Locale.ENGLISH, "%s", Stringrising));
           Wpressure.setText(String.format(Locale.ENGLISH, "%.2f\u006d\u0062", pressure));
           Wvisibility.setText(String.format(Locale.ENGLISH, "%d\u006b\u006d", visibility));
            Wsunrise.setText(String.format(Locale.ENGLISH, "%s", Sunrise));
            Wsunset.setText(String.format(Locale.ENGLISH, "%s", Sunset));
              currentlocation = location;
    }



    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
