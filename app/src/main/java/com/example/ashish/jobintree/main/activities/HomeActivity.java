package com.example.ashish.jobintree.main.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.ashish.jobintree.R;
import com.example.ashish.jobintree.main.SharedPrefManager;
import com.example.ashish.jobintree.main.fragments.AccountFragment;
import com.example.ashish.jobintree.main.fragments.HomeFragment;
import com.example.ashish.jobintree.main.fragments.RecommendationFragment;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private View view;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView tvLocation;
    private Location mLastLocation;

    private static final int ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CHECK_SETTINGS = 2;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
           Fragment fragment = null;
            switch (item.getItemId()) {

                case R.id.navigation_home:
                   // mTextMessage.setText(R.string.title_home);
                    view.setVisibility(View.VISIBLE);
                    fragment = new HomeFragment();
                    break;
                case R.id.navigation_recommended:
                    //mTextMessage.setText(R.string.title_dashboard);
                    view.setVisibility(View.GONE);
                    fragment = new RecommendationFragment();
                   break;
                case R.id.navigation_account:
                   // mTextMessage.setText(R.string.title_notifications);
                    view.setVisibility(View.GONE);
                    fragment = new AccountFragment();
                    break;
            }

            if (fragment!=null){
                //fragment = new HomeFragment();
               // view.setVisibility(View.VISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_home_activity,fragment);
                fragmentTransaction.commit();
            }
            return true;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(TAG, "onCreate: 1527652");

        if(SharedPrefManager.getInstance(this).isLoggedIn() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else {
            if(SharedPrefManager.getInstance(this).isResumeUploaded() == null){
                startActivity(new Intent(this,UploadResumeActivity.class));
                finish();
            }
        }

        setContentView(R.layout.activity_home);

        view = findViewById(R.id.view);
        setTitle("Job in Tree");
        getSupportActionBar().hide();

        //mTextMessage = (TextView) findViewById(R.id.message);

        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_home_activity, fragment);
        fragmentTransaction.commit();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        tvLocation = findViewById(R.id.tv_location_activity_home);

        currentLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               tvLocation.setText("Sector 62, Noida");
            }
        },5000);





        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void currentLocation() {
        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION);
        } else {
            if (getLocationMode(this) != 0) {
                getLocation();
            } else {
                displayLocationSettingsRequest(this);
            }
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(HomeActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });

    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(HomeActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        mLastLocation = location;
                        //Toast.makeText(getActivity(), "finding you...", Toast.LENGTH_SHORT).show();
                        if(mLastLocation == null){
                            Log.i(TAG, "onSuccess: ");
                            getLocation();
                        } else {
                            makeAddress(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                        }
                    }
                });

    }

    private void makeAddress(Double MyLat, Double MyLong) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
        } catch (IOException e) {
            Log.i(TAG, "makeAddress: " + e.getStackTrace().toString());
        }

        assert addresses != null;
        String cityName = addresses.get(0).getLocality();
        //tvLocation.setText(cityName);

        Log.i("HomeFragment", "makeAddress: " + cityName);
    }


    private int getLocationMode(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                return Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            try {
                String locationProviders = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.LOCATION_PROVIDERS_ALLOWED
                );
                if (!TextUtils.isEmpty(locationProviders)) {
                    return 2;
                } else {
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }
    }

}
