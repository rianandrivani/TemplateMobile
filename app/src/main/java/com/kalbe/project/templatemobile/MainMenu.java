package com.kalbe.project.templatemobile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.kalbe.project.templatemobile.BL.clsActivity;
import com.kalbe.project.templatemobile.Common.clsLogin;
import com.kalbe.project.templatemobile.Common.clsPhotoProfile;
import com.kalbe.project.templatemobile.Common.mMenuData;
import com.kalbe.project.templatemobile.Common.mProduct;
import com.kalbe.project.templatemobile.Common.tOrderDetail;
import com.kalbe.project.templatemobile.Common.tOrderHeader;
import com.kalbe.project.templatemobile.Data.DatabaseHelper;
import com.kalbe.project.templatemobile.Data.DatabaseManager;
import com.kalbe.project.templatemobile.Data.clsHardCode;
import com.kalbe.project.templatemobile.Repo.clsLoginRepo;
import com.kalbe.project.templatemobile.Repo.clsPhotoProfilRepo;
import com.kalbe.project.templatemobile.Repo.mConfigRepo;
import com.kalbe.project.templatemobile.Repo.mMenuRepo;
import com.kalbe.project.templatemobile.Repo.mProductRepo;
import com.kalbe.project.templatemobile.Repo.tOrderDetailRepo;
import com.kalbe.project.templatemobile.Repo.tOrderHeaderRepo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rian Andrivani on 11/22/2017.
 */

public class MainMenu extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
    List<clsLogin> dataLogin = null;
    List<mMenuData> dataMenu = null;
    List<clsPhotoProfile> dataImageProfile = null;
    clsLoginRepo loginRepo;
    mMenuRepo menuRepo;
    clsPhotoProfilRepo repoUserImageProfile;

    PackageInfo pInfo = null;
    int selectedId;
    Boolean isSubMenu = false;
    String[] listMenu;
    String[] linkMenu;
    private static final int CAMERA_REQUEST_PROFILE = 120;
    private static final String IMAGE_DIRECTORY_NAME = "Image Personal";
    final int SELECT_FILE_PROFILE = 6;
    private static Bitmap photoProfile, mybitmapImageProfile;
    private static byte[] phtProfile;
    final int PIC_CROP_PROFILE = 5;
    private static ByteArrayOutputStream output = new ByteArrayOutputStream();

    private TextView tvUsername, tvEmail;
    CircleImageView ivProfile;
    private Uri uriImage, selectedImage;

    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;

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
        selectedId = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color_theme));
        }
        try {
            pInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        setSupportActionBar(toolbar);

//        addProductAndOrder();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View vwHeader = navigationView.getHeaderView(0);
        ivProfile = (CircleImageView) vwHeader.findViewById(R.id.profile_image);
        tvUsername = (TextView) vwHeader.findViewById(R.id.username);
        tvEmail = (TextView) vwHeader.findViewById(R.id.email);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(AppIndex.API).build();
        mGoogleApiClient.connect();

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);

        phtProfile = null;

        if (photoProfile != null) {
            ivProfile.setImageBitmap(photoProfile);
            photoProfile.compress(Bitmap.CompressFormat.PNG, 100, output);
            phtProfile = output.toByteArray();
        }

        try {
            repoUserImageProfile = new clsPhotoProfilRepo(getApplicationContext());
            dataImageProfile = (List<clsPhotoProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (dataImageProfile.size() > 0) {
            viewImageProfile();
        }

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageProfile();
            }
        });

        try {
            loginRepo = new clsLoginRepo(getApplicationContext());
            dataLogin = (List<clsLogin>) loginRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUsername.setText(new clsActivity().greetings() + dataLogin.get(0).txtUsername.toString());
        tvEmail.setText("yourEmail@email.com");

        String linkAPI = new mConfigRepo(getApplicationContext()).API_menu;
        try {
            URL u = new URL(linkAPI);
            linkAPI = u.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Menu header = navigationView.getMenu();
        SubMenu subMenuVersion = header.addSubMenu(R.id.groupVersion, 0, 3, "Version");
        try {
            subMenuVersion.add(getPackageManager().getPackageInfo(getPackageName(), 0).versionName + " \u00a9 KN-IT").setIcon(R.mipmap.ic_android).setEnabled(false);
            subMenuVersion.add(linkAPI).setIcon(R.mipmap.ic_link).setEnabled(false);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // get menu from db SQLite
        int menuActive = 0;
        menuActive = R.id.groupMenuDinamis;
        try {
            menuRepo = new mMenuRepo(getApplicationContext());
            dataMenu = (List<mMenuData>) menuRepo.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        linkMenu = new String[dataMenu.size()];
        listMenu = new String[dataMenu.size()];

        for (int i = 0; i < dataMenu.size(); i++) {
            int resId = getResources().getIdentifier(String.valueOf(dataMenu.get(i).txtDescription.toLowerCase()), "drawable", MainMenu.this.getPackageName());
            Drawable icon = MainMenu.this.getResources().getDrawable(resId);

            header.add(menuActive, i, 1, dataMenu.get(i).getTxtMenuName()).setIcon(icon).setCheckable(true);

            linkMenu[i] = dataMenu.get(i).getTxtLink();
            listMenu[i] = dataMenu.get(i).getTxtMenuName();
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                Fragment fragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainMenu.this);

                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure ?");

                        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog dialog2 = new ProgressDialog(MainMenu.this, ProgressDialog.STYLE_SPINNER);
                                dialog2.setIndeterminate(true);
                                dialog2.setMessage("Logging out...");
                                dialog2.setCancelable(false);
                                dialog2.show();

                                new Handler().postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                // On complete call either onLoginSuccess or onLoginFailed
                                                logout();
                                                // onLoginFailed();
                                                dialog2.dismiss();
                                            }
                                        }, 3000);
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        android.app.AlertDialog alert = builder.create();
                        alert.show();
                        return true;

                    case R.id.home:
                        toolbar.setTitle("Home");

                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                        // fragment yang dituju
//                        FragmentInfoContact ContactFragment = new FragmentInfoContact();
//                        FragmentTransaction fragmentTransactionHome = getSupportFragmentManager().beginTransaction();
//                        fragmentTransactionHome.replace(R.id.frame, ContactFragment);
//                        fragmentTransactionHome.commit();
                        selectedId = 99;

                        return true;

                    default:
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                        try {
                            Class<?> fragmentClass = Class.forName(linkMenu[menuItem.getItemId()]);
                            try {
                                toolbar.setTitle(menuItem.getTitle().toString());
                                toolbar.setSubtitle(null);

                                fragment = (Fragment) fragmentClass.newInstance();
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, fragment);
                                fragmentTransaction.addToBackStack(fragment.getClass().getName());
                                fragmentTransaction.commit();
                                selectedId = menuItem.getItemId();
                                isSubMenu = false;

                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        return true;
                }
            }
        });

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    // put image from camera
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_PROFILE) {
            if (resultCode == -1) {
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    String uri = uriImage.getPath().toString();

                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropProfile();

//                    previewCaptureImage2(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if (resultCode == 0) {
                Toast.makeText(getApplicationContext(), "User cancel take image", Toast.LENGTH_SHORT).show();
            }  else {
                try {
                    photoProfile = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == PIC_CROP_PROFILE) {
            if (resultCode == -1) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");

                previewCaptureImageProfile(thePic);
            } else if (resultCode == 0) {
                Toast.makeText(getApplicationContext(), "User cancel take image", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == SELECT_FILE_PROFILE) {
            if(resultCode == RESULT_OK){
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    selectedImage = data.getData();
                    String uri = selectedImage.getPath().toString();
                    bitmap = BitmapFactory.decodeFile(uri, bitmapOptions);

                    performCropGalleryProfile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void logout() {
        Intent intent = new Intent(MainMenu.this, SplashActivity.class);
        DatabaseHelper helper = DatabaseManager.getInstance().getHelper();
        helper.clearDataAfterLogout();
        finish();
        startActivity(intent);
    }

    private void selectImageProfile() {
        final CharSequence[] items = { "Ambil Foto", "Pilih dari Galeri",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getApplicationContext());
                if (items[item].equals("Ambil Foto")) {
                    if(result)
                        captureImageProfile();
                } else if (items[item].equals("Pilih dari Galeri")) {
                    if(result)
                        galleryIntentProfile();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    protected void viewImageProfile() {
        try {
            repoUserImageProfile = new clsPhotoProfilRepo(getApplicationContext());
            dataImageProfile = (List<clsPhotoProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        File folder = new File(Environment.getExternalStorageDirectory().toString() + "/data/data/KalbeFamily/tempdata/Foto_Profil");
        folder.mkdir();

        for (clsPhotoProfile imgDt : dataImageProfile){
            final byte[] imgFile = imgDt.getTxtImg();
            if (imgFile != null) {
                mybitmapImageProfile = BitmapFactory.decodeByteArray(imgFile, 0, imgFile.length);
                Bitmap bitmap = Bitmap.createScaledBitmap(mybitmapImageProfile, 150, 150, true);
                ivProfile.setImageBitmap(bitmap);
            }
        }
    }

    // preview image profile
    private void previewCaptureImageProfile(Bitmap photo){
        try {
            Bitmap bitmap = new clsActivity().resizeImageForBlob(photo);
            ivProfile.setVisibility(View.VISIBLE);
            output = null;
            try {
                output = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, output);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (output != null){
                        output.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bitmap photo_view = Bitmap.createScaledBitmap(bitmap, 150, 150, true);
            phtProfile = output.toByteArray();
            ivProfile.setImageBitmap(photo_view);

            saveImageProfile();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void saveImageProfile() {
        try {
            repoUserImageProfile = new clsPhotoProfilRepo(getApplicationContext());
            dataImageProfile = (List<clsPhotoProfile>) repoUserImageProfile.findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        clsPhotoProfile data = new clsPhotoProfile();
        data.setTxtGuiId("1");
        data.setTxtDescription("Profile");
        data.setTxtImg(phtProfile);

        repoUserImageProfile.createOrUpdate(data);
        Toast.makeText(getApplicationContext(), "Image Profile Saved", Toast.LENGTH_SHORT).show();
    }

    protected void captureImageProfile() {
        uriImage = getOutputMediaFileUri();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_PROFILE);
    }

    private void galleryIntentProfile() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , SELECT_FILE_PROFILE);//one can be replaced with any action code
    }

    private void performCropProfile(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(uriImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_PROFILE);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCropGalleryProfile(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(selectedImage, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP_PROFILE);
        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    private File getOutputMediaFile() {
        // External sdcard location

        File mediaStorageDir = new File(new clsHardCode().txtFolderData + File.separator);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "tmp_act"  + ".png");
        return mediaFile;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        builder.build()
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:

                // NO need to show the dialog;

                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                //  Location settings are not satisfied. Show the user a dialog

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().

                    status.startResolutionForResult(MainMenu.this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    //failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any dialog now
                break;
        }
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>= Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    protected void addProductAndOrder() {
        mProduct data = new mProduct();
        data.setTxtGuiId("1");
        data.setTxtProductCode("ENT");
        data.setTxtProductName("Entrasol");
        data.setTxtPrice((double) 18000);

        mProduct data2 = new mProduct();
        data2.setTxtGuiId("2");
        data2.setTxtProductCode("PREN");
        data2.setTxtProductName("Prenagen");
        data2.setTxtPrice((double) 25000);

        mProduct data3 = new mProduct();
        data3.setTxtGuiId("3");
        data3.setTxtProductCode("MIL");
        data3.setTxtProductName("Milna Biskuit");
        data3.setTxtPrice((double) 5000);

        mProduct data4 = new mProduct();
        data4.setTxtGuiId("4");
        data4.setTxtProductCode("FTBR");
        data4.setTxtProductName("Fitbar");
        data4.setTxtPrice((double) 3000);

        mProduct data5 = new mProduct();
        data5.setTxtGuiId("5");
        data5.setTxtProductCode("DTBSL");
        data5.setTxtProductName("Diabetasol");
        data5.setTxtPrice((double) 12000);

        mProduct data6 = new mProduct();
        data6.setTxtGuiId("6");
        data6.setTxtProductCode("CHLG");
        data6.setTxtProductName("Chilgo");
        data6.setTxtPrice((double) 4500);

        mProduct data7 = new mProduct();
        data7.setTxtGuiId("10");
        data7.setTxtProductCode("BNCL");
        data7.setTxtProductName("Benecol");
        data7.setTxtPrice((double) 3000);

        mProduct data8 = new mProduct();
        data8.setTxtGuiId("11");
        data8.setTxtProductCode("ZE");
        data8.setTxtProductName("Zee");
        data8.setTxtPrice((double) 15000);

        mProductRepo productRepo = new mProductRepo(getApplicationContext());
        productRepo.createOrUpdate(data);
        productRepo.createOrUpdate(data2);
        productRepo.createOrUpdate(data3);
        productRepo.createOrUpdate(data4);
        productRepo.createOrUpdate(data5);
        productRepo.createOrUpdate(data6);
        productRepo.createOrUpdate(data7);
        productRepo.createOrUpdate(data8);

        tOrderHeader dt = new tOrderHeader();
        dt.setTxtGuiId("1");
        dt.setTxtNoTransaksi("A-3311");

        tOrderDetail dta = new tOrderDetail();
        dta.setTxtGuiId(new clsActivity().GenerateGuid());
        dta.setTxtHeaderID("1");
        dta.setTxtQuantity("3");
        dta.setTxtTotalPrice(Double.valueOf("75000"));
        dta.setProduct(data);

        tOrderDetail dta2 = new tOrderDetail();
        dta2.setTxtGuiId(new clsActivity().GenerateGuid());
        dta2.setTxtHeaderID("1");
        dta2.setTxtQuantity("1");
        dta2.setTxtTotalPrice(Double.valueOf("18000"));
        dta2.setProduct(data2);

        tOrderDetail dta3 = new tOrderDetail();
        dta3.setTxtGuiId(new clsActivity().GenerateGuid());
        dta3.setTxtHeaderID("1");
        dta3.setTxtQuantity("4");
        dta3.setTxtTotalPrice(Double.valueOf("18000"));
        dta3.setProduct(data3);

        tOrderDetail dta4 = new tOrderDetail();
        dta4.setTxtGuiId(new clsActivity().GenerateGuid());
        dta4.setTxtHeaderID("1");
        dta4.setTxtQuantity("2");
        dta4.setTxtTotalPrice(Double.valueOf("18000"));
        dta4.setProduct(data8);

        tOrderDetail dta5 = new tOrderDetail();
        dta5.setTxtGuiId(new clsActivity().GenerateGuid());
        dta5.setTxtHeaderID("1");
        dta5.setTxtQuantity("2");
        dta5.setTxtTotalPrice(Double.valueOf("18000"));
        dta5.setProduct(data7);

        tOrderHeaderRepo orderHeaderRepo = new tOrderHeaderRepo(getApplicationContext());
        tOrderDetailRepo orderDetailRepo = new tOrderDetailRepo(getApplicationContext());

        orderHeaderRepo.createOrUpdate(dt);
        orderDetailRepo.createOrUpdate(dta);
        orderDetailRepo.createOrUpdate(dta2);
        orderDetailRepo.createOrUpdate(dta3);
        orderDetailRepo.createOrUpdate(dta4);
        orderDetailRepo.createOrUpdate(dta5);
    }
}
