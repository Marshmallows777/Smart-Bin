package com.jss.smartdustbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jss.smartdustbin.API;
import com.jss.smartdustbin.R;
import com.jss.smartdustbin.utils.HttpStatus;
import com.jss.smartdustbin.utils.LocationTrack;
import com.jss.smartdustbin.utils.NetworkReceiver;
import com.jss.smartdustbin.utils.SmartDustbinApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Timer;

public class ScanResultActivity extends AppCompatActivity {

    Button btnDone;
    String barCodeResult;
    ProgressDialog progressDialog;
    private static final String TAG =  ScanResultActivity.class.getSimpleName();
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;
    double longitude, latitude;

    NetworkReceiver receiver;
    boolean success = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        barCodeResult = getIntent().getStringExtra("code");

        setTitle("Register");
        receiver = new NetworkReceiver();

        btnDone = findViewById(R.id.btn_done);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receiver.isConnected()){
                    progressDialog = new ProgressDialog(ScanResultActivity.this);
                    progressDialog.setTitle("Confirming registration");
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    confirmRegistration();
                } else
                    Toast.makeText(ScanResultActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void confirmRegistration() {

        final Thread fetchRegistrationConfirmationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");

                while(!success){
                    try {
                        httpRequest(accessToken, barCodeResult);
                        Thread.sleep(2000);

                    }
                    catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        fetchRegistrationConfirmationThread.start();
    }

    private void httpRequest(String accessToken, String bin) throws IOException, JSONException {
        URL url = new URL(API.BASE + API.CONFIRM_REGISTRATION + "?bin=" + bin + "&q=xyz");

        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty ("Authorization", "Bearer " + accessToken);
        /*httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoInput(false);
        httpURLConnection.setDoOutput(false);*/
        int responseCode = httpURLConnection.getResponseCode();


        //Log.v(TAG, "Activation Response" + sb.toString());

        Log.e(TAG, "response code-----------" + responseCode);
        if (responseCode == HttpStatus.OK.value()) {
            Log.v(TAG, "json object created");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String response;
            while ((response = bufferedReader.readLine()) != null) {
                sb.append(response);
            }
            JSONObject jsonObject = new JSONObject(sb.toString());
            String status = jsonObject.getString("status");
            if(status.equals("active")){
                success = true;
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        progressDialog.hide();
                        Intent intent = new Intent(ScanResultActivity.this, RegistrationConfirmationActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
            }


        } else{
            success = false;
            onError(httpURLConnection.getResponseCode());
        }
    }

    public void onError(int status) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                if (status == HttpStatus.UNAUTHORIZED.value()) {
                    Toast.makeText(ScanResultActivity.this, "Please login to perform this action.", Toast.LENGTH_SHORT).show();
                    SmartDustbinApplication.getInstance().getDefaultSharedPreferences().edit().clear().apply();
                    success = true;
                    Intent login = new Intent(ScanResultActivity.this, LoginActivity.class);
                    finishAffinity();
                    startActivity(login);
                } else{
                    Toast.makeText(ScanResultActivity.this, "Please try again.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScanResultActivity.this, MapsActivity.class);
                    finishAffinity();
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //locationTrack.stopListener();
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
        ScanResultActivity.this.unregisterReceiver(receiver);
    }


}
