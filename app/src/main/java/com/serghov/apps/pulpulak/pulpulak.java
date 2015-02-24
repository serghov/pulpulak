package com.serghov.apps.pulpulak;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class pulpulak{
    private LatLng position;
    private String address;
    private float rating;
    public pulpulak(LatLng position)
    {
        this.position=position;
        this.address=this.computeAddress();
        rating=-1;
    }
    public pulpulak(LatLng position, String address)
    {
        this.position=position;
        this.address=address;
        rating=-1;
    }
    public pulpulak(LatLng position, String address, int rating)
    {
        this.position=position;
        this.address=address;
        this.rating=rating;
    }

    public float getDistance(LatLng p)
    {
        float[] d=new float[1];
        Location.distanceBetween(this.position.latitude,this.position.longitude,p.latitude,p.longitude,d);
        return d[0];
    }

    private String computeAddress()
    {
        String _Location="tmpStr";
        Geocoder geocoder = new Geocoder(MainActivity.getContext(), Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(this.position.latitude, this.position.longitude, 1);
                if(null!=listAddresses&&listAddresses.size()>0){
                    _Location = listAddresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return _Location;
    }
    public String getAddress()
    {
        return this.address;
    }
    public LatLng getPosition()
    {
        return position;
    }
    public float getRating(){
        return this.rating;
    }
}
