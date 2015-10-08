package com.corporation.tvm.handin4camera;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends android.support.v4.app.FragmentActivity implements OnMapReadyCallback,LocationListener,android.location.LocationListener {

    LatLng custom = new LatLng(0,0);
    public LocationListener loclist;
    public LocationManager locman;
    private Location loc;
    public double latitude;
    public double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        LocationManager locationManager;
        String context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)getSystemService(context);

        String provider = LocationManager.GPS_PROVIDER;
        try{
            loc = locationManager.getLastKnownLocation(provider);
        }
        catch (SecurityException se){
            se.printStackTrace();
        }
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        String context = Context.LOCATION_SERVICE;

        if (loc != null) {
            double lat = loc.getLatitude();
            double lot = loc.getLongitude();
            String text = lat + "," + lot;
            String[] separated = text.split(",");
            latitude = Double.parseDouble(separated[0]);
            longitude = Double.parseDouble(separated[1]);
            custom = new LatLng(latitude,longitude);
            map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
            map.moveCamera(CameraUpdateFactory.newLatLng(custom));
        }
        if (loc == null)
        {
            try{
                locman = (LocationManager)getSystemService(context);
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(locman != null){
                try{
                    locman.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, (android.location.LocationListener)this);
                }
                catch (SecurityException se){
                    se.printStackTrace();
                }
                try{
                    loc = locman.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                catch (SecurityException se){
                    se.printStackTrace();
                }
                double lat = loc.getLatitude();
                double lot = loc.getLongitude();
                String text = lat + "," + lot;
                String[] separated = text.split(",");
                latitude = Double.parseDouble(separated[0]);
                longitude = Double.parseDouble(separated[1]);
                custom = new LatLng(latitude,longitude);
                map.addMarker(new MarkerOptions().position(custom).title("Marker in Custom"));
                map.moveCamera(CameraUpdateFactory.newLatLng(custom));
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

}