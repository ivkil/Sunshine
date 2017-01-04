package com.kiliian.sunshine.sync;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.kiliian.sunshine.R;
import com.kiliian.sunshine.app.SunshineApp;
import com.kiliian.sunshine.data.prefs.DefaultPrefs;
import com.kiliian.sunshine.utilities.SunshineDateUtils;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;


public class SunshineSyncService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "SunshineSyncService";

    @Inject
    SunshineSyncTask syncTask;

    @Inject
    DefaultPrefs defaultPrefs;

    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        SunshineApp.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        buildGoogleApiClient();
        if (googleApiClient.isConnected()) {
            updateLocation();
            syncTask.syncWeather();
        } else {
            googleApiClient.connect();
        }
        return START_NOT_STICKY;
    }

    private synchronized void buildGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        updateLocation();
        syncTask.syncWeather();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed. Error code: " + connectionResult.getErrorCode());
        syncTask.syncWeather();
    }

    private void updateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location != null) {
                defaultPrefs.latitudeRowBits(Double.doubleToRawLongBits(location.getLatitude()));
                defaultPrefs.longitudeRowBits(Double.doubleToRawLongBits(location.getLongitude()));
                Geocoder geocoder = new Geocoder(this, SunshineDateUtils.getLocale(this));
                String locality = getString(R.string.unknown_locality);
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (!addresses.isEmpty()) {
                        locality = addresses.get(0).getLocality();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    defaultPrefs.locality(locality);
                }
            }
        }
    }

}
