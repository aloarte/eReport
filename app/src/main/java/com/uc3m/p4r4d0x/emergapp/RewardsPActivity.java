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

public class RewardsPActivity extends AppCompatActivity {

    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_rewards_p);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Load the toolbar
        loadToolbar();
        //Load the color
        loadColor();

        //Load the progress
        loadProgress();

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
    /*
    * Desc: load the user content into the toolbar
    *
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
    * Desc: load the selected color on the toolbar
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
            Toolbar t= (Toolbar) findViewById(R.id.toolbarRP);
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
     * Desc: on click function to logout from the aplication
     */
    public void performLogout(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RewardsPActivity.this);
        View layView = (LayoutInflater.from(RewardsPActivity.this)).inflate(R.layout.confirm_logout, null);
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

    public void onClickShowQuest(View v){
        onClickShowQuest();
    }

    /*
     * Desc: on click function to show quests
     * */
    public void onClickShowQuest(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(RewardsPActivity.this);
        View layView = (LayoutInflater.from(RewardsPActivity.this)).inflate(R.layout.quest_content, null);
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
* Desc: on click method to navegate from toolbar to achievements activity
* */
    public void onClickChangeQuestActivity(View v){
        Intent myIntent= new Intent(getApplicationContext(), AchievementsActivity.class);
        startActivity(myIntent);

    }
}
