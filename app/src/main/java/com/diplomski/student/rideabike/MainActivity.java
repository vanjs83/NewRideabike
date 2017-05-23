package com.diplomski.student.rideabike;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.LocationListener;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements LocationListener, OnMapReadyCallback {

    double weight=50;
    int age =0;
    private static final int minTime = 1000; // Minimum time interval for update in seconds, i.e. 1 seconds.
    private static final int minDistance = 0; // Minimum distance change for update in meters, i.e. 1 meters.
    EditText input, input1; //for
   // double calPerHPKM = 30; //calores per h per km or 500cal per hour at speed 20kmh
    Button mGoButton,mSaveButton,mEndButton,mSetelliteButton, mTerrainButton;
    TextView mTime,mCalories , mDate, mClock, mSpeed, mMaxSpeed, mAvSpeed, mDistance, mTemp, mWind;
    TextView mTspeed, mMTspeed, mATspeed, mTdistance, mTduration, mTcalories;
    Location currentLocation;
    LocationManager locationManager;
    ArrayList<Polyline> points = new ArrayList<Polyline>();
    ArrayList<Route> routes = new ArrayList<Route>();
    Route currentRoute, simRoute;
    int IndexRute = -1;
    double lastTimeStamp;
    Handler mHandler = new Handler();
    long startTime;
    long elapsedTime;
    final int REFRESH_RATE = 1000;
    boolean started, mapReady=false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleMap mMap;
    Marker startMarker, endMarker;// marker object
    int temp = 0;
    double speedWind=0;
    int coordsIndex = 0;
    boolean simulatedGPS = false;
    boolean getParametars= false;
    boolean visibeMarker = false;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.diplomski.student.rideabike.R.layout.activity_main);

        mGoButton = (Button) findViewById(com.diplomski.student.rideabike.R.id.GoButton);
        mSaveButton = (Button) findViewById(com.diplomski.student.rideabike.R.id.saveButton);
        mEndButton = (Button)findViewById(com.diplomski.student.rideabike.R.id.EndButton);

        //Button Terrain and Satellite
        mSetelliteButton = (Button) findViewById(R.id.ViewSatellite);
        mTerrainButton = (Button)findViewById(R.id.ViewTerrain);


        mATspeed=(TextView)findViewById(com.diplomski.student.rideabike.R.id.textAverage);
        mMTspeed = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textMaxSpeed);
        mTdistance = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textKm);
        mTduration = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textTime);
        mTcalories = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textCalories);


        mTspeed=(TextView)findViewById(com.diplomski.student.rideabike.R.id.textSpeed);
        mTime = (TextView) findViewById(com.diplomski.student.rideabike.R.id.Time);
        mDate = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textDate);
        mClock = (TextView) findViewById(com.diplomski.student.rideabike.R.id.time);
        mSpeed = (TextView) findViewById(com.diplomski.student.rideabike.R.id.speed);
        mMaxSpeed = (TextView) findViewById(com.diplomski.student.rideabike.R.id.MaxSpeed);
        mAvSpeed = (TextView) findViewById(com.diplomski.student.rideabike.R.id.averagespeed);
        mDistance = (TextView) findViewById(com.diplomski.student.rideabike.R.id.km);
        mTemp = (TextView) findViewById(com.diplomski.student.rideabike.R.id.textTemp);
        mCalories=(TextView)findViewById(com.diplomski.student.rideabike.R.id.calories);
        mWind=(TextView)findViewById(R.id.textwind);
        // Digital font
        Typeface digital7= Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        mMTspeed.setTypeface(digital7);
        mATspeed.setTypeface(digital7);
        mTdistance.setTypeface(digital7);
        mTduration.setTypeface(digital7);
        mTcalories.setTypeface(digital7);

        mTspeed.setTypeface(digital7);
        mMaxSpeed.setTypeface(digital7);
        mAvSpeed.setTypeface(digital7);
        mDistance.setTypeface(digital7);
        mSpeed.setTypeface(digital7);
        mTemp.setTypeface(digital7);
        mWind.setTypeface(digital7);
        mTime.setTypeface(digital7);
        mClock.setTypeface(digital7);
        mDate.setTypeface(digital7);
        mCalories.setTypeface(digital7);
        //timer
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
        // editParametars();//for parameters no because editParametars will bi start on every activity

         // weight = getdouble();
          //  showToast(String.format("%.2f",weight));

        //simulate
       // simulatedGPS = true;

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

                //maknuo else
            }

                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


        }





                 //Ispitivanje Gps provider
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //lOCATION UPDATES
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }
        else{
            showToast("Gps Provider not Activated");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.diplomski.student.rideabike.R.id.mapView);
                  mapFragment.getMapAsync(this);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        try {
            loadRoutes();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }



    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
         mapReady=true;
        //Get parameter from listActivity
        Bundle b = getIntent().getExtras();
        int pos = -1; // or other values
        if(b != null)
            pos = b.getInt("RoutePos");
        if(pos != -1) {
            IndexRute=pos;
            showRoute(routes.get(pos));
        }
        //
        //
        // Add a marker in Sydney, Australia, and move the camera.
        // LatLng sydney = new LatLng(51.5, -0.1);
        //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
          mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a thin red line from London to New York.
    }



       //method for edit parameters

    public void editParametars(){

      //  LayoutInflater factory = LayoutInflater.from(this);
       // final View textEntryView = factory.inflate(R.layout.alertdialog, null);

        // variable for input age text
        //input = (EditText) textEntryView.findViewById(R.id.age);
        //variable for input weight text
         // input1 = (EditText) textEntryView.findViewById(R.id.weigth);

         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         View content = LayoutInflater.from(this).inflate(R.layout.alertdialog, null);
         builder.setView(content);
        // variable for input age text
         input = (EditText) content.findViewById(R.id.age);
        //variable for input weight text
         input1 = (EditText) content.findViewById(R.id.weigth);
         builder.setIcon(R.mipmap.bike);
         builder.setCancelable(true);
         builder.setTitle("Enter yours parameters!");
         builder.setMessage("Age and Weight!");
         builder.setPositiveButton("Next",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)  {
                        //Edit person age and weight
                        age = Integer.parseInt(input.getText().toString());
                        weight = Double.parseDouble(input1.getText().toString());

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

   // setContentView(textEntryView);

    }





    private Runnable startTimer = new Runnable() {
        public void run() {
            if(IndexRute == -1) {
                elapsedTime = System.currentTimeMillis() - startTime;
                if (started)
                {
                    updateTimer(elapsedTime);
                    //calc calories
                    if(currentRoute.speed > 0) { //check does have speed
                      //  double cph = calPerHPKM  *  currentRoute.speed;double
                        double cph = ((weight / 2)  *  currentRoute.speed) - 100;
                        currentRoute.totalCalories += ((cph / 60) / 60);
                        mCalories.setText(String.format(Locale.ENGLISH, "%.2f", currentRoute.totalCalories));
                    }
                }
                //Date time
                SimpleDateFormat hhmmss = new SimpleDateFormat("HH:mm:ss",Locale.US);
                SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy",Locale.US);
                mClock.setText(hhmmss.format(new Date()));
                mDate.setText(date.format(new Date()));
                mTemp.setText(String.format(Locale.ENGLISH, "%d\u00B0", temp));
                mWind.setText(String.format(Locale.ENGLISH, "%.2f", speedWind));
                if (currentRoute != null) {
                    currentRoute.temp = temp;
                    currentRoute.wind = speedWind;
                }

                mHandler.postDelayed(this, REFRESH_RATE);
            }
        }
    };


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page")
                // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
                        JSONObject wind = channel.getJSONObject("wind");
                        temp = condition.getInt("temp");
                        //speedWind = wind.getInt("speed");
                        speedWind=wind.getDouble("speed");
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


    public void getWeather(Location location) throws IOException {
// To call the async task do it like this
        Location[] myTaskParams = {location};
        weatherTask task = (weatherTask) new weatherTask().execute(myTaskParams);
    }


    public void saveRoutes() throws IOException {

        FileOutputStream fos = getApplicationContext().openFileOutput("routes.tmp", Context.MODE_PRIVATE);
        ObjectOutputStream  oos = new ObjectOutputStream(fos);
        oos.writeObject(routes);
        oos.flush();
        oos.close();

    }


    public void loadRoutes() throws IOException, ClassNotFoundException {

        FileInputStream fis = getApplicationContext().openFileInput("routes.tmp");
        ObjectInputStream ois = new ObjectInputStream(fis);
        routes = (ArrayList<Route>) ois.readObject();
        ois.close();
    }


   private void CaptureScreen() {
        GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
            Bitmap bitmap=null;
            String ImgPath;
            @Override
            public void onSnapshotReady(Bitmap snapshot) {

                bitmap = snapshot;
                try {
                    saveImage(bitmap);
                    sendEmail(ImgPath);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            private void saveImage(Bitmap bitmap) throws IOException{
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 40, bytes);
                File f = File.createTempFile("img", ".png", getExternalCacheDir());
                f.createNewFile();
                ImgPath = f.getAbsolutePath();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                long size = bytes.size();
                fo.close();
            }
        };
        mMap.snapshot(callback);
    }

    private void sendEmail(String path){
        SimpleDateFormat sdf = new SimpleDateFormat(" dd.MM.yyyy HH:mm", Locale.US);
        Date starttime = new Date(currentRoute.startTime);
        Date endtime = new Date(currentRoute.endTime);
        String [] mail_to = new String[]{"Enter email here!!"};
        String subject= "Hello, Ride a bike here!";
        String emailBody= "Name route:"+ " " + currentRoute.nameRoute + "\n" +
                "Start Time: " + sdf.format(starttime) + "\n"+
                "End Time: " + sdf.format(endtime) + "\n"+
                "Max Speed: " + String.format(Locale.ENGLISH,"%.2f", currentRoute.maxSpeed) + " kmh\n"+
                "Average speed: " + String.format(Locale.ENGLISH,"%.2f", currentRoute.averageSpeed) + " kmh\n"+
                "Distance: " + String.format(Locale.ENGLISH,"%.2f", currentRoute.distance / 1000) + " km\n"+
                "Calories: " + String.format(Locale.ENGLISH,"%.2f", currentRoute.totalCalories) + " cal\n"+
                "Temp: " + String.format(Locale.ENGLISH,"%d\u00B0", currentRoute.temp) + "\n"+
                "Wind: " + String.format(Locale.ENGLISH,"%.2f", currentRoute.wind) + " kmh\n";
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, mail_to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailBody);
        emailIntent.putExtra(Intent.EXTRA_STREAM,  Uri.parse("file://" +path));
        emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public  void mGoButtOnClick(View view) {

        //START button
        if (IndexRute == -1)
        {

            if(!getParametars) { //insert parametars
                editParametars();//for parameters
                getParametars = true;
            }
            showToast("Go!");
            currentRoute = new Route();
            clearPoints();
            currentRoute.speed = 0;
            currentRoute.maxSpeed = 0;
            currentRoute.averageSpeed = 0;
            currentRoute.totalSpeed = 0;
            currentRoute.speedFactor = 0;
            currentLocation = null;
            SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
            currentRoute.date = date.format(new Date());
            currentRoute.startTime = System.currentTimeMillis();
            startTime = System.currentTimeMillis();
            started = true;
            visibeMarker = false;

        }
        //send button
        else {

            if(simulatedGPS) {
                simRoute = currentRoute;
                currentRoute = new Route();
                clearPoints();
                coordsIndex=0;
                currentRoute.speed = 0;
                currentRoute.maxSpeed = 0;
                currentRoute.averageSpeed = 0;
                currentRoute.totalSpeed = 0;
                currentRoute.speedFactor = 0;
                currentLocation = null;
                SimpleDateFormat date = new SimpleDateFormat("dd.MM.yyyy",Locale.US);
                currentRoute.date = date.format(new Date());
                currentRoute.startTime = System.currentTimeMillis();
                startTime = System.currentTimeMillis();
                started = true;
            }
            //Zakomentirao 4.5.2017 simulacija vožnje
            else {
                CaptureScreen();
            }

            //CaptureScreen();
        }
    }

    public void mEndButtOnClick(View view) {


        if (IndexRute == -1) {

            started = false;
            if (currentRoute != null) {
                //And marker

                currentRoute.clock = mClock.getText().toString();
                if (currentLocation != null) {
                    //Stop marker
                   if(!visibeMarker) {
                       showToast("Finish!");
                       mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                .title("Stop"));
                                 visibeMarker = true; //if marker exist
                        //
                    }
                }

                //Set speed at zero
                currentRoute.speed=0;
                mSpeed.setText(String.format(Locale.ENGLISH,"%.2f", currentRoute.speed));


            }
        }
        else
        {
            //Delete button
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete route");
            builder.setIcon(R.mipmap.question);
            builder.setMessage("Are you sure that you want to delete the selected route?");
            // Set up the buttons
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Yes
                    try {
                        routes.remove(IndexRute);
                        saveRoutes();
                        finish();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            //
        }
        }

    public void mSaveButtOnClick(final View view) throws IOException {
        if (currentRoute != null) {
            // Create Alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Save route:");
            builder.setIcon(R.mipmap.bike);//Novo
            builder.setMessage("Enter the name of your route");

              // variable for input text
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
           // Set up the buttons for save and cancel
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Save button
                    currentRoute.endTime = System.currentTimeMillis();
                    currentRoute.nameRoute = input.getText().toString();
                    routes.add(currentRoute);
                    try {
                        saveRoutes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        }
       // else
         //   return;
    }


    public void mViewSatelliteButtOnClick(View view) {

        if(mapReady)
          mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }




    public void  mViewTerrainButtOnClick(View view) {

        if(mapReady)
         mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }




        public void clearPoints()
    {
        for(int i = 0; i < points.size()-1;i++)
        {
            Polyline line = points.get(i);
            line.remove();

        }
        points.clear();//remove polyline
        mMap.clear();// remove marker

    }

    private void updateTimer(float time) {
        long secs = (long) (time / 1000);
        long mins = (long) ((time / 1000) / 60);
        long hrs = (long) (((time / 1000) / 60) / 60);
        if(simulatedGPS) {
            LatLngPoint point = simRoute.points.get(coordsIndex);
            if(point.secFromStart == elapsedTime)
            {
                Location loc = new Location("simulated provider");
                loc.setLatitude(point.lat);
                loc.setLongitude(point.lng);
                loc.setTime(point.secFromStart);
                onLocationChanged(loc);

            }else if(point.secFromStart < elapsedTime)if(coordsIndex < simRoute.points.size())coordsIndex++;
        }
        //
        secs = secs % 60;
        String seconds = String.valueOf(secs);

        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

        mins = mins % 60;
        String minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }


        String hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "0";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

        if(currentRoute != null)
            currentRoute.time = hours + ":" + minutes + ":" + seconds;
            mTime.setText(hours + ":" + minutes + ":" + seconds);
    }

    public void showToast (String message) {
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if (currentLocation != null) {
            try {
                getWeather(currentLocation);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public  void onLocationChanged(Location location) {

        // Weather update
        if (currentLocation == null) {
            try {
                getWeather(location);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (started) {

            if (currentLocation != null) {
                double c_distance = location.distanceTo(currentLocation);

                //Speed calc
                double timeDelta = (location.getTime() - lastTimeStamp) / 1000;
                if (timeDelta > 0) {
                    currentRoute.speed = (c_distance / timeDelta) * 3.6;
                }
                lastTimeStamp = location.getTime();

                if(currentRoute.speed != 0) {//calculate average speed only when speed not zero
                    currentRoute.totalSpeed += currentRoute.speed;
                    currentRoute.speedFactor++;
                    currentRoute.averageSpeed = currentRoute.totalSpeed / currentRoute.speedFactor;
                }

                currentRoute.distance += c_distance;
                //Map
                LatLngPoint aRecord = new LatLngPoint(currentLocation.getLatitude(), currentLocation.getLongitude(), elapsedTime );

                Polyline line = mMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                new LatLng(location.getLatitude(), location.getLongitude()))
                        .width(5)
                        .color(Color.RED));
                points.add(line);
                currentRoute.points.add(aRecord);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                mSpeed.setText(String.format(Locale.ENGLISH, "%.2f", currentRoute.speed));

            } else {


                    CameraPosition cp = CameraPosition.builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))
                             .zoom(15)
                              .bearing(15)
                               .tilt(15)
                                .build();
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
                     mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp), minTime, null);

                        //CameraUpdate center =
                           //CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                            // CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                //mMap.moveCamera(center);
                //mMap.animateCamera(zoom);
                //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom());
                //Start marker

                          mMap.addMarker(new MarkerOptions()
                          .position(new LatLng(location.getLatitude(),location.getLongitude()))
                            .title("Start")).showInfoWindow();


            }
            //
            if (currentRoute.speed > currentRoute.maxSpeed) currentRoute.maxSpeed = currentRoute.speed;
            //

            mMaxSpeed.setText(String.format(Locale.ENGLISH,"%.2f", currentRoute.maxSpeed));
            mDistance.setText(String.format(Locale.ENGLISH ,"%.2f", currentRoute.distance / 1000));
            mAvSpeed.setText(String.format(Locale.ENGLISH, "%.2f", currentRoute.averageSpeed));
            //
            Log.d("Location", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
            currentRoute.latitude = location.getLatitude();
            currentRoute.longitude = location.getLongitude();
        }
        currentLocation = location;

    }

    public void showRoute(Route ruta) {
        //Buttons
        currentRoute = ruta;
        mEndButton.setText("Delete");
        mSaveButton.setVisibility(View.INVISIBLE);
      //  mGoButton.setVisibility(View.INVISIBLE);
        mGoButton.setText("Send");
        mClock.setText(ruta.clock);
        //
        mTime.setText(ruta.time);
        mCalories.setText(String.format(Locale.ENGLISH,"%.2f", ruta.totalCalories));
        mSpeed.setText(String.format(Locale.ENGLISH,"%.2f", ruta.speed));
        mMaxSpeed.setText(String.format(Locale.ENGLISH,"%.2f", ruta.maxSpeed));
        mDistance.setText(String.format(Locale.ENGLISH,"%.2f", ruta.distance / 1000));
        mAvSpeed.setText(String.format(Locale.ENGLISH,"%.2f", ruta.averageSpeed));
        mTemp.setText(String.format(Locale.ENGLISH,"%d\u00B0", ruta.temp));
        mWind.setText(String.format(Locale.ENGLISH, "%.2f", ruta.wind));
        mDate.setText(ruta.date);

        //
        //Map
        points.clear();
        ArrayList<LatLng> coordList = new ArrayList<LatLng>();

        for(int i = 0; i < ruta.points.size();i++) {

            LatLngPoint latlng1 = ruta.points.get(i);

            if(i == 0)
            {   //add Start marker
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latlng1.lat, latlng1.lng))
                        .title("Start")).showInfoWindow();
            }else if(i == ruta.points.size() -1)
            {     //add Stop marker
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latlng1.lat, latlng1.lng))
                        .title("Stop"));
            }

            // Adding points to ArrayList
            coordList.add(new LatLng(latlng1.lat, latlng1.lng));
        }
        mMap.addPolyline(new PolylineOptions().addAll(coordList).width(5.0f).color(Color.RED));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(ruta.latitude,ruta.longitude)));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
        //
    }






    @Override
    public void onProviderDisabled(String provider) {
        Log.d("onProviderDisabled : "," YES");


    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("onProviderEnabled : "," YES");


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("onStatusChanged : "," YES");

    }


// End Class
}
