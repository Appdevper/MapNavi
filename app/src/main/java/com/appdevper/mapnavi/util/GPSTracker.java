package com.appdevper.mapnavi.util;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker implements LocationListener {
    private final Activity mActivity;
    private final Callback callback;

    //flag for GPS Status
    private boolean isGPSEnabled = false;

    //flag for network status
    private boolean isNetworkEnabled = false;

    private boolean canGetLocation = false;

    private Location location;

    //The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; //10 metters

    //The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 10000; // 1 minute

    //Declaring a Place Manager
    protected LocationManager locationManager;
    private static GPSTracker mGPSTracker;

    public static GPSTracker getInstance(Activity activity, Callback call) {
        if (mGPSTracker == null) {
            mGPSTracker = new GPSTracker(activity, call);
        }
        return mGPSTracker;
    }

    public static GPSTracker newInstance(Activity activity, Callback call) {
        mGPSTracker = null;
        mGPSTracker = new GPSTracker(activity, call);
        return mGPSTracker;
    }


    public GPSTracker(Activity activity, Callback call) {
        this.callback = call;
        this.mActivity = activity;
        updateLocation();
    }

    public void updateLocation() {
        this.mActivity.runOnUiThread(new Runnable() {

            @Override
            public void run() {

                try {
                    locationManager = (LocationManager) mActivity.getSystemService(Service.LOCATION_SERVICE);

                    //getting GPS status
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                    //getting network status
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (!isGPSEnabled && !isNetworkEnabled) {
                        // no network provider is enabled
                        canGetLocation = false;
                    } else {
                        if (!checkPermission()) {
                            return;
                        }
                        canGetLocation = true;
                        //First get location from Network Provider
                        if (isNetworkEnabled) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSTracker.this);

//		                    Log.d("Call locationManager to retrieve latitude,longitude from NET");

                            updateGPSCoordinates(LocationManager.NETWORK_PROVIDER);
                        }

                        //if GPS Enabled get lat/long using GPS Services
                        if (isGPSEnabled && location == null) {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, GPSTracker.this);

//		                    Log.d("Call locationManager to retrieve latitude,longgiture from GPS");

                            updateGPSCoordinates(LocationManager.GPS_PROVIDER);
                        }
                    }
                } catch (Exception e) {

                }
            }
        });

    }

    private void updateGPSCoordinates(String provider) {
        if (locationManager != null) {
            if (!checkPermission()) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
        }
    }

    public Location getLocation() {
        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */

    public void stopUsingGPS() {
        if (locationManager != null) {
            if (!checkPermission()) {
                return;
            }
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    /**
     * Function to get latitude
     */
    public String getLatitude() {
        double latitude = 0.0f;

        if (location != null) {
            latitude = location.getLatitude();
        }

        return String.valueOf(latitude);
    }

    /**
     * Function to get longitude
     */
    public String getLongitude() {
        double longitude = 0.0f;

        if (location != null) {
            longitude = location.getLongitude();
        }

        return String.valueOf(longitude);
    }

    public LatLng getLatLng() {
        double longitude = 0.0f;
        double latitude = 0.0f;
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        return new LatLng(latitude, longitude);
    }

    /**
     * Function to check GPS/wifi enabled
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Get list of address by latitude and longitude
     *
     * @return null or List<Address>
     */
    public List<Address> getGeocoderAddress(Context context) {
        if (location != null) {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                return addresses;
            } catch (IOException e) {
                //e.printStackTrace();
//                Log.e("Impossible to connect to Geocoder");
            }
        }

        return null;
    }

    /**
     * Try to get AddressLine
     *
     * @return null or addressLine
     */
    public String getAddressLine(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String addressLine = address.getAddressLine(0);

            return addressLine;
        } else {
            return null;
        }
    }

    /**
     * Try to get Locality
     *
     * @return null or locality
     */
    public String getLocality(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String locality = address.getLocality();

            return locality;
        } else {
            return null;
        }
    }

    /**
     * Try to get Postal Code
     *
     * @return null or postalCode
     */
    public String getPostalCode(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String postalCode = address.getPostalCode();

            return postalCode;
        } else {
            return null;
        }
    }

    /**
     * Try to get CountryName
     *
     * @return null or postalCode
     */
    public String getCountryName(Context context) {
        List<Address> addresses = getGeocoderAddress(context);
        if (addresses != null && addresses.size() > 0) {
            Address address = addresses.get(0);
            String countryName = address.getCountryName();

            return countryName;
        } else {
            return null;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        if(callback!=null){
            callback.callback(location);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

}
