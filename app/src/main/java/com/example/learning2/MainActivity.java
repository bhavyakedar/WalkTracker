package com.example.learning2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble("distance",distance);
        savedInstanceState.putBoolean("running",running);
        savedInstanceState.putBoolean("wasRunning",wasRunning);
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
    }
    double distance = 0;
    Location last_location;
    boolean running = false;
    boolean wasRunning;
    public void startStopwatch(View view){
        boolean on = ((ToggleButton)view).isChecked();
        if(on) {
            Toast toast = Toast.makeText(this, "Activity started!! You may start walking.", Toast.LENGTH_SHORT);
            toast.show();
            running = true;
        }
        else
        {
            Toast toast = Toast.makeText(this,"The Activity is paused!!",Toast.LENGTH_SHORT);
            toast.show();
            running = false;
        }
    }
    public void resetStopwatch(View view){
        Toast toast = Toast.makeText(this,"Your Activity has been reset!!",Toast.LENGTH_SHORT);
        toast.show();
        textView.setText("0.000 KM");
        distance = 0;
    }

    public void startTracker(){
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                String s = String.format("%1$,.3f KM", distance/1000);

                if(running){
                    textView.setText(s);
                }
                handler.postDelayed(this, 1000);
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(MainActivity.this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
        if(savedInstanceState != null){
            distance = savedInstanceState.getDouble("distance");
            running = savedInstanceState.getBoolean("running");
            wasRunning = savedInstanceState.getBoolean("wasRunning");
        }
        LocationListener locationListener =  new LocationListener(){
            @Override
            public void onLocationChanged(Location location){
                if(last_location == null) {
                    last_location = location;
                }
                if(running)
                    distance += location.distanceTo(last_location);
                last_location = location;
            }
            @Override
            public void onStatusChanged(String arg0, int arg1, Bundle bundle) {

            }
            @Override
            public void onProviderEnabled(String s) {

            }
            @Override
            public void onProviderDisabled(String s){

            }

        };
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,1,locationListener);}
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("0.000 Km");
        startTracker();
    }

}
