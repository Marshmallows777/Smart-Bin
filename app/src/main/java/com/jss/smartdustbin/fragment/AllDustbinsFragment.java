/*
package com.jss.smartdustbin.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jss.smartdustbin.Activities.DustbinDetailsActivity;
import com.jss.smartdustbin.R;

import java.util.HashMap;

public class AllDustbinsFragment extends Fragment implements GoogleMap.OnMarkerClickListener {

    TextView dustbinState;
    TextView dustbinCity;
    TextView dustbinLocality;
    LinearLayout content;
    ProgressBar loader;
    ProgressBar dustbinLevelPb;
    Button dustbinMoreDetails;
    private HashMap<Marker, Integer> mHashMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_dustbins, container, false);
        mHashMap = new HashMap<Marker, Integer>();
        dustbinState = rootView.findViewById(R.id.state_text_view);
        dustbinCity = rootView.findViewById(R.id.city_text_view);
        dustbinLocality = rootView.findViewById(R.id.locality_text_view);
        content = rootView.findViewById(R.id.comtent_all_dustbin_activity);
        loader = rootView.findViewById(R.id.loader_all_dustbin_map);
        dustbinLevelPb = rootView.findViewById(R.id.dustbin_progressbar);
        dustbinMoreDetails = rootView.findViewById(R.id.bt_more_details);

        dustbinLevelPb.setProgress(100);

        dustbinMoreDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dustbinDetailsActivityIntent = new Intent(getActivity(), DustbinDetailsActivity.class);
                startActivity(dustbinDetailsActivityIntent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.all_dustbins_map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
               // mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                mMap.clear(); //clear old markers

                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(37.4219999,-122.0862462))
                        .zoom(10)
                        .bearing(0)
                        .tilt(45)
                        .build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 1000, null);
                */
/*mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.4219999,-122.0862462)));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
*//*

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.4219999, -122.0862462))
                        .title("Spider Man")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.4629101,-122.2449094))
                        .title("Iron Man")
                        .snippet("His Talent : Plenty of money"));

                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(37.3092293,-122.1136845))
                        .title("Captain America"));

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        dustbinState.setText("UP");
                        dustbinCity.setText("Noida");
                        dustbinLocality.setText("Sector 58");
                        return true;
                    }
                });

            }
        });


        return rootView;
    }

    private void updateMap(){

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {

       */
/* if (marker.equals(myMarker))
        {
            //handle click here
        }*//*

       return true;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("All Dustbins");

    }

}*/
