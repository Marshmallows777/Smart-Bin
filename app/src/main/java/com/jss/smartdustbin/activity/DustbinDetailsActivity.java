package com.jss.smartdustbin.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jss.smartdustbin.R;
import com.jss.smartdustbin.model.Dustbin;
import com.jss.smartdustbin.utils.Helper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class DustbinDetailsActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener {

    TextView garbageLevelTv;
    TextView lastUpdatedTv;
    ProgressBar dustbinLevelPb;
    TextView landmarkTv;
    TextView installedByTv;
    ImageView alertIcon;
    Dustbin dustbin;
    TextView binTv;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin_details);
        garbageLevelTv = findViewById(R.id.garbage_level_tv);
        lastUpdatedTv = findViewById(R.id.last_updated_tv);
        dustbinLevelPb = findViewById(R.id.dustbin_progressbar);
        landmarkTv = findViewById(R.id.landmark_tv);
        installedByTv = findViewById(R.id.installed_by_tv);
        alertIcon = findViewById(R.id.info_icon);
        binTv = findViewById(R.id.bin_tv);

        alertIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(DustbinDetailsActivity.this);
                builder.setTitle("Message");
                builder.setMessage(dustbin.getComment());
                builder.setPositiveButton("OK", null);
                builder.setIcon(R.drawable.ic_message);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        Bundle bundle = getIntent().getExtras();
        dustbin = bundle.getParcelable("dustbin");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Details");

        int garbageStatus = Helper.getGarbageStatusFromLevel(dustbin.getGarbageLevel());


        dustbinLevelPb.setProgress(Integer.parseInt(dustbin.getGarbageLevel()));
        /*dustbinLevelPb.setProgress(77);
        dustbinLevelPb.setBackgroundColor(Color.parseColor("#E2574C"));*/
        if(garbageStatus == 1) {
            garbageLevelTv.setTextColor(Color.parseColor("#44A849"));
            dustbinLevelPb.setProgressDrawable(getDrawable(R.drawable.progressbar_green));
        } else if(garbageStatus == 2) {
            garbageLevelTv.setTextColor(Color.parseColor("#FF8922"));
            dustbinLevelPb.setProgressDrawable(getDrawable(R.drawable.progressbar_orange));
        } else if(garbageStatus == 3) {
            garbageLevelTv.setTextColor(Color.parseColor("#E2574C"));
            dustbinLevelPb.setProgressDrawable(getDrawable(R.drawable.progressbar_red));
        }


        garbageLevelTv.setText(dustbin.getGarbageLevel() + "% full");
        landmarkTv.setText(dustbin.getLandmark());
        installedByTv.setText(dustbin.getInstalledByUser().getFirstName() + " " + dustbin.getInstalledByUser().getLastName());
        double lat = Double.parseDouble(dustbin.getLatitude());
        double lng = Double.parseDouble(dustbin.getLongitude());
        lastUpdatedTv.setText(dustbin.getLastUpdated());
        binTv.setText(dustbin.getBin());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.clear();

                LatLng dustbinLocation = new LatLng(lat, lng);
                googleMap.addMarker(new MarkerOptions().position(dustbinLocation)
                        .position(new LatLng(lat, lng))
                        .icon(getMarkerIcon(garbageStatus)));

                //CameraUpdateFactory cameraUpdateFactory = new CameraUpdateFactory();

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dustbinLocation, 12));
            }
        });




    }

    public BitmapDescriptor getMarkerIcon(int garbageStatus) {
        switch (garbageStatus){
            case 1 :
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_green", 85, 130));
            case 2 :
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_orange", 85, 130));
            case 3 :
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_red", 110, 140));
            default:
                return BitmapDescriptorFactory.fromBitmap(resizeMapIcons("marker_green", 85, 130));
        }

    }

    public Bitmap resizeMapIcons(String iconName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }


}
