package com.serghov.apps.pulpulak;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


public class MainActivity extends ActionBarActivity implements LocationListener {

    private static Context mContext;

    static  LatLng currentLocation = new LatLng(21 , 57);
    private static GoogleMap googleMap;

    private LocationManager locationManager;
    private String provider;

    ImageButton refreshButton;

    static pulpulakData pulpulaks= new pulpulakData();


    private static LinearLayout detailsView;
    ListView pulpulakList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=getApplicationContext();
        detailsView=(LinearLayout)findViewById(R.id.detailsView);
        final arrayAdapter adapter;
        pulpulakList = (ListView) findViewById(R.id.pulpulakList);

        LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
        /*
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        */
        // Get the location manager

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);
        locationManager.requestSingleUpdate(provider,this,null);
        // Initialize the location fields
        if (location != null)
            onLocationChanged(location);

        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            //Marker TP = googleMap.addMarker(new MarkerOptions().
            //        position(TutorialsPoint).title("TutorialsPoint"));

        } catch (Exception e) {
            e.printStackTrace();
        }



        pulpulak[] p = pulpulaks.getArray();
        for (int i=0;i<p.length;i++) {
            Marker TP = googleMap.addMarker(new MarkerOptions().
                    position(p[i].getPosition()).title(""));
        };


        adapter = new arrayAdapter(this, pulpulaks.getArray());
        pulpulakList.setAdapter(adapter);

        pulpulakList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(pulpulaks.getArray()[position].getPosition())      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }


        });


        final Animation ranim = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        refreshButton = (ImageButton) findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshButton.startAnimation(ranim);
                locationManager.requestSingleUpdate(provider, MainActivity.this, null);
            }
        });


        ImageButton detailsBackButton = (ImageButton) findViewById(R.id.detailsViewBack);

        detailsBackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Animation anim = (Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_out);
                anim.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {

                        detailsView.setVisibility(View.GONE);
                        pulpulakList.setVisibility(View.VISIBLE);
                        pulpulakList.startAnimation((Animation) AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_fade_in));
                    }
                });
                detailsView.startAnimation(anim);
            }
        });

        //refreshButton.setAnimation(ranim);


}



    public static Context getContext() {
        return mContext;
    }
    public static LatLng getCurrentLocation(){
        return currentLocation;
    }
    public static LinearLayout getDetailsView()
    {
        return detailsView;
    }
    public static GoogleMap getMap(){
        return googleMap;
    }
    public static pulpulakData getPulpulaks(){
        return pulpulaks;
    }

    /*public static Animation getRotationAnimation(){
        return ranim;
    }*/
    @Override
    protected void onResume() {
        super.onResume();
        //locationManager.requestLocationUpdates(provider, 5000, 1, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public void onLocationChanged(Location location) {

        try {
            LatLng loc=new LatLng(location.getLatitude(),location.getLongitude());
            currentLocation=loc;
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(loc)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            int i=0;
            for (pulpulak p : ((arrayAdapter)pulpulakList.getAdapter()).getData()) {
                p=pulpulaks.getArray()[i++];
            };
            ((arrayAdapter)pulpulakList.getAdapter()).notifyDataSetChanged();
            //ranim.setRepeatCount(0);
            //refreshButton.clearAnimation();
            Toast.makeText(this, "repeat count: " + refreshButton.getAnimation().getRepeatCount(),
                    Toast.LENGTH_SHORT).show();
            refreshButton.getAnimation().setRepeatCount(2);
            refreshButton.getAnimation().setRepeatMode(2);

            //refreshButton.startAnimation(ranim);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);

    }

}
