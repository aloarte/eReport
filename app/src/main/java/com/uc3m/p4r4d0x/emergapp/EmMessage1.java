package com.uc3m.p4r4d0x.emergapp;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.uc3m.p4r4d0x.emergapp.servicios.GPSService;

public class EmMessage1 extends AppCompatActivity {

    //Define constants to send info
    final int C_YES=1,C_NO=2;
    String   sGPSAddr, sGPSCoord,sGPSCity, sGPSStreet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_em_message1);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        //Get the GPS position and the message info from the previous activity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sGPSAddr       = extras.getString("GPSA");
            sGPSCoord      = extras.getString("GPSC");
            sGPSCity       = extras.getString("GPSCY");
            sGPSStreet     = extras.getString("GPSST");
        }
    }

    @Override
    public void onBackPressed() {
        Intent myIntent = new Intent(this, HomeScreenActivity.class);
        startActivity(myIntent);
    }

    /*
    * onClick method from button bYpop1
    * Save the "yes" info and run next activity
    * */
    public void onClickYesPopUp1(View v){

        Intent i = new Intent(getApplicationContext(), EmMessage2.class);
        //Set value to var popUp1
        i.putExtra("popUp1", C_YES);
        i.putExtra("GPSC", sGPSCoord);
        i.putExtra("GPSA", sGPSAddr);
        i.putExtra("GPSCY",sGPSCity);
        i.putExtra("GPSST", sGPSStreet);
        //Launch intent
        startActivity(i);
    }

    /*
    * onClick method from button bNpop1
    * Save the "no" info and run next activity
    * */
    public void onClickNoPopUp1(View v){

        Intent i = new Intent(getApplicationContext(), EmMessage2.class);
        //Set value to var popUp1
        i.putExtra("popUp1",C_NO);
        i.putExtra("GPSC",sGPSCoord);
        i.putExtra("GPSA",sGPSAddr);
        i.putExtra("GPSCY",sGPSCity);
        i.putExtra("GPSST", sGPSStreet);
        //Launch intent
        startActivity(i);
    }


}
