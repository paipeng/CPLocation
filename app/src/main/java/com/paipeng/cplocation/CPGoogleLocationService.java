package com.paipeng.cplocation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;


import com.paipeng.cplocation.model.CPLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class CPGoogleLocationService extends CPBaseLocationService {
    private static final String TAG = CPGoogleLocationService.class.getSimpleName();
    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // flag for GPS status
    boolean canGetLocation = false;

    private Location location;

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private S2iGoogleLocationListenner s2iGoogleLocationListenner;

    public CPGoogleLocationService(Context context, String apiKey, int gpsIntervalTime, CP_LOCATION_LANGUAGE language) {
        super(context, apiKey, gpsIntervalTime, language);
        this.gpsIntervalTime = 1;
        s2iGoogleLocationListenner = new S2iGoogleLocationListenner();
    }

    @SuppressLint("MissingPermission")
    public void startLocation () {
        Log.i(TAG, "startLocation");
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.i(TAG, "isGPSEnabled: " + isGPSEnabled + " isNetworkEnabled: " + isNetworkEnabled);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled

            } else {
                this.canGetLocation = true;
                // First get location from Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            gpsIntervalTime*1000,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, s2iGoogleLocationListenner);

                    searching();
                    Log.d(TAG, "Network");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                            setS2iLocation(location);
                            started();
                        }
                    }
                } else if (isGPSEnabled) {
                    // if GPS Enabled get lat/long using GPS Services
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            gpsIntervalTime*1000,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, s2iGoogleLocationListenner);

                    searching();
                    Log.d(TAG, "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                        if (location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
                            setS2iLocation(location);
                            started();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            stopped();
        }
    }

    @Override
    public void start() {
        super.start();
        Log.i(TAG, "startLocationListener");

        startLocation();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */

    @Override
    @SuppressLint("MissingPermission")
    public void stop(){
        super.stop();
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        if(locationManager != null){
            locationManager.removeUpdates(s2iGoogleLocationListenner);
            stopped();
        }
    }


    private void setS2iLocation(Location location) {
        if (cpLocation == null) {
            cpLocation = new CPLocation();
        }
        Log.i(TAG, "googleLocation: " + location.getLatitude() + ", " + location.getLongitude() + " " + location.toString());
        cpLocation.setLatitude(location.getLatitude());
        cpLocation.setLongitude(location.getLongitude());

        Locale local;
        Geocoder geocoder;
        List<Address> addresses;

        Log.i(TAG, "current location: " + language);
        if (language == CP_LOCATION_LANGUAGE.CP_LOCATION_LANGUAGE_ZH) {
            local = Locale.SIMPLIFIED_CHINESE;
        } else if (language == CP_LOCATION_LANGUAGE.CP_LOCATION_LANGUAGE_EN) {
            local = Locale.ENGLISH;
        } else if (language == CP_LOCATION_LANGUAGE.CP_LOCATION_LANGUAGE_FR) {
            local = Locale.FRANCE;
        } else {
            local = Locale.ENGLISH;
        }

        geocoder = new Geocoder(context, local);
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            Log.i(TAG, " Geocoder: " + addresses.get(0).toString());
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            cpLocation.setCountry(country);
            cpLocation.setProvince(state);
            cpLocation.setCity(city);
            cpLocation.setDistrict(addresses.get(0).getLocality());
            cpLocation.setAddress(address);
        } catch (IOException e) {
            Log.e(TAG, "Geocoder exception: " + e.getMessage());
        }
    }


    public class S2iGoogleLocationListenner implements LocationListener {

        @Override
        public void onLocationChanged(@NonNull Location location) {
            Log.i(TAG, "onLocationChanged: " + location.toString());
            if (location != null) {
                setS2iLocation(location);
                started();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {

        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {

        }
    }
}
