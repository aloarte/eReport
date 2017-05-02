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

public class AccountConfigurationActivity extends NavigationAchievementsActivity {

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
        super.onCreate(savedInstanceState, R.layout.activity_account_configuration);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        //Load account configuration items
        loadAcountConfiguration();






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


}
