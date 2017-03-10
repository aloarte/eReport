package com.uc3m.p4r4d0x.emergapp.servicios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.helpers.Constants;
import com.uc3m.p4r4d0x.emergapp.receivers.ResultReceiverGPSCoord;

import java.util.List;


/**
 * Created by Alvaro Loarte Rodriguez on 20/02/16.
 * Desc: GPS service: gets the Location by network or GPS provider and calls FetchAddress service to get the
 *       address based in the location obtained
 *
 */
public class GPSService extends Service implements LocationListener {

    private Context sContext;
    double latitude, longitude;
    Location locationG;
    LocationListener locationListener;
    TextView paramViewCoord;
    TextView paramViewAddress;
    TextView paramViewCity;
    TextView paramViewStreet;


    protected ResultReceiverGPSCoord mReceiver;

    //Default constructor (Neccesary for the AndroidManifest.xml)
    public GPSService() {
        //SUper ejecuta el constructor de  la clase Service extendida
        super();
        this.sContext = this.getApplicationContext();
        //Create a new location listener with its inner methods
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    //Constructor
    public GPSService(Context c, TextView vAddress ,TextView vCoord,TextView vCity,TextView vStreet) {
        super();
        this.sContext = c;
        this.paramViewAddress = vAddress;
        this.paramViewCoord= vCoord;
        this.paramViewCity = vCity;
        this.paramViewStreet = vStreet;

        //Create a new location listener with its inner methods
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Upgrade location if onLocationChanged is called
    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }


    @Override
    public void onProviderEnabled(String provider) {

    }


    @Override
    public void onProviderDisabled(String provider) {

    }



    /*
    * Ret : True or false if the location was obtained
    * Desc: Obtain the location item by GPS or Network method.
    *       First try by GPS and if fails, try by network
    * */
    public boolean getLocation() {
        boolean locationObtained;

        //Try to get location by GPS
        if (getLocationByGPS()) {
            //Put values obtained from locationG
            latitude = locationG.getLatitude();
            longitude = locationG.getLongitude();
            //Log.d("ALR", "GetLocation:GPS location" + latitude + "," + longitude);
            locationObtained = true;

        }
        //If fails, try to get location by network
        else if (getLocationByNetwork()) {
            //Put values obtained from locationG
            latitude = locationG.getLatitude();
            longitude = locationG.getLongitude();
            //Log.d("ALR", "GetLocation:NW location" + latitude + "," + longitude);
            locationObtained = true;

        }
        //If both fail, return error status
        else {

            //Log.d("ALR", "GetLocation:Cant get any location");
            locationObtained = false;
        }
        return locationObtained;
    }


    /*
    * Ret : True or false if the location was obtained by GPS
    * Desc: Obtain the location item by GPS
    * */
    public boolean getLocationByGPS() {
        //Local location
        Location locationL;

        try {
            //Get a local locationManager by location service
            String location_context = sContext.LOCATION_SERVICE;
            LocationManager mLocationManager =(LocationManager) sContext.getSystemService(location_context);

            //Get GPS status checking if is enabled
            boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isLocationObtainedByGPS = false;

            //Get the location by gps if GPS was enabled
            if (isGPSEnabled) {

                //Obtain a list of providers to get GPS position
                List<String> providers = mLocationManager.getProviders(true);

                //Iterate list of providers
                for (String provider : providers) {

                    //In each iteration, try to get getLastKnownLocation
                    try {
                        Location location = mLocationManager.getLastKnownLocation(provider);

                        //If the location was obtained
                        if (location != null) {

                            //Get the latitude and longitude elements
                            longitude = location.getLongitude();
                            latitude = location.getLatitude();

                            //Set the current local location as the valid location in locationG
                            isLocationObtainedByGPS = true;
                            locationG = location;

                        }

                        //RequestLocationUpgrades setting provider as valid
                        mLocationManager.requestLocationUpdates(provider, 1000, 0, locationListener);

                    }
                    catch (SecurityException e) {
                        e.printStackTrace();
                    }
                } //End For

            } //end if GPS is enabled

            //Return true or false if location is obtained
            return isLocationObtainedByGPS;

        }
        //Catch SecurityExceptions
        catch (SecurityException se) {

            return false;
        }
        //Catch generic exception
        catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }

    /*
    * Ret : True or false if the location was obtained by Network
    * Desc: Obtain the location item by Network
    * */
    public boolean getLocationByNetwork() {
        Location locationL;
        try {
            //Get a local locationManager by location service
            LocationManager mLocationManager = (LocationManager) sContext.getSystemService(LOCATION_SERVICE);
            //Get Network status checking if is enabled
            boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            boolean isLocationObtainedByNW = false;

            //Get the location by gps if Network was enable
            if (isNetworkEnabled) {

                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

                //Get a local location
                locationL = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                //Check if the previous operation was successful
                if (locationL != null) {
                    //Set the current local location as the valid location in locationG
                    isLocationObtainedByNW = true;
                    locationG = locationL;

                }
            }
            return isLocationObtainedByNW;
        } catch (SecurityException se) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /*
    * Desc: Start FetchAddress service, passing a ResultReceiverGPSCoord object to
    *       get the result value and the Location obtained by this service
    * */
    public void startFetchAddressService() {

        //Iniciate ResultReceiverGPSCoord object
        mReceiver = new ResultReceiverGPSCoord(new android.os.Handler(), paramViewAddress,paramViewCoord,paramViewCity,paramViewStreet);

        //Create the intent to start the FetchAddressService
        Intent intent = new Intent(sContext, FetchAddressService.class);
        //Add the params for the service
        intent.putExtra(Constants.RECEIVER, mReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, locationG);

        //Start service based on sContext (getApplicationContext fails)
        sContext.startService(intent);
    }
}