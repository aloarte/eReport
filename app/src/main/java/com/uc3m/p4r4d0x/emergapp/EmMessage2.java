package com.uc3m.p4r4d0x.emergapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class EmMessage2 extends AppCompatActivity {
    //Value from the previous pop up activity
    int valueFromPopup1=0;

    //Define constants for the message logic
    final int C_PREV_YES=1,C_PREV_NO=2,C_PREV_NOVALUE=0;
    final int C_YES_YES=1,C_YES_NO=2, C_NO_YES=3, C_NO_NO=4;

    String   sGPSAddr, sGPSCoord,sGPSCity,sGPSStreet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_em_message2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        //Get the GPS position and the message info from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            valueFromPopup1 = extras.getInt("popUp1");
            sGPSAddr        = extras.getString("GPSA");
            sGPSCoord       = extras.getString("GPSC");
            sGPSCity       = extras.getString("GPSCY");
            sGPSStreet       = extras.getString("GPSST");

        }

    }


    /*
    * onClick method from button bYpop2
    * With previous info, save previous + "yes" info and run next activity
    * */
    public void onClickYesPopUp2(View v){

        int value=0;
        //Switch all the possible messages with "yes" in the second pop up
        switch(valueFromPopup1){
            case C_PREV_NOVALUE:
            //failure. Send value 0
                break;
            case C_PREV_YES:
                value=C_YES_YES;
                break;
            case C_PREV_NO:
                value=C_NO_YES;
                break;
            default:
                //Failure. Send value 0
                break;
        }
        //Create a new intent and save the info on it
        Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
        i.putExtra("popUp2",value);
        i.putExtra("GPSC",sGPSCoord);
        i.putExtra("GPSA",sGPSAddr);
        i.putExtra("GPSCY",sGPSCity);
        i.putExtra("GPSST", sGPSStreet);

        //Launch next activity
        startActivity(i);

    }


    /*
    * onClick method from button bNpop2
    * With previous info, save previous + "no" info and run next activity
    * */
    public void onClickNoPopUp2(View v){
        int value=0;
        //Switch all the possible messages with "no" in the second pop up
        switch(valueFromPopup1){
            case C_PREV_NOVALUE:
                //failure. Send value 0
                break;
            case C_PREV_YES:
                value=C_YES_NO;
                break;
            case C_PREV_NO:
                value=C_NO_NO;
                break;
            default:
                //failure. Send value 0
                break;
        }
        //Create a new intent and save the info on it
        Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
        i.putExtra("popUp2",value);
        i.putExtra("GPSC",sGPSCoord);
        i.putExtra("GPSA",sGPSAddr);
        i.putExtra("GPSCY",sGPSCity);
        i.putExtra("GPSST", sGPSStreet);

        //Launch next activity
        startActivity(i);

    }




}
