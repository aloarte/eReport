package com.uc3m.p4r4d0x.emergapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBQuestsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;
import com.uc3m.p4r4d0x.emergapp.servicios.GPSService;

public class HomeScreenActivity extends NavigationActivityInner {

    TextView tViewGPS, tViewGPSCoord,tViewGPSCity, tViewGPSStreet;
    String   sGPSAddr, sGPSCoord ,sGPSCity, sGPSStreet;
    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    final int REQUEST_CODE_ASK_PERMISSIONS= 4;

    SharedPreferences sharedpreferences;

    /*
     * Desc: method overrided from AppCompatActivity
     *       this method is called when activity starts
     *       Initialize all the neccessary parts of the main screen
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_home_screen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        checkPermissions();

        //Get the GPS position
        getGPSposition();

        //check if this activity came from EmergencyActivity to make another report
        checkResend();




    }


    /*
    * Desc: Calls GPS Service and prints in the TextView the result
    * */
    public void getGPSposition() {

        //Get the TextView to show the address value
        tViewGPS      = (TextView) findViewById(R.id.tvGPSEM1);
        tViewGPSCoord = (TextView) findViewById(R.id.tvGPSCoordEM1);
        tViewGPSCity = (TextView) findViewById(R.id.tvGPSCityEM1);
        tViewGPSStreet = (TextView) findViewById(R.id.tvGPSStreetEM1);



        //create service passing two TextViews as a param
        GPSService sGPS = new GPSService(getApplicationContext(), this.tViewGPS, this.tViewGPSCoord,this.tViewGPSCity, this.tViewGPSStreet);

        //Try to get the location from GPS or network
        if (sGPS.getLocation()) {
            //If was successful call startFetchAddressService, who will obtain the address bassed on the location obtained
            sGPS.startFetchAddressService();


        } else {
            //If the location couldnt get obtained
            tViewGPS.setText(tViewGPSCoord.getText().toString());
        }
    }

    /*
    * Try to get in strings the GPS position
    * Return true or false if is not obtained
    * */
    public boolean retrieveGPSPosition(){
        sGPSCoord = (String) tViewGPSCoord.getText();
        sGPSAddr  = (String) tViewGPS.getText();
        sGPSCity  = (String) tViewGPSCity.getText();
        sGPSStreet= (String) tViewGPSStreet.getText();


        return (!sGPSAddr.isEmpty() && !sGPSCoord.isEmpty());


    }

    /*
    * Desc: check if have to make another report and this activity came from a report
    * */
    public void checkResend(){
        String sGPSCoord,sGPSAddr,sGPSCity, sGPSStreet;
        int resendMessage=-1;
        int C_FAST=1, C_ASSISTED=2;
        //Get the extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //recover the extras from EmergencyActivity
            sGPSAddr        = extras.getString("GPSA");
            sGPSCoord       = extras.getString("GPSC");
            sGPSCity       = extras.getString("GPSCY");
            sGPSStreet       = extras.getString("GPSST");

            resendMessage   = extras.getInt("lAgainReport");

            //Make a fast report again
            if(resendMessage==C_FAST){
                Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
                //Set value to var popUp1
                i.putExtra("popUp2", "");
                i.putExtra("GPSC", sGPSCoord);
                i.putExtra("GPSA", sGPSAddr);
                i.putExtra("GPSCY", sGPSCity);
                i.putExtra("GPSST", sGPSStreet);
                //Launch intent
                startActivity(i);

            }
            //Make an assisted report again
            else if(resendMessage==C_ASSISTED){
                Intent i = new Intent(getApplicationContext(), EmMessage1.class);
                //Set value to gps position and address
                i.putExtra("GPSC",sGPSCoord);
                i.putExtra("GPSA",sGPSAddr);
                i.putExtra("GPSCY",sGPSCity);
                i.putExtra("GPSST", sGPSStreet);
                //Launch intent
                startActivity(i);
            }
        }
   }

    /*
     * Desc: Check the permissions and request them to the user if necessary
     *  */
    public boolean checkPermissions(){
        //Check if some of the core permissions are not already granted
        if ((ContextCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(HomeScreenActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(HomeScreenActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Some permissions are not granted. Please enable them.", Toast.LENGTH_SHORT).show();

            //If so, request the activation of this permissions
            ActivityCompat.requestPermissions(HomeScreenActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return false;
        }
        else{
            //Permissions already granted
            return true;
        }
    }

    // ----------- ON CLICK METHODS --------------

    /*
    * Desc: on click function to change to AchievementsProgress activity
    * */
    public void onClickAssistedReport(View v){
        //Check if the gps result is ready
        if(retrieveGPSPosition()){
            Intent i = new Intent(getApplicationContext(), EmMessage1.class);
            //Set value to gps position and address
            i.putExtra("GPSC",sGPSCoord);
            i.putExtra("GPSA",sGPSAddr);
            i.putExtra("GPSCY",sGPSCity);
            i.putExtra("GPSST", sGPSStreet);
            //Launch intent
            startActivity(i);
        }
        //if is not ready, dont do anything when the button is pressed
        else{
            if(checkPermissions()){
                Toast.makeText(this, "Couldn't get your position. Please, try again.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Couldn't get your position. Please, check if the requested permissions are enabled before trying to make a report.", Toast.LENGTH_LONG).show();

            }
        }

    }

    /*
    * Desc: on click function to change to AchievementsProgress activity
    * */
    public void onClickFastReport(View v){
        //Check if the gps result is ready
        if(retrieveGPSPosition()){
            Intent i = new Intent(getApplicationContext(), EmergencyActivity.class);
            //Set value to var popUp1
            i.putExtra("popUp2","");
            i.putExtra("GPSC",sGPSCoord);
            i.putExtra("GPSA",sGPSAddr);
            i.putExtra("GPSCY",sGPSCity);
            i.putExtra("GPSST", sGPSStreet);
            //Launch intent
            startActivity(i);
        }
        //if is not ready, dont do anything when the button is pressed
        else{
            if(checkPermissions()){
                Toast.makeText(this, "Couldn't get your position. Please, try again.", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this, "Couldn't get your position. Please, check if the requested permissions are enabled before trying to make a report.", Toast.LENGTH_LONG).show();

            }
        }
    }


    /*
    * Desc: on click function to change to Ranking activity
    * */
    public void onClickNavRanking(View v){
        Intent myIntent= new Intent(getApplicationContext(), RankingActivity.class);
        startActivity(myIntent);
    }

    /*
    * Desc: on click function to change to Achievements activity
    * */
    public void onClickNavAchievements(View v){
        Intent myIntent= new Intent(getApplicationContext(), AchievementsActivity.class);
        startActivity(myIntent);
    }

    /*
    * Desc: on click function to change to AchievementsProgress activity
    * */
    public void onClickNavAchievementsProgress(View v){
        Intent myIntent= new Intent(getApplicationContext(), RewardsPActivity.class);
        startActivity(myIntent);
    }

    /*
    * Desc: on click function to logout
    * */
    public void onClickPerformLogout(View V){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomeScreenActivity.this);
        View layView = (LayoutInflater.from(HomeScreenActivity.this)).inflate(R.layout.confirm_logout, null);
        alertBuilder.setView(layView);
        alertBuilder.setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Remove from the shared preferences the username
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.remove("username");
                        editor.remove("colorprimary");
                        editor.remove("colorsecondary");
                        editor.commit();

                        Toast.makeText(getApplicationContext(), "Session Closed", Toast.LENGTH_SHORT).show();
                        //Create and launch login activity
                        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(myIntent);
                    }
                })
        ;
        Dialog dialog = alertBuilder.create();
        dialog.show();
    }

}
