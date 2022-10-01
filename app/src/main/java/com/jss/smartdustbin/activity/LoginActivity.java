package com.jss.smartdustbin.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jss.smartdustbin.API;
import com.jss.smartdustbin.utils.NetworkReceiver;
import com.jss.smartdustbin.R;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jss.smartdustbin.utils.SmartDustbinApplication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    public static final String LOG_TAG = LoginActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    private SharedPreferences pref;
    private NetworkReceiver receiver;
    EditText etUsername, etUserPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        receiver = new NetworkReceiver();
        pref = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);

        Button btLogin = findViewById(R.id.bt_login);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                etUsername = findViewById(R.id.et_login_username);
                etUserPassword = findViewById(R.id.et_login_password);
               /* if (etUserPassword.getText().toString().equals("123")){
                    SmartDustbinApplication.getInstance().setLogin(true);
                    startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                    finish();
                }*/
                hideKeyboard();
                if (receiver.isConnected()) {
                    String userName = etUsername.getText().toString();
                    String password = etUserPassword.getText().toString();
                    if (userName.isEmpty() || password.isEmpty()) {
                        Toast toast = Toast.makeText(LoginActivity.this, "Enter all fields!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else{
                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setTitle("Validating");
                        progressDialog.setMessage("Logging you in...");
                        etUsername.setEnabled(false);
                        etUserPassword.setEnabled(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        login(userName, password);
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "No internet!", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void login(final String username, final String password){
        StringRequest loginReq = new StringRequest(Request.Method.POST, API.BASE + API.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(LOG_TAG, " Login Activity onResponse: " + response);
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(response).getAsJsonObject();
                String accessToken = jsonObject.get("access_token").getAsString();
                String authorities = jsonObject.get("authorities").getAsString();
                //String[] authorityList = authorities.split(",");
               /* Set<String> authoritiesList = getAuthoritiesFromJsonObject(jsonObject);*/
                Log.e(LOG_TAG, "onResponse: " + accessToken);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("access_token", accessToken);
                editor.putString("authority_list", authorities);
                // //String[] authorityList = authorities.split(",");
                //use this method while getting string of authorities from sharedpref
                editor.apply();


                /*Map<String, String> responseMap = new Gson().fromJson(response, new TypeToken<Map<String, String>>() {}.getType());
                String accessToken = responseMap.get("access_token");
                Log.e(LOG_TAG, "onResponse: " + accessToken);
                SmartDustbinApplication.getInstance().setAccessToken(accessToken);
                SmartDustbinApplication.getInstance().setLogin(true);*/
                startActivity(new Intent(LoginActivity.this, UserHomeActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(LOG_TAG, " Login Activity onErrorResponse: " + error.toString());
                if (error.networkResponse!=null) {
                    String json = new String(error.networkResponse.data);
                    try {
                        JSONObject jsonError = new JSONObject(json);
                        if (jsonError.has("error")) {
                            String errorString = "Invalid data! Try again.";
                            Toast.makeText(LoginActivity.this, errorString, Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            etUsername.setEnabled(true);
                            etUserPassword.setEnabled(true);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Toast.makeText(LoginActivity.this, "Network issue! Try again.", Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                    etUsername.setEnabled(true);
                    etUserPassword.setEnabled(true);
                }
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                params.put("grant_type", "password");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String credentials = API.USERNAME + ":" + API.PASSWORD;
                String encoding = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                Map<String,String> params = new HashMap<>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Basic " + encoding);

                return params;
            }
        };
        SmartDustbinApplication.getInstance().addToRequestQueue(loginReq);
        /*RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(loginReq);*/

    }


    private Set<String> getAuthoritiesFromJsonObject(JsonObject jsonObject) {
        JsonArray jsonArray = jsonObject.getAsJsonArray("authorities");
        Set<String> authorities = new HashSet<>();
        for(int i = 0; i < jsonArray.size(); i++){
            JsonObject authorityObject = jsonArray.get(i).getAsJsonObject();
            String authority = authorityObject.get("authority").getAsString();
            authorities.add(authority);
        }
        return authorities;
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
        LoginActivity.this.unregisterReceiver(receiver);
    }

}



