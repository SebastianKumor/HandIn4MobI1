package com.corporation.tvm.handin4camera;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.corporation.tvm.helpers.DatabaseHelper;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,LocationListener,android.location.LocationListener {

    LatLng custom = new LatLng(0,0);
    public LocationListener loclist;
    public LocationManager locman;
    private Location loc;
    public double latitude;
    public double longitude;
    DatabaseHelper dbsHelper;
    SQLiteDatabase dbs;
    LatLng[] markers = new LatLng[0];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
        }else{
            buildAlertMessageNoGps();
        }
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Toast.makeText(this, "Network is Enabled in your device", Toast.LENGTH_SHORT).show();
        }else{
            buildAlertMessageNoGps();
        }
        String provider = LocationManager.GPS_PROVIDER;
        try{
            loc = locationManager.getLastKnownLocation(provider);
        }
        catch (SecurityException se){
            se.printStackTrace();
        }
        mapFragment.getMapAsync(this);

        dbsHelper = new DatabaseHelper(getApplication().getApplicationContext());
        dbs = dbsHelper.getReadableDatabase();

        getPicsLatLng();
    }

    public void getPicsLatLng()
    {
        try {
            Cursor c = dbs.rawQuery("SELECT * FROM gallery", null);

            int latIndex = c.getColumnIndex("lat");
            int lngIndex = c.getColumnIndex("lng");
            c.moveToFirst();

            int i = 0;
            while (c != null) {
                Log.i("Results - lat", Double.toString(c.getDouble(latIndex)));
                Log.i("Results - lng", Double.toString(c.getDouble(lngIndex)));
                c.moveToNext();
                markers[i] = new LatLng(c.getDouble(latIndex), c.getDouble(lngIndex));
                i++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        String context = Context.LOCATION_SERVICE;

        if (loc != null) {
            if ( (!locman.isProviderEnabled( LocationManager.GPS_PROVIDER )) || (!locman.isProviderEnabled( LocationManager.NETWORK_PROVIDER )) ) {
                buildAlertMessageNoGps();
            }
            double lat = loc.getLatitude();
            double lot = loc.getLongitude();
            String text = lat + "," + lot;
            String[] separated = text.split(",");
            latitude = Double.parseDouble(separated[0]);
            longitude = Double.parseDouble(separated[1]);
            if (latitude == 0 && longitude == 0)
            {
                custom = new LatLng(0,0);
                map.addMarker(new MarkerOptions().position(custom).title("Marker in Default"));
                map.moveCamera(CameraUpdateFactory.newLatLng(custom));
            }
            else {
                custom = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
                map.moveCamera(CameraUpdateFactory.newLatLng(custom));
            }
            for(int i = 0; i < markers.length; i++)
            {
                custom = new LatLng(markers[i].latitude, markers[i].longitude);
                map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
                map.moveCamera(CameraUpdateFactory.newLatLng(custom));
            }
        }
        if (loc == null) {
            try {
                locman = (LocationManager) getSystemService(context);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!locman.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps();
            } else {
                if (locman != null) {
                    try {
                        locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) this);
                    } catch (SecurityException se) {
                        se.printStackTrace();
                    }
                    try {
                        loc = locman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    } catch (SecurityException se) {
                        se.printStackTrace();
                    }
                    double lat = loc.getLatitude();
                    double lot = loc.getLongitude();
                    String text = lat + "," + lot;
                    String[] separated = text.split(",");
                    latitude = Double.parseDouble(separated[0]);
                    longitude = Double.parseDouble(separated[1]);
                    if (latitude == 0 && longitude == 0) {
                        custom = new LatLng(0, 0);
                        map.addMarker(new MarkerOptions().position(custom).title("Marker in Default"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(custom));
                    } else {
                        custom = new LatLng(latitude, longitude);
                        map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(custom));
                    }
                    for(int i = 0; i < markers.length; i++)
                    {
                        custom = new LatLng(markers[i].latitude, markers[i].longitude);
                        map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(custom));
                    }
                }
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Gps Enabled",
                Toast.LENGTH_SHORT).show();
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No/Already Activated", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        finish();
                        startActivity( new Intent(MapsActivity.this,MapsActivity.class));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}