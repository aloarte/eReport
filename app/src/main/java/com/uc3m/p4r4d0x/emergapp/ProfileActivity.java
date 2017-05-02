package com.uc3m.p4r4d0x.emergapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBTitlesManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

public class ProfileActivity extends NavigationActivityInner {

    ProgressBar pbProfile;
    int progressStatus=0;
    Handler handler =new Handler();
    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Initialize all the neccessary parts of the main screen
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_profile);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        pbProfile = (ProgressBar) findViewById(R.id.pbProfile);

        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        //Check the username
        if(username.compareTo("default")==0){
            // Get the Drawable custom_progressbar
            Drawable draw = getResources().getDrawable(R.drawable.customprogressbar);
            // set the drawable as progress drawable
            pbProfile.setProgressDrawable(draw);
            pbProfile.setProgress(progressStatus);
        }
        else{
            DBUserManager managerDB                = new DBUserManager(this);
            //Select the user
            Cursor resultQuery                 = managerDB.selectUser(username);
            if(resultQuery.moveToFirst()==true){
                Drawable draw;
                int progressbarcolor  = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR));
                switch(progressbarcolor){
                    //DefaultColor
                    case 0:
                        draw = getResources().getDrawable(R.drawable.customprogressbar);
                        break;
                    //Red
                    case 1:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_red);
                        break;
                    //Blue
                    case 2:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_blue);
                        break;
                    //Green
                    case 3:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_green);
                        break;
                    //Purple
                    case 4:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_purple);
                        break;
                    //Yellow
                    case 5:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_yellow);
                        break;
                    //Pink
                    case 6:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_pink);
                        break;
                    //Grey
                    case 7:
                        draw = getResources().getDrawable(R.drawable.customprogressbar_grey);
                        break;
                    default:
                        draw = getResources().getDrawable(R.drawable.customprogressbar);
                        break;
                }
                pbProfile.setProgressDrawable(draw);
                pbProfile.setProgress(progressStatus);
            }

        }

        //Load the titles
        loadProfileInfo();


    }


    /*
    * Desc: load the all the user info such as
    *       the avatar, his data and the titles
    *       recovered from the DDBB
    *
    * */
    public void loadProfileInfo(){
        //Load the data
        loadUserData();
        //Check if the titles are unlocked and if so, load them
        if(checkUnlockSelectTitles()){
            loadTitles();
        }
    }



    /*
    * Desc: load the user data recovered from the DDBB including
    *       the name, level,title, AP, XP and the progress bar
    * */
    public void loadUserData(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            DBUserManager managerDB = new DBUserManager(this);
            //Select the user
            Cursor resultQuery = managerDB.selectUser(username);
            //If the user exists
            if (resultQuery.moveToFirst() == true) {
                //Set the User
                String user = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME));
                TextView tvProfileUser = (TextView) findViewById(R.id.tvProfileNickname);
                tvProfileUser.setText(user);
                //Set the level
                String level = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL));
                TextView tvProfileLevel = (TextView) findViewById(R.id.tvProfileRank);
                tvProfileLevel.setText(level);
                //Set the title
                String title = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE));
                TextView tvProfileTitle = (TextView) findViewById(R.id.tvProfileTitle);
                tvProfileTitle.setText(title);
                //Set the AP points
                int APpoints = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS));
                TextView tvProfileAP = (TextView) findViewById(R.id.tvProfileAP);
                tvProfileAP.setText("" + APpoints);
                //Set the XP points
                int XPpoints = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS));
                TextView tvProfileCurrentXP = (TextView) findViewById(R.id.tvProfileCurrentXP);
                TextView tvProfileLevelXP = (TextView) findViewById(R.id.tvProfileMaxLevelXP);

                int maxXpPoints=0;
                switch (level) {
                    case "Traveler":
                        tvProfileLevelXP.setText("" + 50);
                        maxXpPoints=50;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;
                    case "Veteran":
                        tvProfileLevelXP.setText("" + 150);
                        maxXpPoints=150;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;
                    case "Champion":
                        tvProfileLevelXP.setText("" + 300);
                        maxXpPoints=300;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;
                    case "Hero":
                        tvProfileLevelXP.setText("" + 500);
                        maxXpPoints=500;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;
                    case "Legend":
                        tvProfileLevelXP.setText("" + 999);
                        maxXpPoints=999;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;
                    default:
                        tvProfileLevelXP.setText("" + 999);
                        maxXpPoints=999;
                        tvProfileCurrentXP.setText("" + XPpoints);
                        break;

                }
                progressStatus=(XPpoints*100)/maxXpPoints;
                pbProfile.setProgress(progressStatus);

                int avatar = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR));
                ImageView fragmentImageView = (ImageView) findViewById(R.id.ivProfileAvatarImage);
                //Set text to it
                fragmentImageView.setImageResource(avatar);

            }
        }
    }

    /*
    * Desc: Check from the DDBB if the user can select his title
    * */
    public boolean checkUnlockSelectTitles(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        boolean retValue=false;
        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            //Get the linear layout
            LinearLayout llSelecttitles = (LinearLayout) findViewById(R.id.llProfileTitleSelector);
            //Get the database manager
            DBUserManager managerDBUser = new DBUserManager(this);
            //Make que query
            Cursor resultQuery = managerDBUser.selectUser(username);
            //Check if the title selection is unlocked
            if(resultQuery.moveToFirst()==true) {
                if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_MODIFY_TITLE)) == 1) {
                    llSelecttitles.setVisibility(View.VISIBLE);
                    retValue = true;
                } else {
                    llSelecttitles.setVisibility(View.INVISIBLE);
                    retValue = false;
                }
            }
        }
        return retValue;
    }

    /*
    * Desc: load the obtained titles on the screen
    *       recovered from the DDBB
    * */
    public void loadTitles(){

        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            int obtainedAux=0;
            String nameTitleAux="";
            DBTitlesManager managerDBTitles = new DBTitlesManager(this);

            //Select all the titles that the user have
            Cursor resultQuery = managerDBTitles.selectUserTitles(username);
            //iterate each title
            for(resultQuery.moveToFirst();
                !resultQuery.isAfterLast();
                resultQuery.moveToNext()){

                //Get the title name and if is obtained
                nameTitleAux = resultQuery.getString(resultQuery.getColumnIndex(DBTitlesManager.TT_NAME_ID));
                obtainedAux  = resultQuery.getInt(resultQuery.getColumnIndex(DBTitlesManager.TT_OBTAINED));


                //Switch by the title name, get the view and perform the view change
                switch (nameTitleAux){
                    case "tBegginer":
                        RadioButton rbBegginer = (RadioButton) findViewById(R.id.rbBegginerTitle);
                        changeTitleVisiblity(obtainedAux,rbBegginer);
                        break;
                    case "tHero":
                        RadioButton rbChampion = (RadioButton) findViewById(R.id.rbChampionTitle);
                        changeTitleVisiblity(obtainedAux,rbChampion);
                        break;
                    case "tTop":
                        RadioButton rbTopReporter = (RadioButton) findViewById(R.id.rbTopReporterTitle);
                        changeTitleVisiblity(obtainedAux,rbTopReporter);
                        break;
                    case "tSeeker":
                        RadioButton rbSeekerOfTruth = (RadioButton) findViewById(R.id.rbSeekerOfTruthTitle);
                        changeTitleVisiblity(obtainedAux,rbSeekerOfTruth);
                        break;
                    case "tWorker":
                        RadioButton rbHardWorker = (RadioButton) findViewById(R.id.rbHardWorkerTitle);
                        changeTitleVisiblity(obtainedAux,rbHardWorker);
                        break;
                }
            }

        }
    }

    /*
    * Desc: Change the visibility on the button if the title is obtained
    * Par: int 1 obtained 0 not obtained, and the radio button view
    *
    * */
    public void changeTitleVisiblity(int obtained, View titleRadioButton){
        //If the title was obtained
        if(obtained==1){
            titleRadioButton.setVisibility(TextView.VISIBLE);
        }
        //If the title wasnt obtained
        else if(obtained==0){
            titleRadioButton.setVisibility(TextView.GONE);
        }
    }

    /*
     * Desc: onClick method for the select title button
     *       Get the title and set on the database, after it, reload the toolbar
     *
     * */
    public void onClickSelectTitle(View v) {
        //Get the radio group view
        RadioGroup rgTitles = (RadioGroup) findViewById(R.id.rgTitles);
        //Check if there is any selection
        if (rgTitles.getCheckedRadioButtonId() != -1) {
            //Get the id of the selected view
            int vId = rgTitles.getCheckedRadioButtonId();
            //Get the selected view
            View rbView = rgTitles.findViewById(vId);
            //Get the button id
            int rID = rgTitles.indexOfChild(rbView);
            //Get the button selected
            RadioButton rbTitle = (RadioButton) rgTitles.getChildAt(rID);
            //Get the buttons value
            String titleSelected = (String) rbTitle.getText();
            //Set blank if the option was dont display
            if (titleSelected.compareTo("Dont display any title") == 0) {
                titleSelected = "-";

            }

            //Store on the DDBB
            String username = sharedpreferences.getString("username", "default");

            if (username.compareTo("default") != 0) {
                DBUserManager managerDB = new DBUserManager(this);
                managerDB.upgradeUserTitle(username, titleSelected);
                Toast.makeText(getApplicationContext(), "Title changed", Toast.LENGTH_SHORT).show();
                loadToolbar();
                loadUserData();
            }

        }
    }


}
