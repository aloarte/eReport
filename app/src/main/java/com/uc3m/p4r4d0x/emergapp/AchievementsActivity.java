package com.uc3m.p4r4d0x.emergapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.fragments.AchievementFragment1;
import com.uc3m.p4r4d0x.emergapp.fragments.AchievementFragment2;
import com.uc3m.p4r4d0x.emergapp.fragments.AchievementFragment3;
import com.uc3m.p4r4d0x.emergapp.fragments.AchievementFragment4;
import com.uc3m.p4r4d0x.emergapp.fragments.RankFragment2;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAchievementsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBQuestsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

public class AchievementsActivity extends NavigationActivityInner {



    TabLayout tabLayoutAchievements;
    ViewPager viewPagerAchievements;

    /*Inner class implemented for the TabLayout*/
    private class CustomAdapter extends FragmentPagerAdapter {
        //String array with the fragments names
        private String fragmentsNames[] = new String[]{"Novel", "Expert", "Secret", "Quests"};

        public CustomAdapter(FragmentManager supportFragmentManager, Context applicationContext) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            AchievementFragment1 achievementFragment1;
            AchievementFragment2 achievementFragment2;
            AchievementFragment3 achievementFragment3;
            AchievementFragment4 achievementFragment4;
            Fragment retFragment;
            //For each position create the correspond fragment, created by a fragment class
            switch (position) {
                case 0:
                    achievementFragment1= new AchievementFragment1();
                    loadNovelAchievements(achievementFragment1);
                    retFragment=achievementFragment1;
                    return retFragment;
                case 1:
                    achievementFragment2= new AchievementFragment2();
                    loadExpertAchievements(achievementFragment2);
                    retFragment=achievementFragment2;
                    return retFragment;
                case 2:
                    achievementFragment3= new AchievementFragment3();
                    loadSecretAchievements(achievementFragment3);
                    retFragment=achievementFragment3;
                    return retFragment;
                case 3:
                    achievementFragment4= new AchievementFragment4();
                    loadQuests(achievementFragment4);
                    retFragment=achievementFragment4;
                    return retFragment;
                default:
                    return null;
            }
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_achievements);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);




        //FOR THE TAB LAYOUT
        //Get the viewPager
        viewPagerAchievements = (ViewPager) findViewById(R.id.viewPagerAchievements);
        viewPagerAchievements.setAdapter(new CustomAdapter(getSupportFragmentManager(), getApplicationContext()));

        //Get the tabLayout and set the viewPager
        tabLayoutAchievements = (TabLayout) findViewById(R.id.tabLayoutAchievements);
        tabLayoutAchievements.setupWithViewPager(viewPagerAchievements);

        //Override onTabSelected methods to let the tab respond with the viewPager
        tabLayoutAchievements.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerAchievements.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPagerAchievements.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPagerAchievements.setCurrentItem(tab.getPosition());
            }
        });




    }






    /*
    * Desc: load the achievements stored on the DDBB for each user, setting the data into a array
    *       and passing it to a fragment
    * Param:AchievementFragment1 with the fragment that will be created
    * */
    public void loadNovelAchievements(AchievementFragment1 achievementFragment){

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
            String [][] data = new String [6][3];
            //Open the DDBB manager
            DBAchievementsManager managerDBAchievements = new DBAchievementsManager(this);
            //Select the users ordered by XP points
            Cursor resultQuery;
            //iterate each user to save data into the array
            int i,color=-1;
            for(i=0;i<6;i++){
                switch (i){
                    case 0:
                        resultQuery = managerDBAchievements.selectAchievement("aNovelMeta",username);
                        break;
                    case 1:
                        resultQuery = managerDBAchievements.selectAchievement("aNovel1",username);
                        break;
                    case 2:
                        resultQuery = managerDBAchievements.selectAchievement("aNovel2",username);
                        break;
                    case 3:
                        resultQuery = managerDBAchievements.selectAchievement("aNovel3",username);
                        break;
                    case 4:
                        resultQuery = managerDBAchievements.selectAchievement("aNovel4",username);
                        break;
                    case 5:
                        resultQuery = managerDBAchievements.selectAchievement("aNovel5",username);
                        break;
                    default:
                        resultQuery = managerDBAchievements.selectAchievement("aNovelMeta",username);
                        break;
                }
                if(resultQuery.moveToFirst()==true) {
                    data[i][0] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS));
                    data[i][1] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS_MAX));
                    data[i][2] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_COMPLETED));
                }
                else{
                    data[i][0]="0";
                    data[i][1]="0";
                    data[i][2]="0";
                }
            }
            //Get the color selected by the user
            DBUserManager dbUserManager = new DBUserManager (this);
            Cursor resultQueryUser = dbUserManager.selectUser(username);
            if(resultQueryUser.moveToFirst()==true) {
                color = resultQueryUser.getInt(resultQueryUser.getColumnIndex(DBUserManager.TU_COLOR));
            }
            //Set the data retrieved into the fragment view
            achievementFragment.setArgumentsToFragment(data, i,color);

        }
    }

    /*
    * Desc: load the achievements stored on the DDBB for each user, setting the data into a array
    *       and passing it to a fragment
    * Param:AchievementFragment2 with the fragment that will be created
    * */
    public void loadExpertAchievements(AchievementFragment2 achievementFragment){

        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            int obtainedAux=0;
            //String array to save all data recovered from the DDBB
            String [][] data = new String [8][3];
            //Open the DDBB manager
            DBAchievementsManager managerDBAchievements = new DBAchievementsManager(this);
            //Select the users ordered by XP points
            Cursor resultQuery;
            //iterate each user to save data into the array
            int i,color=-1;
            for(i=0;i<8;i++){
                switch (i){
                    case 0:
                        resultQuery = managerDBAchievements.selectAchievement("aExpertMeta",username);
                        break;
                    case 1:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert1",username);
                        break;
                    case 2:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert2",username);
                        break;
                    case 3:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert3",username);
                        break;
                    case 4:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert4",username);
                        break;
                    case 5:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert5",username);
                        break;
                    case 6:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert6",username);
                        break;
                    case 7:
                        resultQuery = managerDBAchievements.selectAchievement("aExpert7",username);
                        break;
                    default:
                        resultQuery = managerDBAchievements.selectAchievement("aExpertMeta",username);
                        break;
                }
                if(resultQuery.moveToFirst()==true) {
                    data[i][0] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS));
                    data[i][1] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS_MAX));
                    data[i][2] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_COMPLETED));
                }
                else{
                    data[i][0]="0";
                    data[i][1]="0";
                    data[i][2]="0";
                }

            }
            //Get the color selected by the user
            DBUserManager dbUserManager = new DBUserManager (this);
            Cursor resultQueryUser = dbUserManager.selectUser(username);
            if(resultQueryUser.moveToFirst()==true) {
                color = resultQueryUser.getInt(resultQueryUser.getColumnIndex(DBUserManager.TU_COLOR));
            }
            //Set the data retrieved into the fragment view
            achievementFragment.setArgumentsToFragment(data,i,color,checkAchievementReleased());

        }
    }

    /*
    * Desc: load the achievements stored on the DDBB for each user, setting the data into a array
    *       and passing it to a fragment
    * Param:AchievementFragment3 with the fragment that will be created
    * */
    public void loadSecretAchievements(AchievementFragment3 achievementFragment){

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
            String [][] data = new String [6][4];
            //Open the DDBB manager
            DBAchievementsManager managerDBAchievements = new DBAchievementsManager(this);
            //Select the users ordered by XP points
            Cursor resultQuery;
            //iterate each user to save data into the array
            int i,color=-1;
            for(i=0;i<6;i++){
                switch (i){
                    case 0:
                        resultQuery = managerDBAchievements.selectAchievement("aSecretMeta",username);
                        break;
                    case 1:
                        resultQuery = managerDBAchievements.selectAchievement("aSecret1",username);
                        break;
                    case 2:
                        resultQuery = managerDBAchievements.selectAchievement("aSecret2",username);
                        break;
                    case 3:
                        resultQuery = managerDBAchievements.selectAchievement("aSecret3",username);
                        break;
                    case 4:
                        resultQuery = managerDBAchievements.selectAchievement("aSecret4",username);
                        break;
                    case 5:
                        resultQuery = managerDBAchievements.selectAchievement("aSecret5",username);
                        break;
                    default:
                        resultQuery = managerDBAchievements.selectAchievement("aSecretMeta",username);
                        break;
                }
                if(resultQuery.moveToFirst()==true) {
                    data[i][0] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS));
                    data[i][1] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS_MAX));
                    data[i][2] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_COMPLETED));
                    data[i][3] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_DISPLAYED));

                }
                else{
                    data[i][0]="0";
                    data[i][1]="0";
                    data[i][2]="0";
                    data[i][3]="0";

                }


            }
            //Get the color selected by the user
            DBUserManager dbUserManager = new DBUserManager (this);
            Cursor resultQueryUser = dbUserManager.selectUser(username);
            if(resultQueryUser.moveToFirst()==true) {
                color = resultQueryUser.getInt(resultQueryUser.getColumnIndex(DBUserManager.TU_COLOR));
            }
            //Set the data retrieved into the fragment view
            achievementFragment.setArgumentsToFragment(data,i,color,checkAchievementReleased());

        }
    }

    /*
    * Desc: load the quests recovered from the DDBB, setting the data into a array
    *       and passing it to a fragment
    * Param:AchievementFragment4 with the fragment that will be created
    * */
    public void loadQuests(AchievementFragment4 achievementFragment){

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
            String [][] data = new String [2][6];
            //Open the DDBB manager
            DBQuestsManager managerDBQuests = new DBQuestsManager(this);
            DBUserManager managerDB = new DBUserManager(this);

            //Select the user
            Cursor resultQueryUser = managerDB.selectUser(username);
            //If the user exists
            if (resultQueryUser.moveToFirst() == true) {

                Cursor resultQuery = managerDBQuests.selectQuest("Quest1", resultQueryUser.getString(resultQueryUser.getColumnIndex(DBUserManager.TU_LEVEL)));
                if(resultQuery.moveToFirst()==true) {
                    data[0][0] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_NAME));
                    data[0][1] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_DESCRIPTION));
                    data[0][2] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBQuestsManager.TQ_XP_REWARD));
                    data[0][3] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBQuestsManager.TQ_AP_REWARD));
                    data[0][4] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_INTERNAL_CITY));
                    data[0][5] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_INTERNAL_STREET));

                }
                else {
                    data[0][0] = "-";
                    data[0][1] = "-";
                    data[0][2] = "0";
                    data[0][3] = "0";
                    data[0][4] = "-";
                    data[0][5] = "-";
                }

                resultQuery = managerDBQuests.selectQuest("Quest2", resultQueryUser.getString(resultQueryUser.getColumnIndex(DBUserManager.TU_LEVEL)));
                if(resultQuery.moveToFirst()==true) {
                    data[1][0] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_NAME));
                    data[1][1] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_DESCRIPTION));
                    data[1][2] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBQuestsManager.TQ_XP_REWARD));
                    data[1][3] = "" + resultQuery.getInt(resultQuery.getColumnIndex(DBQuestsManager.TQ_AP_REWARD));
                    data[1][4] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_INTERNAL_CITY));
                    data[1][5] = resultQuery.getString(resultQuery.getColumnIndex(DBQuestsManager.TQ_INTERNAL_STREET));
                }
                else {
                    data[1][0] = "-";
                    data[1][1] = "-";
                    data[1][2] = "0";
                    data[1][3] = "0";
                    data[0][4] = "-";
                    data[0][5] = "-";
                }
                achievementFragment.setArgumentsToFragment(data);

            }
            else{
                achievementFragment.setArgumentsToFragment(new String[][]{{"","","","","",""},{"","","","","",""}});
            }
        }
    }



    public void onClickStartQuest1(View v){
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        TextView tvStreet   = (TextView) findViewById(R.id.tvQuest1Street);
        TextView tvCity     = (TextView) findViewById(R.id.tvQuest1City);
        TextView tvDesc     = (TextView) findViewById(R.id.tvQuestContentQ1);
        TextView tvAP       = (TextView) findViewById(R.id.tvQuestAPQ1);
        TextView tvXP       = (TextView) findViewById(R.id.tvQuestXPQ1);

        sharedpreferences.edit().putBoolean("questB", true).commit();
        sharedpreferences.edit().putString("quest", "Quest1").commit();
        sharedpreferences.edit().putString("questDesc", tvDesc.getText().toString()).commit();
        sharedpreferences.edit().putString("questCity", tvCity.getText().toString()).commit();
        sharedpreferences.edit().putString("questStreet", tvStreet.getText().toString()).commit();
        sharedpreferences.edit().putInt("questAP", Integer.parseInt(tvAP.getText().toString())).commit();
        sharedpreferences.edit().putInt("questXP", Integer.parseInt(tvXP.getText().toString())).commit();

        removeQuestNotification();

        Toast.makeText(this, "Quest selected", Toast.LENGTH_SHORT).show();


    }

    public void onClickStartQuest2(View v){
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        TextView tvStreet   = (TextView) findViewById(R.id.tvQuest2Street);
        TextView tvCity     = (TextView) findViewById(R.id.tvQuest2City);
        TextView tvDesc     = (TextView) findViewById(R.id.tvQuestContentDescriptionQ2);
        TextView tvAP       = (TextView) findViewById(R.id.tvQuestAPQ2);
        TextView tvXP       = (TextView) findViewById(R.id.tvQuestXPQ2);

        sharedpreferences.edit().putBoolean ("questB"       , true).commit();
        sharedpreferences.edit().putString("quest", "Quest2").commit();
        sharedpreferences.edit().putString("questDesc", tvDesc.getText().toString()).commit();
        sharedpreferences.edit().putString("questCity"    ,tvCity.getText().toString()).commit();
        sharedpreferences.edit().putString("questStreet"  , tvStreet.getText().toString()).commit();
        sharedpreferences.edit().putInt("questAP", Integer.parseInt(tvAP.getText().toString())).commit();
        sharedpreferences.edit().putInt("questXP"      , Integer.parseInt(tvXP.getText().toString())).commit();

        removeQuestNotification();
        Toast.makeText(this, "Quest selected", Toast.LENGTH_SHORT).show();

    }

    /*
    * Desc: put into the shared preferences quest notification one notif less
    * */
    public void removeQuestNotification(){
        //Get the number of notifications
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        int notifNumber=sharedpreferences.getInt("quest_notifications", 0);
        if(notifNumber==0){

        }
        else{
            sharedpreferences.edit().putInt("quest_notifications",0).commit();
        }

        loadNotificationQuests();

    }



}
