package com.example.leveranstest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ListView listView;

    private TextView longitude, latitude;

    private DBHelper dbHelper;

    private String[] showOrders;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private static String phoneNumber = "0739058562";

    private static String phoneNumber2 = "+46739058562";

    private static Random random = new Random();

    // Defined Array values to show in ListView
    private static String[] orders = new String[] {
            "Order 1",
            "Order 2",
            "Order 3",
            "Order 4",
            "Order 5",
            "Order 6",
            "Order 7",
            "Order 8",
            "Order 9",
            "Order 10",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        dbHelper.addHighScore("Martin", 999);

        listView = (ListView) findViewById(R.id.list);

        latitude = (TextView) findViewById(R.id.latitude);

        longitude = (TextView) findViewById(R.id.longitude);

        randomOrders();
    }

    /**
     *  Inflates the menu. The necessary buttons are
     *  added to the Actionbar.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_play:
                randomOrders();
                return true;
            default:
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void randomOrders() {
        int numberOfOrders = 1 + random.nextInt(10); //1 + 0-9 = 1-10
        showOrders = new String[numberOfOrders];
        for (int i = 0; i < numberOfOrders; i++) {
            showOrders[i] = orders[i];
        }
        createAdapter(showOrders);
    }

    private void createAdapter(String[] showOrders) {
        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, showOrders);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;

                // ListView Clicked item value
                String  itemValue = (String) listView.getItemAtPosition(position);

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position: "+ itemPosition +"\n" + "ListItem: " + itemValue, Toast.LENGTH_SHORT)
                        .show();

            }

        });
    }

    public void gps(View view) {

        GPSTracker gps = new GPSTracker(this);

        if (gps.canGetLocation()) {
            String output = "";
            output = "Latitude: " + gps.getLatitude();
            latitude.setText(output);
            output = "Longitude: " + gps.getLongitude();
            longitude.setText(output);
        } else {
            gps.showSettingsAlert();
        }
        //gps.stopUsingGPS();
    }

    public void sendSMSMessage(View view) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {
                        Manifest.permission.SEND_SMS
                        },
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    String message = "Ordern har levererats!";
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS skickat",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS misslyckades...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
