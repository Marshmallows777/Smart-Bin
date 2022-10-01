package com.jss.smartdustbin.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jss.smartdustbin.API;
import com.jss.smartdustbin.R;
import com.jss.smartdustbin.adapter.DustbinsAdapter;
import com.jss.smartdustbin.model.Dustbin;
import com.jss.smartdustbin.model.Ward;
import com.jss.smartdustbin.utils.HttpStatus;
import com.jss.smartdustbin.utils.Jsonparser;
import com.jss.smartdustbin.utils.NetworkReceiver;
import com.jss.smartdustbin.utils.SmartDustbinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.jss.smartdustbin.activity.LoginActivity.LOG_TAG;

public class DustbinListActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = DustbinListActivity.class.getSimpleName();
     private static final int NO_OF_ITEMS_IN_RECYCLER_VIEW = 10;
    private RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    private List<Dustbin> dustbinList;
    private DustbinsAdapter dustbinsAdapter;
    ProgressBar progressBar;
    View defaultTv;
    View defaultEmptyWardTv;
    ImageView mapIcon;
    ImageView filterIcon;
    List<Ward> wardList;
    Spinner wardsSpinner;
    String wardId;
    private Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    int pageCount = 0;
    int totalPages = 1;
    private NetworkReceiver receiver;
    LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin_list);

        Toolbar toolbar = findViewById(R.id.toolbar_top);
        toolbar.setTitle("Dustbins");
        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        receiver = new NetworkReceiver();
        wardList = new ArrayList<>();

        recyclerView = findViewById(R.id.rv_dustbins);
        dustbinList = new ArrayList<>();
        dustbinsAdapter = new DustbinsAdapter(this, dustbinList);
        progressBar = findViewById(R.id.progressBar);
        defaultTv = findViewById(R.id.empty_dustbin_list_default_tv);
        defaultEmptyWardTv = findViewById(R.id.empty_ward_list_default_tv);
//        mapIcon = findViewById(R.id.map_icon);
//        filterIcon = findViewById(R.id.filter);
        wardsSpinner = findViewById(R.id.spinner_wards_select);
        contentLayout = findViewById(R.id.content);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = mLayoutManager.getChildCount();
                totalItems = mLayoutManager.getItemCount();
                scrollOutItems = mLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems) && pageCount < totalPages-1)
                {
                    isScrolling = false;
                    pageCount++;
                    loadDustbinList(wardId, pageCount, NO_OF_ITEMS_IN_RECYCLER_VIEW);
                }
            }
        });

        /*mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dustbinList.size() == 0){
                    Toast.makeText(DustbinListActivity.this, "No dustbins to show on map", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(DustbinListActivity.this, AllDustbinActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("dustbin_list", (ArrayList<? extends Parcelable>) dustbinList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });

        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] choices = {"25% and below", "25% - 75%", "75% and above", "Faulty dustbins"};

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DustbinListActivity.this)
                        .setTitle("Filter by Garbage level");
                alertDialogBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(receiver.isConnected()){
                            recyclerView.setVisibility(GONE);
                            dustbinList.clear();
                            progressBar.setVisibility(View.VISIBLE);
                            defaultTv.setVisibility(GONE);
                            defaultEmptyWardTv.setVisibility(GONE);
                            if(wardList.size() == 0)
                                defaultEmptyWardTv.setVisibility(View.VISIBLE);
                            else
                                loadDustbinList(wardList.get(0).getId(), pageCount, NO_OF_ITEMS_IN_RECYCLER_VIEW );
                        } else
                            Toast.makeText(DustbinListActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();

                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", null)
                        .setSingleChoiceItems(choices, 0,null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });*/

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(dustbinsAdapter);
        if(receiver.isConnected()){
            defaultTv.setVisibility(GONE);
            defaultEmptyWardTv.setVisibility(GONE);
            loadWardList();

        } else
            Toast.makeText(DustbinListActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();




       /* Dustbin d1 = new Dustbin("75", "12/09/19 10:14 AM");
        Dustbin d2 = new Dustbin("25", "12/09/19 10:14 AM");
        Dustbin d3 = new Dustbin("50", "12/09/19 10:14 AM");
        Dustbin d4 = new Dustbin("40", "12/09/19 10:14 AM");
        Dustbin d5 = new Dustbin("10", "12/09/19 10:14 AM");
        Dustbin d6 = new Dustbin("80", "12/09/19 10:14 AM");

        dustbinList.add(d1);
        dustbinList.add(d2);
        dustbinList.add(d3);
        dustbinList.add(d4);
        dustbinList.add(d5);
        dustbinList.add(d6);

        dustbinsAdapter.notifyDataSetChanged();*/
        wardsSpinner.setOnItemSelectedListener(this);


    }

    private void setSpinnerItems(){
        if(wardList.size() == 0){
            defaultEmptyWardTv.setVisibility(View.VISIBLE);
        } else {
            ArrayAdapter<Ward> statesDataAdapter = new ArrayAdapter<Ward>(DustbinListActivity.this, R.layout.spinner_item, wardList);
            statesDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            wardsSpinner.setAdapter(statesDataAdapter);
            wardsSpinner.setSelection(0, true);
        }


    }

    private void setDustbinList(){
        if(dustbinList.size() == 0)
            defaultTv.setVisibility(View.VISIBLE);
        dustbinsAdapter.setItems(dustbinList);
        dustbinsAdapter.notifyDataSetChanged();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(receiver.isConnected()){
            recyclerView.setVisibility(GONE);
            dustbinList.clear();
            progressBar.setVisibility(View.VISIBLE);
            defaultTv.setVisibility(GONE);
            defaultEmptyWardTv.setVisibility(GONE);
            Ward ward = wardList.get(pos);
            wardId = ward.getId();
            pageCount = 0;
            loadDustbinList(wardId, pageCount, NO_OF_ITEMS_IN_RECYCLER_VIEW);
        } else
            Toast.makeText(DustbinListActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void loadDustbinList(String wardId, int pageCount, int noOfItems){

        StringBuilder urlSb = new StringBuilder(API.BASE);
        urlSb.append(API.DUSTBIN_LIST);
        urlSb.append("?wardId=");
        urlSb.append(wardId);
        urlSb.append("&page=");
        urlSb.append(pageCount);
        urlSb.append("&size=");
        urlSb.append(noOfItems);

        progressBar.setVisibility(View.VISIBLE);
        contentLayout.setWeightSum(7);

        final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlSb.toString(),  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, " onResponse: " + response);
                progressBar.setVisibility(GONE);
                contentLayout.setWeightSum(6);
                recyclerView.setVisibility(View.VISIBLE);
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    totalPages = Integer.parseInt(jsonObject.get("totalPages").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List<Dustbin> dustbins = Jsonparser.responseStringToDustbinList(response);

                dustbinList.addAll(dustbins);
                setDustbinList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, " onErrorResponse: " + error.toString());
                if(error.networkResponse != null){
                    onError(error.networkResponse.statusCode);
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }

        };

        SmartDustbinApplication.getInstance().addToRequestQueue(stringRequest);
    }

    private void loadWardList() {
        final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.BASE + API.WARDS_LIST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, " onResponse: " + response);
                wardList = Jsonparser.responseStringToWardList(response);
                setSpinnerItems();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, " onErrorResponse: " + error.toString());
                if(error.networkResponse != null){
                    onError(error.networkResponse.statusCode);
                }
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }

        };

        SmartDustbinApplication.getInstance().addToRequestQueue(stringRequest);
    }

    public void onError(int status) {
        if(status == HttpStatus.UNAUTHORIZED.value()){
            Toast.makeText(DustbinListActivity.this, "Please login to perform this action.", Toast.LENGTH_SHORT).show();
            SmartDustbinApplication.getInstance().getDefaultSharedPreferences().edit().clear().apply();
            Intent login = new Intent(DustbinListActivity.this, LoginActivity.class);
            finishAffinity();
            startActivity(login);
        } else{
            Toast.makeText(DustbinListActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DustbinListActivity.this, UserHomeActivity.class);
            finish();
            startActivity(intent);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dustbin_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter: {
                String[] choices = {"25% and below", "25% - 75%", "75% and above", "Faulty dustbins"};

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DustbinListActivity.this)
                        .setTitle("Filter by Garbage level");
                alertDialogBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(receiver.isConnected()){
                            recyclerView.setVisibility(GONE);
                            dustbinList.clear();
                            progressBar.setVisibility(View.VISIBLE);
                            defaultTv.setVisibility(GONE);
                            defaultEmptyWardTv.setVisibility(GONE);
                            pageCount = 0;
                            if(wardList.size() == 0)
                                defaultEmptyWardTv.setVisibility(View.VISIBLE);
                            else
                                loadDustbinList(wardList.get(0).getId(), pageCount, NO_OF_ITEMS_IN_RECYCLER_VIEW );
                        } else
                            Toast.makeText(DustbinListActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();

                    }
                });
                alertDialogBuilder.setNegativeButton("Cancel", null)
                        .setSingleChoiceItems(choices, 0,null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            }
            case R.id.map: {
                if(dustbinList.size() == 0){
                    Toast.makeText(DustbinListActivity.this, "No dustbins to show on map", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DustbinListActivity.this, "Please wait for a while.  ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DustbinListActivity.this, AllDustbinActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("dustbin_list", (ArrayList<? extends Parcelable>) dustbinList);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        DustbinListActivity.this.unregisterReceiver(receiver);
    }

}
