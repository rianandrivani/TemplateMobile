package com.kalbe.project.templatemobile;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Common.clsToken;
import com.kalbe.project.templatemobile.Common.mConfigData;
import com.kalbe.project.templatemobile.Common.mMenuData;
import com.kalbe.project.templatemobile.Data.VolleyResponseListener;
import com.kalbe.project.templatemobile.Data.VolleyUtils;
import com.kalbe.project.templatemobile.Data.clsHardCode;
import com.kalbe.project.templatemobile.Repo.clsLoginRepo;
import com.kalbe.project.templatemobile.Repo.clsTokenRepo;
import com.kalbe.project.templatemobile.Repo.mConfigRepo;
import com.kalbe.project.templatemobile.Repo.mMenuRepo;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class LoginActivity extends Activity {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    EditText etUsername, etPassword;
    String txtUsername, txtPassword, imeiNumber, deviceName, access_token;
    String clientId = "";
    Button btnSubmit, btnExit;
    Spinner spnRole;

    private int intSet = 1;
    int intProcesscancel = 0;

    List<clsToken> dataToken;
    clsLoginRepo loginRepo;
    clsTokenRepo tokenRepo;
    mMenuRepo menuRepo;

    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Are you sure to exit?");

        builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }

        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);

            imeiNumber = tm.getDeviceId().toString();
            deviceName = android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL;
        } else {
            //TODO
            imeiNumber = tm.getDeviceId().toString();
            deviceName = android.os.Build.MANUFACTURER+" "+android.os.Build.MODEL;
        }

        etUsername = (EditText) findViewById(R.id.editTextUsername);
        etPassword = (EditText) findViewById(R.id.editTextPass);
        btnSubmit = (Button) findViewById(R.id.buttonLogin);
        btnExit = (Button) findViewById(R.id.buttonExit);
        spnRole = (Spinner) findViewById(R.id.spnRole);
        final CircularProgressView progressView = (CircularProgressView) findViewById(R.id.progress_view);

        try {
            loginRepo = new clsLoginRepo(getApplicationContext());
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
            if (dataToken.size() == 0) {
                requestToken();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Spinner Drop down elements
        final List<String> roleName = new ArrayList<String>();
        roleName.add("Select One");

        etUsername.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    intProcesscancel = 0;
                    roleName.add("Example - Role 1");

                    return true;
                }
                return false;
            }
        });

        // Creating adapter for spinnerTelp
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roleName);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Initializing an ArrayAdapter with initial text like select one
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_spinner_dropdown_item, roleName){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // attaching data adapter to spinner
        spnRole.setAdapter(spinnerArrayAdapter);

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // put code here
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // put code here
            }
        });

        etPassword.setOnTouchListener(new DrawableClickListener.RightDrawableClickListener(etPassword) {
            public boolean onDrawableClick() {
                if (intSet == 1) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    intSet = 0;
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    intSet = 1;
                }

                return true;
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                progressView.setVisibility(View.VISIBLE);
//                progressView.startAnimation();

                if (etUsername.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Username tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }else if (etPassword.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    popupSubmit();
                }
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this);

                builder.setTitle("Exit");
                builder.setMessage("Are you sure to exit?");

                builder.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                android.app.AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void popupSubmit() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are You sure?");

        builder.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                login();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    // sesuaikan username dan password dengan data di server
    private void login() {
        txtUsername = etUsername.getText().toString();
        txtPassword = etPassword.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        final String now = dateFormat.format(cal.getTime()).toString();
        final String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
        String strLinkAPI = new clsHardCode().linkLogin;
        JSONObject resJson = new JSONObject();

        try {
            tokenRepo = new clsTokenRepo(getApplicationContext());
            dataToken = (List<clsToken>) tokenRepo.findAll();
            resJson.put("txtUsername", txtUsername);
            resJson.put("txtPassword", txtPassword);
            resJson.put("txtRefreshToken", dataToken.get(0).txtRefreshToken.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        final String mRequestBody = resJson.toString();

        volleyLogin(strLinkAPI, mRequestBody, "Please Wait....", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsn = jsonObject.getJSONObject("result");
                        String warn = jsn.getString("txtMessage");
                        String result = jsn.getString("intResult");

                        if (result.equals("1")){
                            clsLogin data = new clsLogin();
                            data.setTxtGuiId("1");
                            data.setTxtUsername(txtUsername);
                            data.setTxtPassword(txtPassword);
                            data.setDtLogin(now);
                            data.setTxtImei(imeiNumber);
                            data.setTxtDeviceName(deviceName);

                            loginRepo.createOrUpdate(data);
                            Log.d("Data info", "Login Success");
                            listMenu();

                            Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                            finish();
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), warn, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void requestToken(){
        String username = "";
        String strLinkAPI = new clsHardCode().linkToken;

        mConfigRepo configRepo = new mConfigRepo(getApplicationContext());
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(4);
            mConfigData configDataUser = (mConfigData) configRepo.findById(5);
            username = configDataUser.getTxtDefaultValue().toString();
            clientId = configDataClient.getTxtDefaultValue().toString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new VolleyUtils().makeJsonObjectRequestToken(this, strLinkAPI, username, "", clientId, "Request Token, Please Wait", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        String accessToken = "";
                        String refreshToken = "";
                        JSONObject jsonObject = new JSONObject(response);
                        accessToken = jsonObject.getString("access_token");
                        refreshToken = jsonObject.getString("refresh_token");
                        String dtIssued = jsonObject.getString(".issued");

                        clsToken data = new clsToken();
                        data.setIntId("1");
                        data.setDtIssuedToken(dtIssued);
                        data.setTxtUserToken(accessToken);
                        data.setTxtRefreshToken(refreshToken);

                        tokenRepo.createOrUpdate(data);
                        Log.d("Data info", "get access_token & refresh_token, Success");

                        Toast.makeText(getApplicationContext(), "Ready For Login", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void volleyLogin(String strLinkAPI, final String mRequestBody, String progressBarType, final VolleyResponseListener listener) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        final String[] body = new String[1];
        final String[] message = new String[1];
        final ProgressDialog Dialog = new ProgressDialog(LoginActivity.this);
        Dialog.setMessage(progressBarType);
        Dialog.setCancelable(false);
        Dialog.show();

        final ProgressDialog finalDialog = Dialog;
        final ProgressDialog finalDialog1 = Dialog;

        mConfigRepo configRepo = new mConfigRepo(getApplicationContext());
        try {
            mConfigData configDataClient = (mConfigData) configRepo.findById(4);
            clientId = configDataClient.getTxtDefaultValue().toString();
            dataToken = (List<clsToken>) tokenRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringRequest request = new StringRequest(Request.Method.POST, strLinkAPI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Boolean status = false;
                String errorMessage = null;
                listener.onResponse(response, status, errorMessage);
                finalDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String strLinkAPI = new clsHardCode().linkToken;
                final String refresh_token = dataToken.get(0).txtRefreshToken;
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == HttpStatus.SC_UNAUTHORIZED) {
                    // HTTP Status Code: 401 Unauthorized
                    try {
                        // body for value error response
                        body[0] = new String(error.networkResponse.data,"UTF-8");
                        JSONObject jsonObject = new JSONObject(body[0]);
                        message[0] = jsonObject.getString("Message");
                        //Toast.makeText(context, "Error 401, " + message[0], Toast.LENGTH_SHORT).show();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new VolleyUtils().requestTokenWithRefresh(LoginActivity.this, strLinkAPI, refresh_token, clientId, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(String response, Boolean status, String strErrorMsg) {
                            if (response != null) {
                                try {
                                    String accessToken = "";
                                    String newRefreshToken = "";
                                    String refreshToken = "";
                                    JSONObject jsonObject = new JSONObject(response);
                                    accessToken = jsonObject.getString("access_token");
                                    refreshToken = jsonObject.getString("refresh_token");
                                    String dtIssued = jsonObject.getString(".issued");

                                    clsToken data = new clsToken();
                                    data.setIntId("1");
                                    data.setDtIssuedToken(dtIssued);
                                    data.setTxtUserToken(accessToken);
                                    data.setTxtRefreshToken(refreshToken);

                                    tokenRepo.createOrUpdate(data);
                                    Toast.makeText(getApplicationContext(), "Success get new Access Token", Toast.LENGTH_SHORT).show();
                                    newRefreshToken = refreshToken;
                                    if (refreshToken == newRefreshToken && !newRefreshToken.equals("")) {
                                        login();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    finalDialog1.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "Error 500, Server Error", Toast.LENGTH_SHORT).show();
                    finalDialog1.dismiss();
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                try {
                    tokenRepo = new clsTokenRepo(getApplicationContext());
                    dataToken = (List<clsToken>) tokenRepo.findAll();
                    access_token = dataToken.get(0).getTxtUserToken();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + access_token);

                return headers;
            }
        };
        request.setRetryPolicy(new
                DefaultRetryPolicy(60000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(request);
    }

    private void listMenu() {
        mMenuData menu = new mMenuData();
        menu.setIntId(Integer.parseInt("1"));
        menu.setIntOrder(1);
        menu.setIntParentID(109);
        menu.setTxtDescription("mn1");
        menu.setTxtLink("com.kalbe.project.templatemobile.FragmentFirstMenu");
        menu.setIntMenuID("220");
        menu.setTxtVisible("null");
        menu.setTxtIcon("null");
        menu.setTxtMenuName("Menu ke-1");

        mMenuData menu2 = new mMenuData();
        menu2.setIntId(Integer.parseInt("2"));
        menu2.setIntOrder(4);
        menu2.setIntParentID(47);
        menu2.setTxtDescription("mn2");
        menu2.setTxtLink("com.kalbe.project.templatemobile.FragmentSecondMenu");
        menu2.setIntMenuID("98");
        menu2.setTxtVisible("null");
        menu2.setTxtIcon("null");
        menu2.setTxtMenuName("Menu ke-2");

        menuRepo = new mMenuRepo(getApplicationContext());
        menuRepo.createOrUpdate(menu);
        menuRepo.createOrUpdate(menu2);
    }
}
