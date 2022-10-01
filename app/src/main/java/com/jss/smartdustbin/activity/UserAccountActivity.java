package com.jss.smartdustbin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jss.smartdustbin.API;
import com.jss.smartdustbin.model.User;
import com.jss.smartdustbin.R;
import com.jss.smartdustbin.utils.Helper;
import com.jss.smartdustbin.utils.HttpStatus;
import com.jss.smartdustbin.utils.Jsonparser;
import com.jss.smartdustbin.utils.SmartDustbinApplication;

import java.util.HashMap;
import java.util.Map;

public class UserAccountActivity extends AppCompatActivity {

    private static final String TAG =  UserAccountActivity.class.getSimpleName();
    TextView userFullName;
    TextView userGender;
    TextView userAge;
    TextView userContactNo;
    TextView userEmail;
    ProgressBar progressBar;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Account");

        userFullName = findViewById(R.id.user_name);
        userGender = findViewById(R.id.user_gender);
        userAge = findViewById(R.id.user_age);
        userContactNo = findViewById(R.id.user_phoneNo);
        userEmail = findViewById(R.id.user_email);

        progressBar = findViewById(R.id.progress_bar);
        linearLayout = findViewById(R.id.content);
        //fetchUserInfo();
    }

    private void fetchUserInfo() {
        progressBar.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);

        final String accessToken = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("access_token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, API.BASE + API.FCM_TOKEN_POST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, " onResponse: " + response);
                User user = Jsonparser.getUserFromResponse(response);
                userFullName.setText(user.getFirstName() + " " + user.getLastName());
                userAge.setText(user.getAge());
                userContactNo.setText(user.getContactNo());
                userEmail.setText(user.getEmail());
                userGender.setText(user.getGender());
                progressBar.setVisibility(View.INVISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, " onErrorResponse: " + error.toString());
                if(error.networkResponse != null){
                    onError(error.networkResponse.statusCode);
                }
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<>();
                return params;
            }

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
            Toast.makeText(UserAccountActivity.this, "Please login to perform this action.", Toast.LENGTH_SHORT).show();
            SmartDustbinApplication.getInstance().getDefaultSharedPreferences().edit().clear().apply();
            Intent login = new Intent(UserAccountActivity.this, LoginActivity.class);
            finishAffinity();
            startActivity(login);
        } else{
            Toast.makeText(UserAccountActivity.this, "Error fetching data, Please try again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UserAccountActivity.this, UserHomeActivity.class));
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

}
