package com.jss.smartdustbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jss.smartdustbin.API;
import com.jss.smartdustbin.R;
import com.jss.smartdustbin.model.Ward;
import com.jss.smartdustbin.utils.HttpStatus;
import com.jss.smartdustbin.utils.Jsonparser;
import com.jss.smartdustbin.utils.NetworkReceiver;
import com.jss.smartdustbin.utils.SmartDustbinApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jss.smartdustbin.activity.LoginActivity.LOG_TAG;

public class RegistrationExtraDataActivity extends AppCompatActivity {

    String qrCodeResult;
    Spinner wardsSpinner;
    EditText landmarkEditText;
    Button continueBt;
    String selectedWardId;
    View progressBar;
    List<Ward> wardList;
    private NetworkReceiver receiver;
    String landMark;
    EditText binEditText;
    View emptyWardDefaultTv;
    TextView notAllowedTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_extra_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        qrCodeResult = getIntent().getStringExtra("code");
        setTitle("Register");

        receiver = new NetworkReceiver();

        wardsSpinner = findViewById(R.id.spinner_wards);
        landmarkEditText = findViewById(R.id.et_landmark);
        progressBar = findViewById(R.id.progress_bar);
        binEditText = findViewById(R.id.et_bin);
        emptyWardDefaultTv = findViewById(R.id.empty_ward_list_default_tv);
        notAllowedTv = findViewById(R.id.registration_not_allowed);

        continueBt = findViewById(R.id.btn_continue);
        wardList = new ArrayList<>();
        if(qrCodeResult != null){
            binEditText.setText(qrCodeResult);
        }


        continueBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                landMark = landmarkEditText.getText().toString();
                if (wardsSpinner.getSelectedItem()==null|| landMark.isEmpty()) {
                    Toast toast = Toast.makeText(RegistrationExtraDataActivity.this, "Enter all fields!", Toast.LENGTH_SHORT);
                    toast.show();
                }else {
                    landMark = landmarkEditText.getText().toString();
                    selectedWardId = wardList.get(wardsSpinner.getSelectedItemPosition()).getId();
                    qrCodeResult = binEditText.getText().toString();
                    Intent intent = new Intent(RegistrationExtraDataActivity.this, MapsActivity.class);
                    intent.putExtra("code", qrCodeResult);
                    intent.putExtra("ward_id", selectedWardId);
                    intent.putExtra("landmark", landMark);
                    startActivity(intent);
                }
            }
        });

        if (receiver.isConnected()) {
            fetchWardList();
        }
        else {
            Toast.makeText(RegistrationExtraDataActivity.this, "No internet!", Toast.LENGTH_SHORT).show();
        }


    }

    private void fetchWardList() {
        final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API.BASE + API.WARDS_LIST , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, " onResponse: " + response);
                progressBar.setVisibility(View.GONE);
                wardList = Jsonparser.responseStringToWardList(response);
                if(wardList.size() == 0){
                    emptyWardDefaultTv.setVisibility(View.VISIBLE);
                    notAllowedTv.setText("Registration can't be proceeded further as wards has not been allocated yet !");
                }
                ArrayAdapter<Ward> wardsDataAdapter = new ArrayAdapter<Ward>(RegistrationExtraDataActivity.this, R.layout.spinner_item, wardList);
                wardsDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                wardsSpinner.setAdapter(wardsDataAdapter);

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
            Toast.makeText(RegistrationExtraDataActivity.this, "Please login to perform this action.", Toast.LENGTH_SHORT).show();
            SmartDustbinApplication.getInstance().getDefaultSharedPreferences().edit().clear().apply();
            Intent login = new Intent(RegistrationExtraDataActivity.this, LoginActivity.class);
            finishAffinity();
            startActivity(login);
        } else{
            Toast.makeText(RegistrationExtraDataActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistrationExtraDataActivity.this, UserHomeActivity.class);
            finish();
            startActivity(intent);
        }
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
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        RegistrationExtraDataActivity.this.unregisterReceiver(receiver);
    }



}
