package com.uc3m.p4r4d0x.emergapp.receivers;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.helpers.Constants;
import com.uc3m.p4r4d0x.emergapp.R;


/**
 * Created by Alvaro Loarte Rodriguez on 30/03/16.
 * Desc: Object that receive the result from the FetchAddress service and prints the result on TextViews
 */

public class ResultReceiverGPSCoord extends ResultReceiver {
    private Receiver mReceiver=null;
    private String address="";
    private String gpsCoord="40.4169416,-3.7083866";
    private String gpsCity="";
    private String gpsStreet="";
    private String errorMessage="";
    private TextView GPSAddressView;
    private TextView GPSCoordView;
    private TextView GPSCityView;
    private TextView GPSStreetView;

    /*
    * Param: Handler and the TextViews for printing the address and the latitude and longitude
    * Desc: Main Constructor
    * */
    public ResultReceiverGPSCoord(android.os.Handler handler, TextView addrView, TextView GPSView,TextView city,TextView street) {
        super(handler);
        GPSAddressView=addrView;
        GPSCoordView=GPSView;
        GPSCityView=city;
        GPSStreetView=street;
    }

    /*
    * Desc: Neccesary interface for onReceiveResult method
    * */
    public interface Receiver {
            void onReceiveResult(int resultCode,Bundle resultData);
    }

   /*
   * Param: resultCode and resultData obtained from the operation from FetchAddressService
   * Desc:  Overrided method called when any result on the ResultReceiver is obtained
   *        Check the value and call setView function for printing the address
   * */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        //Check if the method was called proper
        if(mReceiver != null) {
            mReceiver.onReceiveResult(resultCode,resultData);
        }
        else {
            //If the resultCode was success, print the address on the TextView
            if (resultCode == Constants.SUCCESS_RESULT) {

                // Get the address and the latitude and longitude from the resultData object
                address = resultData.getString(Constants.RESULT_DATA_KEY);
                gpsCoord=resultData.getString(Constants.RESULT_DATA_KEY2);
                gpsCity =resultData.getString(Constants.RESULT_DATA_KEY3);
                gpsStreet =resultData.getString(Constants.RESULT_DATA_KEY4);


                //Print the addres on the TextView
                setView(true,resultCode);
            }
            else{
                //Get the error message from the resultData
                errorMessage= resultData.getString(Constants.RESULT_DATA_KEY);
                //Print an error message on the Textview
                setView(false,resultCode);
            }
        }
    }


  /*
  * Param: boolean value: true if the addres is obtained or false if not
  * Desc:  Prints the address in the TextView
  * */
    public void setView(boolean addressObtained,int resultCode){
        //Set the latitude and longitude on the textView
        GPSCoordView.setText(gpsCoord);
        //Check if address is obtained

        if(addressObtained){
            //Display the address on the textView
            GPSAddressView.setText(address);
            GPSCityView.setText(gpsCity);
            GPSStreetView.setText(gpsStreet);

        }
        else{

            if(resultCode==Constants.FAILURE_RESULT_NETWORK){
                //Display an error message
                GPSAddressView.setText(errorMessage);
            }
            else{
                //Display an error message
                GPSAddressView.setText(R.string.no_address_found);
            }

        }

    }

    }

