package com.serghov.apps.pulpulak;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class arrayAdapter extends ArrayAdapter<pulpulak> {
    private final Context context;
    private final pulpulak[] values;

    public arrayAdapter(Context context, pulpulak[] values) {
        super(context, R.layout.row_layout,values);
        this.context = context;
        this.values = values;

    }
    public pulpulak[] getData() {
        return values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView label = (TextView) rowView.findViewById(R.id.label);
        TextView distance = (TextView) rowView.findViewById(R.id.distance);
        TextView rating = (TextView) rowView.findViewById(R.id.rating);
        label.setText(values[position].getAddress());
        distance.setText(""+(int)values[position].getDistance(MainActivity.getCurrentLocation())+" m");

        if (values[position].getRating()!=-1)
            rating.setText(values[position].getRating()+"/10");
        else
            rating.setText("----   ");

        ImageButton infoButton = (ImageButton)rowView.findViewById(R.id.infoButton);
        infoButton.setFocusable(false);
        infoButton.setFocusableInTouchMode(false);
        infoButton.setClickable(true);

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final ListView pulpulakList = ((ListView)v.getParent().getParent());
                int position=pulpulakList.getPositionForView((View)v.getParent());
                pulpulak currentPulpulak=MainActivity.getPulpulaks().getArray()[position];

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(currentPulpulak.getPosition())      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                MainActivity.getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                final LinearLayout detailsView=MainActivity.getDetailsView();

                ((TextView)detailsView.findViewById(R.id.label)).setText(currentPulpulak.getAddress());
                ((TextView)detailsView.findViewById(R.id.distance))
                        .setText((double)Math.round( currentPulpulak.getDistance(MainActivity.getCurrentLocation()) * 10) / 10+" m");
                ((RatingBar)detailsView.findViewById(R.id.ratingBar)).setRating(currentPulpulak.getRating());

                Animation anim = (Animation) AnimationUtils.loadAnimation(context, R.anim.abc_fade_out);
                anim.setAnimationListener(new Animation.AnimationListener(){
                    @Override
                    public void onAnimationStart(Animation arg0) {
                    }
                    @Override
                    public void onAnimationRepeat(Animation arg0) {
                    }
                    @Override
                    public void onAnimationEnd(Animation arg0) {

                        pulpulakList.setVisibility(View.INVISIBLE);
                        detailsView.setVisibility(View.VISIBLE);
                        detailsView.startAnimation((Animation) AnimationUtils.loadAnimation(context, R.anim.abc_fade_in));
                    }
                });
                pulpulakList.startAnimation(anim);

            }
        });

        return rowView;
    }
}