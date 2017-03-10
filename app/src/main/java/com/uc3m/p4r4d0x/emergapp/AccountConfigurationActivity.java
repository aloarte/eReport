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
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAchievementsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAvatarsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBTitlesManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

import java.util.ArrayList;

public class AccountConfigurationActivity extends AppCompatActivity {

    //Info to use shared preferences to have a session
    final String MyPREFERENCES = "userPreferencesG1";
    SharedPreferences sharedpreferences;


    Spinner colorOptionsSpinner;
    String colorSelected="";
    int colorSelectedInt=0;


    int avatarImageSelected=-1;
    LinearLayout [] llArray = new LinearLayout[6];

    String [][] colors = new String[][] {
                                            {"Default","#009688","#4db6ac"},
                                            {"Red"    ,"#d32f2f","#ffcdd2"},
                                            {"Blue"   ,"#303f9f","#7986cb"},
                                            {"Green"  ,"#43a047","#a5d6a7"},
                                            {"Purple" ,"#8e24aa","#ba68c8"},
                                            {"Yellow" ,"#ffca28","#ffe082"},
                                            {"Pink"   ,"#e91e63","#f8bbd0"},
                                            {"Grey"   ,"#bdbdbd","#e0e0e0"}
    };

    int achievementObtained=0;

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Initialize all the neccessary parts of the main screen
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_account_configuration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAC);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //Load the toolbar
        loadToolbar();
        //Load the color
        loadColor();
        //Load account configuration items
        loadAcountConfiguration();
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
    * Desc: auxiliar function to retrieve the hex codes for each color
    *
    * */
    public String[] getColorsCodes(String color){
        String[] colorsToReturn = new String []{"",""};

        //Search for the requested color on the array
        for(int i=0; i<colors.length;i++){
            //When matches, fill the return string with the codes
            if(color.compareTo(colors[i][0])==0){
                colorsToReturn[0]=colors[i][1];
                colorsToReturn[1]=colors[i][2];
                colorSelectedInt=i;
                break;
            }
        }

        if(colorsToReturn[0].compareTo("")==0 || colorsToReturn[1].compareTo("")==0){
            colorsToReturn[0]="#009688";
            colorsToReturn[1]="#26a69a";
        }
        return colorsToReturn;

    }

    /*
    * Desc: insert the color ID to the DDBB of the current user
    *
    * */
    public void insertColorOnDDBB(int color){
        String username = sharedpreferences.getString("username", "default");
        if(username.compareTo("default")!=0){
            DBUserManager managerDB = new DBUserManager(this);
            managerDB.upgradeUserColor(username, color);
        }
    }

    /*
    * Desc: load the info in the toolbar
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
        * Desc: on click function to logout from the aplication
        * */
    public void performLogout(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AccountConfigurationActivity.this);
        View layView = (LayoutInflater.from(AccountConfigurationActivity.this)).inflate(R.layout.confirm_logout, null);
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
    * Desc: load the color selected by the user
    *
    * */
    public void loadColor(){

        //Check if there is any user logged into the aplication checking shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //Get the colors codes
        String primaryColor = sharedpreferences.getString("colorprimary", "default");
        String secondaryColor = sharedpreferences.getString("colorsecondary", "default");
        //if there is no color
        if(primaryColor.compareTo("default")==0 || secondaryColor.compareTo("default")==0){
            //Load default color
        }
        else{
            //Load the new color
            Toolbar t= (Toolbar) findViewById(R.id.toolbarAC);
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
     * Desc: load the activity info for the account configuration
     *
     * */
    public void loadAcountConfiguration(){
        //Load the data
        loadAcountConfigurationData();

        //Check if the titles are unlocked and if so, load them
        if(checkUnlockSelectColors()){
            loadColorsToSelect();
        }
    }

    /*
     * Desc: load the avatars data
     *
     * */
    public void loadAcountConfigurationData(){
        llArray[0] = (LinearLayout) findViewById(R.id.llAvatar1);
        llArray[1] = (LinearLayout) findViewById(R.id.llAvatar2);
        llArray[2] = (LinearLayout) findViewById(R.id.llAvatar3);
        llArray[3] = (LinearLayout) findViewById(R.id.llAvatar4);
        llArray[4] = (LinearLayout) findViewById(R.id.llAvatar5);
        llArray[5] = (LinearLayout) findViewById(R.id.llAvatar6);

        //Load the avatar checking all the avatars selected
        loadAvatars();

    }

    /*
    * Load the avatars unlocked by the user
    * */
    public void loadAvatars(){

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
            DBAvatarsManager managerDBAvatars = new DBAvatarsManager(this);

            //Select all the titles that the user have
            Cursor resultQuery = managerDBAvatars.selectUserTitles(username);
            //iterate each title
            for(resultQuery.moveToFirst();
                !resultQuery.isAfterLast();
                resultQuery.moveToNext()){

                //Get the title name and if is obtained
                nameTitleAux = resultQuery.getString(resultQuery.getColumnIndex(DBAvatarsManager.TAV_NAME_ID));
                obtainedAux  = resultQuery.getInt(resultQuery.getColumnIndex(DBAvatarsManager.TAV_UNLOCKED));

                //Switch by the title name, get the view and perform the view change
                switch (nameTitleAux){
                    case "avAvatarMan1":
                        LinearLayout llAvatar1 = (LinearLayout) findViewById(R.id.llAvatar1);
                        changeAvatarVisiblity(obtainedAux, llAvatar1);
                        break;
                    case "avAvatarWoman1":
                        LinearLayout llAvatar2 = (LinearLayout) findViewById(R.id.llAvatar2);
                        changeAvatarVisiblity(obtainedAux, llAvatar2);
                        break;
                    case "avAvatarMan2":
                        LinearLayout llAvatar3 = (LinearLayout) findViewById(R.id.llAvatar3);
                        changeAvatarVisiblity(obtainedAux, llAvatar3);
                        break;
                    case "avAvatarWoman2":
                        LinearLayout llAvatar4 = (LinearLayout) findViewById(R.id.llAvatar4);
                        changeAvatarVisiblity(obtainedAux, llAvatar4);
                        break;
                    case "avAvatarManHipster":
                        LinearLayout llAvatar5 = (LinearLayout) findViewById(R.id.llAvatar5);
                        changeAvatarVisiblity(obtainedAux, llAvatar5);
                        break;
                    case "avAvatarWomanHipster":
                        LinearLayout llAvatar6 = (LinearLayout) findViewById(R.id.llAvatar6);
                        changeAvatarVisiblity(obtainedAux,llAvatar6);
                        break;
                }
            }

        }
    }

    /*
     * Desc: Check from the DDBB if the user can select color changes
     * */
    public boolean checkUnlockSelectColors(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        boolean retValue=false;
        Log.d("ALR","LoadTitles");
        //Check the username
        if(username.compareTo("default")==0){
            //If is empty (error) do nothing
        }
        else {
            //Get the linear layout
            LinearLayout llSelectColors = (LinearLayout) findViewById(R.id.llProfileColorSelector);
            //Get the database manager
            DBUserManager managerDBUser = new DBUserManager(this);
            //Make que query
            Cursor resultQuery = managerDBUser.selectUser(username);
            //Check if the title selection is unlocked
            if(resultQuery.moveToFirst()==true) {
                if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_MODIFY_COLOR)) == 1) {
                    llSelectColors.setVisibility(View.VISIBLE);
                    retValue = true;
                } else {
                    llSelectColors.setVisibility(View.INVISIBLE);
                    retValue = false;
                }
            }
        }
        return retValue;
    }

    /*
    * Desc: Load the colors unlocked to change interface colors
    * */
    public void loadColorsToSelect(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
        DBUserManager managerDBUser = new DBUserManager(this);
        //Make a query to know if the user have all the colors unlocked
        Cursor resultQuery = managerDBUser.selectUser(username);
        boolean moreColorsUnlocked=false;
        if(resultQuery.moveToFirst()) {
            //Check if the extra color selection is unlocked
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_MORECOLORS_UNLOCKED)) == 1){
                moreColorsUnlocked=true;
            }
        }

        //Get the spinner
        colorOptionsSpinner = (Spinner) findViewById(R.id.spinnerAccountConfiguration);

        //Build an array list with the elements to fill the spinner
        ArrayList<ItemSpinnerData> alSpinnerData= new ArrayList<>();
        //Add all the elements
        alSpinnerData.add(new ItemSpinnerData("Default",R.drawable.defaultsquare));
        alSpinnerData.add(new ItemSpinnerData("Red",R.drawable.redsquare));
        alSpinnerData.add(new ItemSpinnerData("Blue",R.drawable.bluesquare));
        alSpinnerData.add(new ItemSpinnerData("Green",R.drawable.greensquare));
        //Check if the extra colors are unlocked
        if(moreColorsUnlocked) {
            alSpinnerData.add(new ItemSpinnerData("Purple", R.drawable.purplesquare));
            alSpinnerData.add(new ItemSpinnerData("Yellow", R.drawable.yellowsquare));
            alSpinnerData.add(new ItemSpinnerData("Pink", R.drawable.pinksquare));
            alSpinnerData.add(new ItemSpinnerData("Grey", R.drawable.greysquare));
        }


        //Build the spinner adapter using the array list
        SpinnerAdapter adapter = new SpinnerAdapter (this,R.layout.spinner_layout,R.id.tvSpinnerAccountConfiguration,alSpinnerData);
        //Set the adapter to the spinner to set the info in the spinner
        colorOptionsSpinner.setAdapter(adapter);

        colorOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorSelected=((TextView)view.findViewById(R.id.tvSpinnerAccountConfiguration)).getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        //Check if the avatar selection is unlocked
        if(resultQuery.moveToFirst()) {
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_MODIFY_AVATAR)) == 1) {
                retValue = true;
            } else {
                retValue = false;
            }
        }

        return retValue;
    }

    /*
    * Desc: Change the visibility on the button if the avatar is obtained
    * Par: int 1 obtained 0 not obtained, and the radio button view
    *
    * */
    public void changeAvatarVisiblity(int obtained, View titleRadioButton){
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
    * Desc: change the color of a selected image avatar to show that its selected
    * */
    public void markImageViewAvatar(int idLinearLayoutAvatar){

        //Iterate all the avatar images
        for(int i=0;i<6;i++){
            //If is the selected, mark it
            if(i==idLinearLayoutAvatar){
                llArray[i].setBackgroundColor(Color.argb(20, 84, 84, 84));
            }
            //if not, set its regular color
            else{
                llArray[i].setBackgroundColor(Color.WHITE);
            }
        }

    }


    // ----------------- ACHIEVEMENTS------------


    /**This methods are a copy from the methods in EmergencyActivity
     * They perform the same function, but in another activity
     * */

    public boolean checkAchievementReleased(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");

        boolean retValue=false;
        DBUserManager managerDBUser = new DBUserManager(this);
        //Make que query
        Cursor resultQuery = managerDBUser.selectUser(username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()) {
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBUser.TU_ACHIEVEMENTS_UNLOCKED)) == 1) {
                retValue = true;
            } else {
                retValue = false;
            }
        }
        return retValue;

    }

    public void changeUserStats(String username,int ap,int xp){

        DBUserManager managerDB = new DBUserManager(this);

        //Select the user
        Cursor resultQuery= managerDB.selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()) {
            int totalxpoints  = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)) + xp;
            int totalappoints = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)) + ap;
            managerDB.upgradeUserAPXPpoints(
                    username,
                    totalappoints,
                    totalxpoints
            );
            //Check if by the points the user have upgrade his level
            checkLevelUp(resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)), totalxpoints);
            checkRewardObtained(totalappoints);
            if(checkAchievementReleased()) {
                //Check if the achievement is completed and if so, upgrade the achievement
                if (totalappoints >= 250) {
                    upgradeAchievementExpert("aExpert5");
                }
                //Check if the user is in the ranking and if so, upgrade the achievement
                if (checkUserIsInTopRanking()) {
                    upgradeAchievementExpert("aExpert6");
                    //Unlock title "top reporter"
                    DBTitlesManager managerDBTitles = new DBTitlesManager(this);
                    managerDBTitles.upgradeTitleObtained("tTop", 1, username);
                    Toast.makeText(this,"New title unlocked", Toast.LENGTH_LONG).show();

                }
            }
        }

        loadToolbar();
    }

    public void checkLevelUp (String level, int xpoints){
        String username = sharedpreferences.getString("username", "default");
        DBUserManager managerDB = new DBUserManager(this);

        switch(level){
            case "Traveler":
                if(xpoints>=50){
                    Toast.makeText(this, "Level Up!", Toast.LENGTH_LONG).show();
                    managerDB.upgradeUserLevel(username,"Veteran");
                }
                break;
            case "Veteran":
                if(xpoints>=150){
                    Toast.makeText(this, "Level Up!", Toast.LENGTH_LONG).show();
                    managerDB.upgradeUserLevel(username, "Champion");
                }
                break;
            case "Champion":
                if(xpoints>=300){
                    Toast.makeText(this, "Level Up!", Toast.LENGTH_LONG).show();
                    managerDB.upgradeUserLevel(username,"Hero");
                    if(checkAchievementReleased())upgradeAchievementExpert("aExpert4");
                    //Unlock title "tHero"
                    DBTitlesManager managerDBTitles = new DBTitlesManager(this);
                    managerDBTitles.upgradeTitleObtained("tBegginer", 1, username);
                    Toast.makeText(this,"New title unlocked", Toast.LENGTH_LONG).show();

                }
                break;
            case "Hero":
                if(xpoints>=500){
                    Toast.makeText(this, "Level Up!", Toast.LENGTH_LONG).show();
                    managerDB.upgradeUserLevel(username,"Legend");
                }
                break;
            case "Legend":
                break;
            default:
                break;

        }
    }

    public void checkRewardObtained(int appoints){
        String username = sharedpreferences.getString("username", "default");
        //Get the managers for the DDBB
        DBUserManager managerUsersDB      = new DBUserManager(this);
        DBAvatarsManager managerAvatarsDB = new DBAvatarsManager(this);
        DBTitlesManager managerTitlesDB   = new DBTitlesManager(this);

        //Select the user
        Cursor resultQuery                 = managerUsersDB.selectUser(username);
        //If the user exists
        if(resultQuery.moveToFirst()) {
            //Get the progress achieved by the user
            int userProgress = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED));

            //Switch the progress
            switch(userProgress){
                //Nothing unlocked yet
                case 0:
                    if(appoints>=50){
                        Toast.makeText(this, "Title change unlocked!", Toast.LENGTH_LONG).show();
                        managerUsersDB.upgradeUserUnlockTitleAvatarColor(username,
                                1,
                                0,
                                0
                        );
                        managerUsersDB.upgradeUserProgressRewards(username);

                    }
                    break;
                //1 element unlocked
                case 1:
                    if(appoints>=100){
                        Toast.makeText(this, "Avatar change unlocked!", Toast.LENGTH_LONG).show();
                        managerUsersDB.upgradeUserUnlockTitleAvatarColor(username,
                                1,
                                1,
                                0
                        );
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //2 element unlocked
                case 2:
                    if(appoints>=125){
                        Toast.makeText(this, "Avatar unlocked!", Toast.LENGTH_LONG).show();
                        managerAvatarsDB.upgradeAvatarUnlocked("avAvatarMan2",1,username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //3 element unlocked
                case 3:

                    if(appoints>=150){
                        Toast.makeText(this, "Color change unlocked!", Toast.LENGTH_LONG).show();
                        managerUsersDB.upgradeUserUnlockTitleAvatarColor(username,
                                1,
                                1,
                                1
                        );
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //4 element unlocked
                case 4:
                    if(appoints>=175){
                        Toast.makeText(this, "Avatar unlocked", Toast.LENGTH_LONG).show();
                        managerAvatarsDB.upgradeAvatarUnlocked("avAvatarWoman2", 1, username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //5 element unlocked
                case 5:
                    if(appoints>=200){
                        Toast.makeText(this, "New colors unlocked!", Toast.LENGTH_LONG).show();
                        managerUsersDB.upgradeUserMoreColors(username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //6 element unlocked
                case 6:
                    if(appoints>=225){
                        Toast.makeText(this, "Avatar unlocked!", Toast.LENGTH_LONG).show();
                        managerAvatarsDB.upgradeAvatarUnlocked("avAvatarManHipster", 1, username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //7 element unlocked
                case 7:
                    if(appoints>=250){
                        Toast.makeText(this, "New title unlocked!", Toast.LENGTH_LONG).show();
                        managerTitlesDB.upgradeTitleObtained("tWorker", 1, username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;
                //8 element unlocked
                case 8:
                    if(appoints>=275){
                        Toast.makeText(this, "Avatar unlocked!", Toast.LENGTH_LONG).show();
                        managerAvatarsDB.upgradeAvatarUnlocked("avAvatarWomanHipster", 1, username);
                        managerUsersDB.upgradeUserProgressRewards(username);
                    }
                    break;

                default:
                    break;


            }
        }

    }

    public boolean checkUserIsInTopRanking(){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");
        boolean retValue=false;
        DBUserManager managerDBUser = new DBUserManager(this);
        //Make the querys
        Cursor resultQueryAP = managerDBUser.selectUsersOrderedByAP();
        Cursor resultQueryXP = managerDBUser.selectUsersOrderedByXP();

        //Search the three top users and check if the current user is there
        int i;
        for(i=0,resultQueryAP.moveToFirst(),resultQueryXP.moveToFirst();
            i<3 && !resultQueryAP.isAfterLast() && !resultQueryXP.isAfterLast();
            i++,resultQueryAP.moveToNext(),resultQueryXP.moveToNext()){

            //check if the user is at the position in the ranking
            if (   resultQueryAP.getString(resultQueryAP.getColumnIndex(managerDBUser.TU_NAME)).compareTo(username)==0
                    || resultQueryXP.getString(resultQueryXP.getColumnIndex(managerDBUser.TU_NAME)).compareTo(username)==0
                    ){
                retValue=true;
                break;
            }
        }
        return retValue;
    }

    public void upgradeAchievementExpert(String achievement){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");


        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement(achievement, username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()==true) {
            //If the achievement is not obtained already
            if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0){
                //Upgrade the achievement to obtained
                managerDBAchiements.upgradeAchievementObtained(achievement, 1, 0,1, username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                Toast.makeText(this,"Achievement completed", Toast.LENGTH_LONG).show();

                upgradeAchievementMetaExpert();
                achievementObtained=1;



            }

        }

    }

    public void upgradeAchievementSecret(String achievement){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");


        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement(achievement, username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()==true) {
            //If the achievement is not obtained already
            if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0){
                //Upgrade the achievement to obtained
                managerDBAchiements.upgradeAchievementObtained(achievement, 1, 0, 1, username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaSecret();
                Toast.makeText(this,"Achievement completed", Toast.LENGTH_LONG).show();
                achievementObtained=1;



            }

        }

    }

    public void upgradeAchievementMetaExpert() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aExpertMeta", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            //If the achievement is not obtained already
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED)) == 0) {
                //If is the last achievement to complet progress
                if(     resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1 ==
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX))){
                    //Upgrade the achievement aExpertMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpertMeta",
                            1,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX)),1, username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                    Toast.makeText(this,"Achievement completed", Toast.LENGTH_LONG).show();

                }
                else{
                    //Upgrade the progress of achievement aExpertMeta
                    managerDBAchiements.upgradeAchievementObtained("aExpertMeta",
                            0,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1,1, username);
                }


            }
        }
    }

    public void upgradeAchievementMetaSecret() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aSecretMeta", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            //If the achievement is not obtained already
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED)) == 0) {
                //If is the last achievement to complet progress
                if(     resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1 ==
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX))){
                    //Upgrade the achievement aSecretMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aSecretMeta",
                            1,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX)),
                            1,
                            username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                    //Unlock title "seeker of truth"
                    DBTitlesManager managerDBTitles = new DBTitlesManager(this);
                    managerDBTitles.upgradeTitleObtained("tSeeker", 1, username);
                    Toast.makeText(this,"Achievement completed", Toast.LENGTH_LONG).show();
                    Toast.makeText(this,"New title unlocked", Toast.LENGTH_LONG).show();


                }
                else{
                    //Upgrade the progress of achievement aSecretMeta
                    managerDBAchiements.upgradeAchievementObtained("aSecretMeta",
                            0,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1,
                            1,
                            username);
                }


            }
        }
    }



    //ON CLICK METHODS

    /*
    * Desc: on click method to change for the button to change the app color style
    *
    * */
    public void onClickChangeColor(View v){

        //if the color is not chosen , skip
        if(colorSelected.compareTo("")!=0){
            String[] colorsToSet=getColorsCodes(colorSelected);


            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("colorprimary", colorsToSet[0]);
            editor.putString("colorsecondary",colorsToSet[1]);
            editor.commit();

            //insert on the database that color
            insertColorOnDDBB(colorSelectedInt);

            loadColor();
        }


    }

    /*
     * Desc: on click method to select an avatar
     * */
    public void onClickSelectAvatar(View v){
        int elementId=v.getId();
        switch(elementId){
            case R.id.ivACAvatarImage1:
                markImageViewAvatar(0);
                avatarImageSelected=R.mipmap.avatar_hombre2;
                break;
            case R.id.ivACAvatarImage2:
                markImageViewAvatar(1);
                avatarImageSelected=R.mipmap.avatar_mujer2;
                break;
            case R.id.ivACAvatarImage3:
                markImageViewAvatar(2);
                avatarImageSelected=R.mipmap.avatar_hombre1;
                break;
            case R.id.ivACAvatarImage4:
                markImageViewAvatar(3);
                avatarImageSelected=R.mipmap.avatar_mujer1;
                break;
            case R.id.ivACAvatarImage5:
                markImageViewAvatar(4);
                avatarImageSelected=R.mipmap.avatar_hipster1;
                break;
            case R.id.ivACAvatarImage6:
                markImageViewAvatar(5);
                avatarImageSelected=R.mipmap.avatar_hipster2;
                break;
            default:
                break;
        }

    }

    /*
     * Desc: on click method to save the selected avatar
     * */
    public void onClickSaveAvatar(View v){
        String username = sharedpreferences.getString("username", "default");

        if(username.compareTo("default")!=0 && avatarImageSelected!=-1) {
            DBUserManager managerDB = new DBUserManager(this);
            managerDB.upgradeUserAvatar(username, avatarImageSelected);
            upgradeAchievementSecret("aSecret4");
            loadToolbar();
        }
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(AccountConfigurationActivity.this);
        View layView = (LayoutInflater.from(AccountConfigurationActivity.this)).inflate(R.layout.quest_content, null);
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
