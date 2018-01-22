package com.kalbe.project.templatemobile;

import android.Manifest;
import android.app.Activity;
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

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Common.mMenuData;
import com.kalbe.project.templatemobile.Data.VolleyResponseListener;
import com.kalbe.project.templatemobile.Data.VolleyUtils;
import com.kalbe.project.templatemobile.Data.clsHardCode;
import com.kalbe.project.templatemobile.Repo.clsLoginRepo;
import com.kalbe.project.templatemobile.Repo.mMenuRepo;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class LoginActivity extends Activity {
    private static final int REQUEST_READ_PHONE_STATE = 0;
    EditText etUsername, etPassword;
    String txtUsername, txtPassword, imeiNumber, deviceName, accessToken;;
    Button btnSubmit, btnExit;
    Spinner spnRole;

    private int intSet = 1;
    int intProcesscancel = 0;

    List<clsLogin> dataLogin;
    clsLoginRepo loginRepo;
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
                requestTokenAndLogin();
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
    private void requestTokenAndLogin() {
        txtUsername = etUsername.getText().toString();
        txtPassword = etPassword.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        final String now = dateFormat.format(cal.getTime()).toString();

        String strLinkAPI = new clsHardCode().linkToken;
        final String username = txtUsername;
        final String password = txtPassword;
        final String clientId = "z/iQZAGiEmA+ygHJ+UvmcA3Ij/xrAGQPYzwyp1FI9IE=";


        new VolleyUtils().makeJsonObjectRequestToken(this, strLinkAPI, username, password, clientId, "Request Token, Please Wait", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, Boolean status, String strErrorMsg) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        accessToken = jsonObject.getString("access_token");

                        try {
                            loginRepo = new clsLoginRepo(getApplicationContext());
                            dataLogin = (List<clsLogin>) loginRepo.findAll();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        clsLogin data = new clsLogin();
                        data.setTxtGuiId(String.valueOf(dataLogin.size() + 1));
                        data.setTxtUsername(txtUsername);
                        data.setTxtPassword(txtPassword);
                        data.setDtLogin(now);
                        data.setTxtImei(imeiNumber);
                        data.setTxtDeviceName(deviceName);
                        data.setTxtUserToken(accessToken);

                        loginRepo.createOrUpdate(data);
                        Log.d("Data info", "Login Success");
                        listMenu();

                        Intent intent = new Intent(LoginActivity.this, MainMenu.class);
                        finish();
                        startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
