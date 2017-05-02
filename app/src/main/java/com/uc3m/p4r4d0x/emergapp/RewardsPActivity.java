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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

public class RewardsPActivity extends NavigationActivityInner {

    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_rewards_p);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        //Load the progress
        loadProgress();


    }


    /*
  * Desc: load the progress of rewards
  * */
    public void loadProgress(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        DBUserManager managerDB                = new DBUserManager(this);
        //Select the user
        Cursor resultQuery                 = managerDB.selectUser(username);
        //If the user exists
        if(resultQuery.moveToFirst()) {
            //Set the level
            int userProgress = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED));
            for (int i = 0; i <= userProgress; i++) {
                switch (i) {
                    case 0:
                        changeColorOnProgress(R.id.llSepTop1);
                        break;
                    case 1:
                        changeColorOnProgress(R.id.llSepTop1);
                        changeColorOnProgress(R.id.llSepBot1);
                        changeCompletedReward(R.id.ivRewardCompleted1);
                        break;
                    case 2:
                        changeColorOnProgress(R.id.llSepTop2);
                        changeColorOnProgress(R.id.llSepBot2);
                        changeCompletedReward(R.id.ivRewardCompleted2);
                        break;
                    case 3:
                        changeColorOnProgress(R.id.llSepTop3);
                        changeColorOnProgress(R.id.llSepBot3);
                        changeCompletedReward(R.id.ivRewardCompleted3);
                        break;
                    case 4:
                        changeColorOnProgress(R.id.llSepTop4);
                        changeColorOnProgress(R.id.llSepBot4);
                        changeCompletedReward(R.id.ivRewardCompleted4);
                        break;
                    case 5:
                        changeColorOnProgress(R.id.llSepTop5);
                        changeColorOnProgress(R.id.llSepBot5);
                        changeCompletedReward(R.id.ivRewardCompleted5);
                        break;
                    case 6:
                        changeColorOnProgress(R.id.llSepTop6);
                        changeColorOnProgress(R.id.llSepBot6);
                        changeCompletedReward(R.id.ivRewardCompleted6);
                        break;
                    case 7:
                        changeColorOnProgress(R.id.llSepTop7);
                        changeColorOnProgress(R.id.llSepBot7);
                        changeCompletedReward(R.id.ivRewardCompleted7);
                        break;
                    case 8:
                        changeColorOnProgress(R.id.llSepTop8);
                        changeColorOnProgress(R.id.llSepBot8);
                        changeCompletedReward(R.id.ivRewardCompleted8);
                        break;
                    case 9:
                        changeColorOnProgress(R.id.llSepTop9);
                        changeColorOnProgress(R.id.llSepBot9);
                        changeCompletedReward(R.id.ivRewardCompleted9);
                        break;
                }
            }
        }
    }

    public void changeColorOnProgress(int idLinearLayout){
        //Check if there is any user logged into the aplication checking shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String primaryColor = sharedpreferences.getString("colorprimary", "default");
        String secondaryColor = sharedpreferences.getString("colorsecondary", "default");

        //if there is no color
        if(primaryColor.compareTo("default")==0 || secondaryColor.compareTo("default")==0){
            //Load default color
        }
        else{
            LinearLayout llToColor = (LinearLayout) findViewById(idLinearLayout);
            llToColor.setBackgroundColor(Color.parseColor(secondaryColor));
        }

    }

    public void changeCompletedReward(int idImageView){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        DBUserManager managerDB                = new DBUserManager(this);
        //Select the user
        Cursor resultQuery                 = managerDB.selectUser(username);
        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            //Set the level
            int color = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR));

            int resourceID;
            switch (color) {
                //DefaultColor
                case 0:
                    resourceID = R.mipmap.doneicon_ereporter;
                    break;
                //Red
                case 1:
                    resourceID = R.mipmap.doneicon_red;
                    break;
                //Blue
                case 2:
                    resourceID = R.mipmap.doneicon_blue;
                    break;
                //Green
                case 3:
                    resourceID = R.mipmap.doneicon_green;
                    break;
                //Purple
                case 4:
                    resourceID = R.mipmap.doneicon_purple;
                    break;
                //Yellow
                case 5:
                    resourceID = R.mipmap.doneicon_yellow;
                    break;
                //Pink
                case 6:
                    resourceID = R.mipmap.doneicon_pink;
                    break;
                //Grey
                case 7:
                    resourceID = R.mipmap.doneicon_grey;
                    break;
                default:
                    resourceID = R.mipmap.doneicon;
                    break;
            }

            //Get the text view
            ImageView ivDone;
            ivDone = (ImageView) findViewById(idImageView);
            //Set text to it
            ivDone.setImageResource(resourceID);
        }

    }



}
