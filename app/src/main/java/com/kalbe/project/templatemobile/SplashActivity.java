package com.kalbe.project.templatemobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kalbe.project.templatemobile.BL.clsMainBL;
import com.kalbe.project.templatemobile.Common.clsStatusMenuStart;
import com.kalbe.project.templatemobile.Repo.enumStatusMenuStart;
import com.kalbe.project.templatemobile.Repo.mConfigRepo;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    long delay = 5000;
    private TextView version;
    boolean firstStart;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    PackageInfo pInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = (TextView) findViewById(R.id.tv_version);
        version.setText(pInfo.versionName.toString());
        version.setGravity(Gravity.CENTER | Gravity.BOTTOM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCameraPermission = ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= 23
                && hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                && hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED
                && hasAccessFineLocationPermission != PackageManager.PERMISSION_GRANTED
                && hasCameraPermission != PackageManager.PERMISSION_GRANTED
                ) {
            boolean checkPermission = checkPermission();

        } else if (Build.VERSION.SDK_INT >= 23
                && hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED
                && hasAccessFineLocationPermission == PackageManager.PERMISSION_GRANTED
                && hasCameraPermission == PackageManager.PERMISSION_GRANTED
                ){
            StartAnimations();
            checkStatusMenu();

        } else if (Build.VERSION.SDK_INT >= 23
                && hasCameraPermission != PackageManager.PERMISSION_GRANTED){
            boolean checkPermission = checkPermission();
        } else {
            StartAnimations();
            checkStatusMenu();
        }
        //StartAnimations();
        //checkStatusMenu();
    }

    private boolean checkPermission() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setMessage("You need to allow access. . .");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        && !ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        &&!ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this,
                        Manifest.permission.CAMERA)){
                    ActivityCompat.requestPermissions(SplashActivity.this,
                            new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE},
                            REQUEST_CODE_ASK_PERMISSIONS);
                    dialog.dismiss();

                }
                ActivityCompat.requestPermissions(SplashActivity.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE},
                        REQUEST_CODE_ASK_PERMISSIONS);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

        return true;
    }

    private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView iv = (TextView) findViewById(R.id.iv_anim);
        iv.clearAnimation();
        iv.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        anim.reset();
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);
        iv2.setBackgroundResource(R.drawable.ic_kalbe);
        iv2.clearAnimation();
        iv2.startAnimation(anim);
    }

    private void checkStatusMenu() {
        Timer runProgress = new Timer();
        TimerTask viewTask = new TimerTask() {

            public void run() {
                Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                clsStatusMenuStart _clsStatusMenuStart = null;

                try {
                    _clsStatusMenuStart = new clsMainBL().checkUserActive(getApplicationContext());
                    if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.FormLogin) {
                        myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    } else if (_clsStatusMenuStart.get_intStatus() == enumStatusMenuStart.UserActiveLogin) {
                    myIntent = new Intent(getApplicationContext(), MainMenu.class);
//                    myIntent.putExtra("key_view", "home_menu");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    new mConfigRepo(getApplicationContext()).InsertDefaultmConfig();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                finish();
                startActivity(myIntent);
            }
        };
        runProgress.schedule(viewTask, delay);
    }
}
