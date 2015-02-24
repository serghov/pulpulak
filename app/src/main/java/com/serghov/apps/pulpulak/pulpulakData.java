package com.serghov.apps.pulpulak;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class pulpulakData {
    public pulpulakData()
    {

    }
    public pulpulak[] getArray()
    {
        double[] coords=new double[]{ 40.177674, 44.518687,40.178024, 44.519509,40.178765,
                44.513356,40.181576, 44.516514,40.182125, 44.514679,40.185935, 44.514449,40.187604, 44.510337};
        String[] addresses = new String[]{"14 Vardanants St","Tpagrichner St","1 Abovyan St",
                "16 Abovyan St","5 Northern Ave","37 Mesrop MastotsAve","37 Moskovyan St"};

        final pulpulak[] pulpulaks= new pulpulak[coords.length/2];
        for (int i=0;i<pulpulaks.length;i++)
        {
            pulpulaks[i]=new pulpulak(new LatLng(coords[2*i],coords[2*i+1]),addresses[i],(new Random()).nextInt(11));
        }

        Arrays.sort(pulpulaks, new Comparator<pulpulak>() {
            public int compare(pulpulak a, pulpulak b) {
                LatLng pos = MainActivity.getCurrentLocation();
                return Float.compare(a.getDistance(pos), b.getDistance(pos));
            }
        });

        return pulpulaks;
    }
}
