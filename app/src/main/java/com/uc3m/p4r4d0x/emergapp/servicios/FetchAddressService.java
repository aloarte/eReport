package com.uc3m.p4r4d0x.emergapp.servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import com.uc3m.p4r4d0x.emergapp.helpers.Constants;
import com.uc3m.p4r4d0x.emergapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Alvaro Loarte Rodríguez on 26/03/16.
 * Define an intent service to fetch address : http://developer.android.com/intl/es/training/location/display-address.html
 * Desc: Obtains the address based on a Location item previously obtained by GPS or network
 */
public class FetchAddressService extends IntentService {
    protected ResultReceiver mSender;
    public Context sContext;
    //latitud and longitude are from Madrid - Puerta del sol, by default
    double latitude=40.4169416,longitude=-3.7083866;
    int accuracy;
    String city="",street="";

    //Default constructor (Neccesary for the AndroidManifest.xml)
    public FetchAddressService() {
        super("");
    }
    //Constructor
    public FetchAddressService (Context c, String name){

        super(name);
        this.sContext=c;

    }

    /*
    * Overrided method: triggered when startService(intent) is called
    * Param: an intent with the two params for this service: Location with the location data
    *        and the value for the ResultReceiver to return the address value
    * Desc:  Based on the Location, get the Address and calls deliverResultToReceiver to return
    *        the address value
    * */
    @Override
    protected void onHandleIntent(Intent intent) {
        String errorMessage = "";
        int errorCode =-1;
        //Create a Geocoder object
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        // Get the location passed to this service through an extra.
        Location locationFromGPS = intent.getParcelableExtra(
                Constants.LOCATION_DATA_EXTRA);

        accuracy=(int)locationFromGPS.getAccuracy();
        // Get the ResultReceiver object passed to this service through an extra.
        mSender=intent.getParcelableExtra(Constants.RECEIVER);


        //Create a List of addresses
        List<Address> addresses = null;

        try {
            //Obtain the addres based on the latitude and longitude values from the location
            //Check if we have network connection (needed for calling the geocoder.getFromLocation
            if(isConnectedToNetwork()) {
                latitude=locationFromGPS.getLatitude();
                longitude=locationFromGPS.getLongitude();
                addresses = geocoder.getFromLocation(latitude,longitude,
                        //Get just a single address.
                        1);
            }
            else{
                errorMessage = ""+locationFromGPS.getLatitude()+ ", "+locationFromGPS.getLongitude()+ "\n(in "+accuracy+ " m)";
                errorCode=Constants.FAILURE_RESULT_NETWORK;
            }
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            errorCode=Constants.FAILURE_RESULT_OTHER;
            Log.e("ALR", errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = getString(R.string.invalid_lat_long_used);
            errorCode=Constants.FAILURE_RESULT_OTHER;
            Log.e("ALR", errorMessage + ". " +
                    "Latitude = " + locationFromGPS.getLatitude() +
                    ", Longitude = " +
                    locationFromGPS.getLongitude(), illegalArgumentException);
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                 errorMessage = getString(R.string.no_address_found);
            }

            //Call deliverResultToReceiver with failure result code and error message
            deliverResultToReceiver(errorCode, errorMessage);
        }
        //Handle case where address was found
        else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();

            city=address.getLocality();
            street=address.getThoroughfare();

            // Fetch the address lines using getAddressLine,join in them, and send them to the thread.
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
            }

            //Call deliverResultToReceiver with success result code and the address
            String loc=TextUtils.join(System.getProperty("line.separator"),
                    addressFragments);

            deliverResultToReceiver(Constants.SUCCESS_RESULT,loc+ "\n(in "+accuracy+ " m)");
        }
    }

    /*
    * param: resultCode and a message to deliver to the ResultReceiver
    * desc:  Deliver through the ResultReceiver the result of this service
    */
    private void deliverResultToReceiver(int resultCode, String message) {

        //Put the result message or the address on a Bundle
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        bundle.putString(Constants.RESULT_DATA_KEY2,""+latitude+","+longitude+"");
        bundle.putString(Constants.RESULT_DATA_KEY3,city);
        bundle.putString(Constants.RESULT_DATA_KEY4,street);


        //Send the values
        mSender.send(resultCode, bundle);
    }

    protected Boolean isConnectedToNetwork(){
        if(isConnectedByWifi()){
            return true;
        }else{
            if(isConnectedBy3G()){
                return true;
            }else{

                return false;
            }
        }
    }

    protected Boolean isConnectedByWifi(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean isConnectedBy3G(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }


}

