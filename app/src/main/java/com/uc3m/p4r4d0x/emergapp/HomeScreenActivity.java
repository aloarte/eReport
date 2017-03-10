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

public class HomeScreenActivity extends AppCompatActivity {

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
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarH);
        setSupportActionBar(toolbar);

        checkPermissions();

        //Get the GPS position
        getGPSposition();
        //Load Toolbar
        loadToolbar();
        //Load the color
        loadColor();
        //check if this activity came from EmergencyActivity to make another report
        checkResend();

        loadNotificationQuests();



    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Prepare the toolbar menu
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_emergency_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Prepare the elements on the toolbar menu
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;

        switch (item.getItemId()) {
            case R.id.action_close_session:
                performLogout();
                return true;
            case R.id.action_acount_configuration:
                if(checkUnlockAcountConfiguration()){
                    myIntent= new Intent(getApplicationContext(), AccountConfigurationActivity.class);
                    startActivity(myIntent);
                }
                else{
                    Toast.makeText(this, "This feature is locked", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_profile:
                myIntent= new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_ranking:
                myIntent= new Intent(getApplicationContext(), RankingActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_achievements:
                myIntent= new Intent(getApplicationContext(), AchievementsActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_rewards:
                myIntent= new Intent(getApplicationContext(), RewardsPActivity.class);
                startActivity(myIntent);
                return true;
            case R.id.action_quest:
                onClickShowQuest();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
    }

    /*OnRequestPermissions
    * Desc: check the status of the permissions
    * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted

                } else {
                    // Permission Denied
                    Toast.makeText(HomeScreenActivity.this, "Some permissions have been rejected. Please, enable them to use this app.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    /*
     * Desc: load the data into the toolbar
     * */
    public void loadToolbar(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else{
            //Put username in the toolbar text view
            TextView tvToolbarUser         = (TextView) findViewById(R.id.tvToolbarUser);
            tvToolbarUser.setText(username);

        }
        DBUserManager managerDB                = new DBUserManager(this);
        //Select the user
        Cursor resultQuery                 = managerDB.selectUser(username);
        //If the user exists
        if(resultQuery.moveToFirst()==true){
            //Set the level
            String level                      = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL));
            TextView tvToolbarLevel = (TextView) findViewById(R.id.tvToolbarLevel);
            tvToolbarLevel.setText(level);
            //Set the title
            String title                      = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE));
            TextView tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitle);
            tvToolbarTitle.setText(title);
            //Set the AP points
            int APpoints                     = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS));
            TextView tvToolbarAP = (TextView) findViewById(R.id.tvToolbarCurrentAP);
            tvToolbarAP.setText(""+APpoints);
            //Set the XP points
            int XPpoints                     = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS));
            TextView tvToolbarXPMax = (TextView) findViewById(R.id.tvToolBarNextLevelXP);
            TextView tvToolbarXP = (TextView) findViewById(R.id.tvToolbarCurrentXP);

            switch(level){
                case "Traveler":
                    tvToolbarXPMax.setText(""+50);
                    tvToolbarXP.setText(""+XPpoints);
                    break;
                case "Veteran":
                    tvToolbarXPMax.setText(""+150);
                    tvToolbarXP.setText(""+XPpoints);
                    break;
                case "Champion":
                    tvToolbarXPMax.setText(""+300);
                    tvToolbarXP.setText(""+XPpoints);
                    break;
                case "Hero":
                    tvToolbarXPMax.setText(""+500);
                    tvToolbarXP.setText(""+XPpoints);
                    break;
                case "Legend":
                    tvToolbarXPMax.setText(""+999);
                    tvToolbarXP.setText(""+XPpoints);
                    break;
                default:
                    break;

            }

            int avatar = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR));
            ImageView fragmentImageView = (ImageView) findViewById(R.id.ivLogoToolbar);
            //Set text to it
            fragmentImageView.setImageResource(avatar);
        }
    }

    /*
    * Desc: load the color on the toolbar and other elements
    * */
    public void loadColor(){

        //Check if there is any user logged into the aplication checking shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String primaryColor = sharedpreferences.getString("colorprimary", "default");
        String secondaryColor = sharedpreferences.getString("colorsecondary", "default");
        //if there is no color
        if(primaryColor.compareTo("default")==0 || secondaryColor.compareTo("default")==0){
            //Load default color
        }
        else{

            //Load the new color
            Toolbar t= (Toolbar) findViewById(R.id.toolbarH);
            t.setBackgroundColor(Color.parseColor(primaryColor));

        }
    }


    /*
   * Desc: load the notification icon for the quests
   * */
    public void loadNotificationQuests(){
        //Get the number of notifications
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int notifNumber   = sharedpreferences.getInt("quest_notifications", 0);
        boolean isQuestS  = sharedpreferences.getBoolean("questB", false);

        //Get the element to change it
        ImageView ivNotif = (ImageView) findViewById(R.id.ivQuestNotification);

        switch(notifNumber){
            case 0:
                ivNotif.setImageResource(R.mipmap.ic_quests);
                break;
            case 1:
                ivNotif.setImageResource(R.mipmap.ic_quests_1);
                break;
            case 2:
                ivNotif.setImageResource(R.mipmap.ic_quests_2);
                break;
            default:
                ivNotif.setImageResource(R.mipmap.ic_quests);
                break;
        }

        LinearLayout llImageProfile = (LinearLayout) findViewById(R.id.llImageProfile);
        LinearLayout llQuestActive = (LinearLayout) findViewById(R.id.llQuestActive);
        if(isQuestS){
            llImageProfile.setVisibility(View.GONE);
            llQuestActive.setVisibility(View.VISIBLE);
        }
        else{
            llImageProfile.setVisibility(View.VISIBLE);
            llQuestActive.setVisibility(View.GONE);
        }
    }

    /*
        * Desc: on click function to logout from the aplication
        * */
    public void performLogout(){

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

    /*
    * Desc: Check from the DDBB if the user can select his account configuration
    * */
    public boolean checkUnlockAcountConfiguration(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
        boolean retValue=false;
        //Get the linear layout

        DBUserManager managerDBUser = new DBUserManager(this);
        //Make que query
        Cursor resultQuery = managerDBUser.selectUser(username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()==true) {
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_MODIFY_AVATAR)) == 1) {
                retValue = true;
            } else {
                retValue = false;
            }
        }

        return retValue;
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

    public void onClickShowQuest(View v){
        onClickShowQuest();
    }

    /*
   * Desc: on click function to logout
   * */
    public void onClickShowQuest(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(HomeScreenActivity.this);
        View layView = (LayoutInflater.from(HomeScreenActivity.this)).inflate(R.layout.quest_content, null);
        alertBuilder.setView(layView);
        final TextView questName = (TextView) layView.findViewById(R.id.tvQuestPopName);
        final TextView questDesc = (TextView) layView.findViewById(R.id.tvQuestPopDesc);
        final TextView questCity = (TextView) layView.findViewById(R.id.tvQuestPopCity);
        final TextView questStreet = (TextView) layView.findViewById(R.id.tvQuestPopStreet);
        final TextView questAP = (TextView) layView.findViewById(R.id.tvQuestPopAP);
        final TextView questXP = (TextView) layView.findViewById(R.id.tvQuestPopXP);

        LinearLayout llQuest = (LinearLayout) layView.findViewById(R.id.llQuestData);
        LinearLayout llNoQuest = (LinearLayout) layView.findViewById(R.id.llNoQuest);


        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        boolean isQuestS     = sharedpreferences.getBoolean("questB", false);
        String questNameS    = sharedpreferences.getString("quest", "default");
        String questDescS    = sharedpreferences.getString("questDesc", "default");
        String questCityS    = sharedpreferences.getString("questCity", "default");
        String questStreetS  = sharedpreferences.getString("questStreet", "default");
        final int questAPS         = sharedpreferences.getInt("questAP", -1);
        final int questXPS         = sharedpreferences.getInt("questXP", -1);

        if(isQuestS) {
            llQuest.setVisibility(View.VISIBLE);
            llNoQuest.setVisibility(View.GONE);

            if(questNameS.compareTo("Quest1")==0){
                questName.setText("Report on locality");
            }
            else if(questNameS.compareTo("Quest2")==0){
                questName.setText("Report event");
            }
            else{
                questName.setText(questNameS);
            }
            questDesc.setText(questDescS);
            questCity.setText(questCityS);
            questStreet.setText(questStreetS);
            questAP.setText(""+questAPS);
            questXP.setText(""+questXPS);
            alertBuilder.setCancelable(true)
                    .setPositiveButton("Abandon Quest", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Toast.makeText(this, "Quest 1 completed! Reward: " + questAPS + " AP, " + questXPS + " XP", Toast.LENGTH_SHORT).show();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.remove("questB");
                            editor.remove("quest");
                            editor.remove("questDesc");
                            editor.remove("questCity");
                            editor.remove("questStreet");
                            editor.remove("questAP");
                            editor.remove("questXP");
                            editor.commit();
                            loadNotificationQuests();

                        }
                    })
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
            ;
        }
        else{
            llQuest.setVisibility(View.GONE);
            llNoQuest.setVisibility(View.VISIBLE);

            alertBuilder.setCancelable(true)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
            ;
        }

        Dialog dialog = alertBuilder.create();
        dialog.show();

    }

    /*
   * Desc: on click method to navegate from toolbar to profile activity
   * */
    public void onClickChangeProfileActivity(View v){
        Intent myIntent= new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(myIntent);
    }

    /*
  * Desc: on click method to navegate from toolbar to acount configuration activity
  * */
    public void onClickChangeACActivity(View v){
        if(checkUnlockAcountConfiguration()){
            Intent myIntent= new Intent(getApplicationContext(), AccountConfigurationActivity.class);
            startActivity(myIntent);
        }
        else{
            Toast.makeText(this, "This feature is locked", Toast.LENGTH_SHORT).show();
        }
    }

    /*
* Desc: on click method to navegate from toolbar to achievements activity
* */
    public void onClickChangeQuestActivity(View v){
        Intent myIntent= new Intent(getApplicationContext(), AchievementsActivity.class);
        startActivity(myIntent);

    }
}
