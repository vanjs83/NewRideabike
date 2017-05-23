package com.diplomski.student.rideabike;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.IOException;
import java.util.Locale;




public class ListActivity extends MainActivity {
    //variable list
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView = (ListView) findViewById(R.id.list);
        //Empty list
        TextView emptyText = (TextView)findViewById(R.id.emptyList);
        listView.setEmptyView(emptyText);
           //scroll
        try {
            loadRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(routes !=null) {

            final String[] values = new String[routes.size()];
            final String emptyString="     ";
            for (int i = 0; i < routes.size(); i++) {
                Route ruta = routes.get(i);
                values[i] =  emptyString + ruta.nameRoute + "\n" + emptyString +ruta.date;

            }
                //Array adapter

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);
            // Assign adapter to ListView
            listView.setAdapter(adapter);

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition     = position;

                    ItemOnClick(itemPosition);
                    // ListView Clicked item value
                    // String  itemValue    = (String) listView.getItemAtPosition(position);
                }

            });

        }
    }




    public void ItemOnClick(int Position) {
        currentRoute = routes.get(Position) ;
        mSpeed.setText(String.format(Locale.ENGLISH,"%.2f", currentRoute.speed));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("RoutePos", Position);
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        try {
            loadRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if(routes !=null) {
            final String[] values = new String[routes.size()];
            final String emptyString="     ";
            for (int i = 0; i < routes.size(); i++) {
                Route ruta = routes.get(i);
                values[i] =  emptyString + ruta.nameRoute + "\n" + emptyString +ruta.date;

            }
               //New List Adapter

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, values);

            // Assign adapter to ListView
            listView.setAdapter(adapter);

            // ListView Item Click Listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // ListView Clicked item index
                    int itemPosition     = position;
                    ItemOnClick(itemPosition);
                    // ListView Clicked item value
                    // String  itemValue    = (String) listView.getItemAtPosition(position);
                }

            });


                }
        }


//end class
}


