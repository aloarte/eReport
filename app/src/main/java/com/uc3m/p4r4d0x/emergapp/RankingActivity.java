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
import android.service.notification.NotificationListenerService;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.uc3m.p4r4d0x.emergapp.fragments.RankFragment1;
import com.uc3m.p4r4d0x.emergapp.fragments.RankFragment2;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBTitlesManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

public class RankingActivity extends NavigationActivityInner {


    TabLayout tabLayoutRanking;
    ViewPager viewPagerRanking;
    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;

    /*Inner class implemented for the TabLayout*/
    private class CustomAdapter extends FragmentPagerAdapter {
        //String array with the fragments names
        private String fragmentsNames[] = new String[]{"XP Ranking", "AP Ranking"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }


        @Override
        public Fragment getItem(int position) {

            RankFragment1 rankFragment1;
            RankFragment2 rankFragment2;
            Fragment retFragment;
            //For each position create the correspond fragment, created by a fragment class
            switch (position) {
                case 0:
                    rankFragment1= new RankFragment1();
                    //Load the info into the fragment to set the rank data
                    loadUsersInXPRanking(rankFragment1);
                    //Get the fragment to return it
                    retFragment=rankFragment1;
                    break;
                case 1:
                    rankFragment2= new RankFragment2();
                    loadUsersInAPRanking(rankFragment2);
                    retFragment=rankFragment2;
                    break;
                default:
                    retFragment= null;
                break;
            }
            return retFragment;
        }

        //Return the size of the array
        @Override
        public int getCount() {
            return fragmentsNames.length;
        }

        //Return the name of each fragment
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentsNames[position];
        }

    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Initialize all the neccessary parts of the main screen
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_ranking);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        //FOR THE TAB LAYOUT
        //Get the viewPager
        viewPagerRanking = (ViewPager) findViewById(R.id.viewPagerRanking);
        viewPagerRanking.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        //Get the tabLayout and set the viewPager
        tabLayoutRanking = (TabLayout) findViewById(R.id.tabLayoutRanking);
        tabLayoutRanking.setupWithViewPager(viewPagerRanking);

        //Override onTabSelected methods to let the tab respond with the viewPager
        tabLayoutRanking.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerRanking.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPagerRanking.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPagerRanking.setCurrentItem(tab.getPosition());
            }
        });


    }

    /*
    * Desc: get the data from the DDBB to fill propperly the rank textViews
    * param: the rank fragment class to set on it the data
    * */
    public void loadUsersInXPRanking(RankFragment1 rankFragment){

        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
      //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            int obtainedAux=0;
            //String array to save all data recovered from the DDBB
            String [][] data = new String [5][5];
            //Open the DDBB manager
            DBUserManager managerDBUsers = new DBUserManager(this);
            //Select the users ordered by XP points
            Cursor resultQuery = managerDBUsers.selectUsersOrderedByXP();
            //iterate each user to save data into the array
            int i=0,userTop=0;
            for(resultQuery.moveToFirst();
                i<5 && !resultQuery.isAfterLast();
                resultQuery.moveToNext(),i++){
                if(resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)).compareTo(username)==0){
                    userTop=1;
                }
                //Get the name,title, level and XP points of the user
                data[i][0] = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME));
                data[i][1] = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE));
                data[i][2] = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL));
                data[i][3] = ""+resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS));
                data[i][4] = ""+userTop;
                userTop=0;
            }
            //Set the data retrieved into the fragment view
            rankFragment.setArgumentsToFragment(data, i);

        }
    }

    /*
    * Desc: get the data from the DDBB to fill propperly the rank textViews
    * param: the rank fragment class to set on it the data
    * */
    public void loadUsersInAPRanking(RankFragment2 rankFragment){

        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            int obtainedAux=0;
            //String array to save all data recovered from the DDBB
            String [][] data = new String [5][5];
            //Open the DDBB manager
            DBUserManager managerDBUsers = new DBUserManager(this);
            //Select the users ordered by XP points
            Cursor resultQuery = managerDBUsers.selectUsersOrderedByAP();
            //iterate each user to save data into the array
            int i=0,userTop=0;
            for(resultQuery.moveToFirst();
                i<5 && !resultQuery.isAfterLast();
                resultQuery.moveToNext(),i++){
                if(resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)).compareTo(username)==0){
                    userTop=1;
                }
                //Get the name,title, level and XP points of the user
                data[i][0] = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME));
                data[i][1] = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE));
                data[i][2] = ""+resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR));
                data[i][3] = ""+resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS));
                data[i][4] = ""+userTop;
                userTop=0;
            }
            //Set the data retrieved into the fragment view
            rankFragment.setArgumentsToFragment(data, i);

        }
    }

}
