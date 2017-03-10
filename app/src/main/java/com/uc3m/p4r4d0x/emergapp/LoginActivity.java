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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAchievementsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAvatarsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBQuestsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBTitlesManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;


public class LoginActivity extends AppCompatActivity {

    Button bLogin,bNewAc;
    EditText etPassword,etUser;
    final String MyPREFERENCES="userPreferencesG1";
    SharedPreferences sharedpreferences;
    final int REQUEST_CODE_ASK_PERMISSIONS= 4;

    TextView tvFailLogin;
    int retriesLogin = 3;
    Context context;

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

    int colorSelected=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarL);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        checkPermissions();


        context= getApplicationContext();
        //Get the buttons
        bLogin=(Button)findViewById(R.id.bSSignIn);
        bNewAc=(Button)findViewById(R.id.bLSignIn);
        //Get the edit text fields
        etPassword=(EditText)findViewById(R.id.etPassword);
        etUser=(EditText)findViewById(R.id.etUser);
        //Get the text view for the retries
        tvFailLogin=(TextView)findViewById(R.id.tvRetry);
        //hide text view
        tvFailLogin.setVisibility(View.GONE);

        //Check if there is any user logged into the aplication checking shared preferences
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        insertInitialValues();

        //if there is no user
        if(username.compareTo("default")==0){
            //Continue:no session
        }
        else{

            changeToHomeActivity();
        }


    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Prepare the toolbar menu
    * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    * Desc: method overrided from AppCompatActivity
    *       this method is called when activity starts
    *       Prepare the elements on the toolbar menu
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.changeEmail:
                onClickShowChangeEmail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                    Toast.makeText(LoginActivity.this, "Some permissions have been rejected. Please, enable them to use this app.", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /*
  * Desc: on click function to change email
  * */
    public void onClickShowChangeEmail(){
        //Get the alert dialog based on the resource email_change_input
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(LoginActivity.this);
        View layView = (LayoutInflater.from(LoginActivity.this)).inflate(R.layout.email_change_input, null);
        alertBuilder.setView(layView);

        //Get the field
        final EditText userInput = (EditText) layView.findViewById(R.id.tvContentMessage);

        //Build the buttons on the alertbuilder
        alertBuilder.setCancelable(true)
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    //Set the email into the shared preferences
                    sharedpreferences.edit().putString("email_to_report", userInput.getText().toString()).commit();
                    Toast.makeText(getApplicationContext(), "Email to retrieve reports changed!", Toast.LENGTH_SHORT).show();

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            })
        ;

        Dialog dialog = alertBuilder.create();
        dialog.show();

    }

    public void insertInitialValues(){
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences.edit().putBoolean("first_time", true).commit();
        if (sharedpreferences.getBoolean("first_time", true)){
            insertExampleUsers();
            insertQuests();
            sharedpreferences.edit().putBoolean("first_time", false).commit();
        }
        else{
        }
    }


    public boolean insertExampleUsers(){

        DBUserManager dbUser= new DBUserManager(this);
        boolean users=
                        dbUser.insertFullFieldsUser("TestUser1", "1234", "admin1@gmail.com", "10/10/2010", "Veteran", "Begginer"              , 50 , 65, 0,R.mipmap.avatar_noavatar  , 1, 0, 0 ,1,1 ,0,"000000000","000000000")&
                        dbUser.insertFullFieldsUser("TestUser2", "1234", "admin2@gmail.com", "10/10/2010", "Veteran" , "-"              , 125, 100, 0,R.mipmap.avatar_hombre1  , 1, 1, 0 ,1,3 ,0 ,"110000000","110000000") &
                        dbUser.insertFullFieldsUser("TestUser3", "1234", "admin3@gmail.com", "10/10/2010", "Hero" , "-", 180, 325, 3,R.mipmap.avatar_hombre2 , 1, 1, 1 ,1,5 ,0,"111000000","111000000") &
                        dbUser.insertFullFieldsUser("TestUser4", "1234", "admin4@gmail.com", "10/10/2010", "Hero"    , "Hero"              , 215, 375, 4,R.mipmap.avatar_mujer2  , 1, 1, 1 ,1,6 ,1 ,"111110000","111110000") &
                        dbUser.insertFullFieldsUser("AdminUser", "1234", "admin5@gmail.com", "10/10/2010", "Legend"  , "Top Reporter"   , 320, 750, 7,R.mipmap.avatar_hipster1, 1, 1, 1 ,1,9 ,1,"111111111","111111111");
        boolean titles=
                insertUserTitles("TestUser1",2) & insertUserTitles("TestUser2",2) &
                        insertUserTitles("TestUser3",3) & insertUserTitles("TestUser4",3) &
                        insertUserTitles("AdminUser",1) ;

        boolean achievements=
                insertUserAchievementsWithNovelCompleted("TestUser1") & insertUserAchievementsWithNovelCompleted("TestUser2") &
                        insertUserAchievementsWithNovelCompleted("TestUser3") & insertUserAchievementsWithNovelCompleted("TestUser4") &
                        insertUserAchievementsCompleted("AdminUser") ;

        boolean avatars=
                insertUserAvatars("TestUser1",2) & insertUserAvatars("TestUser2",2) &
                        insertUserAvatars("TestUser3",3) & insertUserAvatars("TestUser4",3) &
                        insertUserAvatars("AdminUser",1) ;



        DBAchievementsManager dbAchievement = new DBAchievementsManager(this);

        return users & titles & achievements & avatars;

    }

    /*
     * Desc: Insert the titles of the user
     * Param: an String with the name of the user for calling the DDBB
     * Ret value: true or false if anything fails
     * */
    public boolean insertUserTitles(String username, int typeOfUser){
        DBTitlesManager titleDB = new DBTitlesManager(this);
        boolean bValueRet=false;
        switch(typeOfUser){
            //All unlocked
            case 1:
                bValueRet = titleDB.inserttitle("tBegginer", username,1) &
                        titleDB.inserttitle("tHero", username,1) &
                        titleDB.inserttitle("tTop", username,1) &
                        titleDB.inserttitle("tWorker", username,1) &
                        titleDB.inserttitle("tSeeker", username,1);
                break;
            //Just novel unlocked
            case 2:
                bValueRet = titleDB.inserttitle("tBegginer", username,1) &
                        titleDB.inserttitle("tHero", username,0) &
                        titleDB.inserttitle("tTop", username,0) &
                        titleDB.inserttitle("tWorker", username,0) &
                        titleDB.inserttitle("tSeeker", username,0);
                break;
            case 3:
                //Top title and hero title
                bValueRet = titleDB.inserttitle("tBegginer", username,1) &
                        titleDB.inserttitle("tHero", username,1) &
                        titleDB.inserttitle("tTop", username,1) &
                        titleDB.inserttitle("tWorker", username,0) &
                        titleDB.inserttitle("tSeeker", username,0);
                break;
        }
        return  bValueRet;

    }

    /*
     * Desc: Insert the achievements of the user
     * Param: an String with the name of the user for calling the DDBB
     * Ret value: true or false if anything fails
     * */
    public boolean insertUserAchievementsCompleted(String username){
        DBAchievementsManager achievementsDB = new DBAchievementsManager(this);
        boolean achievementsNovel, achievementsExpert, achievementsSecret;
        achievementsNovel=
                achievementsDB.insertAchievement("aNovelMeta","First Steps"                             ,5 ,50 ,50 ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel1"   ,"Photo Editor"                            ,0 ,5  ,5  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel2"   ,"Video Editor"                            ,0 ,5  ,5  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel3"   ,"Message Editor"                          ,0 ,5  ,0  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel4"   ,"Ubication Editor"                        ,0 ,5  ,0  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel5"   ,"Reporter"                                ,0 ,5  ,0  ,1,1,username) ;

        achievementsExpert= achievementsDB.insertAchievement("aExpertMeta","Community Helper"                       ,5 ,100,50,1,1,username) &
                achievementsDB.insertAchievement("aExpert1"   ,"Pictures Lover"                         ,10,10 ,10 ,1,1,username) &
                achievementsDB.insertAchievement("aExpert2"   ,"Videos Lover"                           ,10,10 ,10 ,1,1,username) &
                achievementsDB.insertAchievement("aExpert3"   ,"Quests Lover"                           ,5 ,10 ,10 ,1,1,username) &
                achievementsDB.insertAchievement("aExpert4"   ,"Expert Reporter"                        ,0 ,25 ,0  ,1,1,username) &
                achievementsDB.insertAchievement("aExpert5"   ,"Hard Worker"                            ,0 ,0  ,20 ,1,1,username) &
                achievementsDB.insertAchievement("aExpert6"   ,"Top Reporter"                           ,0 ,10 ,10 ,1,1,username) &
                achievementsDB.insertAchievement("aExpert7"   ,"Reporting Anywhere"                     ,3 ,20 ,10 ,1,1,username) ;

        achievementsSecret= achievementsDB.insertAchievement("aSecretMeta","Seeker of Truth"                        ,5 ,100,50 ,1,1,username) &
                achievementsDB.insertAchievement("aSecret1"   ,"I give my best"                         ,0 ,10 ,0  ,1,0,username) &
                achievementsDB.insertAchievement("aSecret2"   ,"An image is worth more than 1000 words" ,0 ,10 ,0  ,1,0,username) &
                achievementsDB.insertAchievement("aSecret3"   ,"As fast as I can"                       ,0 ,10 ,0  ,1,0,username) &
                achievementsDB.insertAchievement("aSecret4"   ,"Personal image is allways the first"    ,0 ,10 ,0  ,1,0,username) &
                achievementsDB.insertAchievement("aSecret5"   ,"First my neighborhood"                  ,0 ,10 ,0  ,1,0,username);


        return achievementsNovel & achievementsExpert & achievementsSecret;
    }

    /*
    * Desc: Insert the achievements of the user
    * Param: an String with the name of the user for calling the DDBB
    * Ret value: true or false if anything fails
    * */
    public boolean insertUserAchievementsWithNovelCompleted(String username){
        DBAchievementsManager achievementsDB = new DBAchievementsManager(this);
        boolean achievementsNovel, achievementsExpert, achievementsSecret;
        achievementsNovel=
                achievementsDB.insertAchievement("aNovelMeta","First Steps"                             ,5 ,50 ,50 ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel1"   ,"Photo Editor"                            ,0 ,5  ,5  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel2"   ,"Video Editor"                            ,0 ,5  ,5  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel3"   ,"Message Editor"                          ,0 ,5  ,0  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel4"   ,"Ubication Editor"                        ,0 ,5  ,0  ,1,1,username) &
                        achievementsDB.insertAchievement("aNovel5"   ,"Reporter"                                ,0 ,5  ,0  ,1,1,username) ;

        achievementsExpert= achievementsDB.insertAchievement("aExpertMeta","Community Helper"                       ,5 ,100,50,0,1,username) &
                achievementsDB.insertAchievement("aExpert1"   ,"Pictures Lover"                         ,10,10 ,10 ,0,1,username) &
                achievementsDB.insertAchievement("aExpert2"   ,"Videos Lover"                           ,10,10 ,10 ,0,1,username) &
                achievementsDB.insertAchievement("aExpert3"   ,"Quests Lover"                           ,5 ,10 ,10 ,0,1,username) &
                achievementsDB.insertAchievement("aExpert4"   ,"Expert Reporter"                        ,0 ,25 ,0  ,0,1,username) &
                achievementsDB.insertAchievement("aExpert5"   ,"Hard Worker"                            ,0 ,0  ,20 ,0,1,username) &
                achievementsDB.insertAchievement("aExpert6"   ,"Top Reporter"                           ,0 ,10 ,10 ,0,1,username) &
                achievementsDB.insertAchievement("aExpert7"   ,"Reporting Anywhere"                     ,3 ,20 ,10 ,0,1,username) ;

        achievementsSecret= achievementsDB.insertAchievement("aSecretMeta","Seeker of Truth"                        ,5 ,100,50 ,0,1,username) &
                achievementsDB.insertAchievement("aSecret1"   ,"I give my best"                         ,0 ,10 ,0  ,0,0,username) &
                achievementsDB.insertAchievement("aSecret2"   ,"An image is worth more than 1000 words" ,0 ,10 ,0  ,0,0,username) &
                achievementsDB.insertAchievement("aSecret3"   ,"As fast as I can"                       ,0 ,10 ,0  ,0,0,username) &
                achievementsDB.insertAchievement("aSecret4"   ,"Personal image is allways the first"    ,0 ,10 ,0  ,0,0,username) &
                achievementsDB.insertAchievement("aSecret5"   ,"First my neighborhood"                  ,0 ,10 ,0  ,0,0,username);


        return achievementsNovel & achievementsExpert & achievementsSecret;
    }

    /*
     * Desc: Insert the avatars of the user
     * Param: an String with the name of the user for calling the DDBB
     * Ret value: true or false if anything fails
     * */
    public boolean insertUserAvatars(String username,int typeOfUser){
        DBAvatarsManager avatarDB = new  DBAvatarsManager(this);
        boolean bValueRet=false;
        switch(typeOfUser){
            //All completed
            case 1:
                bValueRet=avatarDB.insertAvatar("avAvatarMan1"        , R.mipmap.avatar_hombre1  ,1, username) &
                        avatarDB.insertAvatar("avAvatarWoman1"      , R.mipmap.avatar_mujer1  , 1, username) &
                        avatarDB.insertAvatar("avAvatarMan2"        , R.mipmap.avatar_hombre2 , 1, username) &
                        avatarDB.insertAvatar("avAvatarWoman2"      , R.mipmap.avatar_mujer2  , 1, username) &
                        avatarDB.insertAvatar("avAvatarManHipster"  , R.mipmap.avatar_hipster1, 1, username) &
                        avatarDB.insertAvatar("avAvatarWomanHipster", R.mipmap.avatar_hipster2, 1, username);
                break;
            //1 avatar
            case 2:
                bValueRet=avatarDB.insertAvatar("avAvatarMan1"        , R.mipmap.avatar_hombre1  ,1, username) &
                        avatarDB.insertAvatar("avAvatarWoman1"      , R.mipmap.avatar_mujer1  , 1, username) &
                        avatarDB.insertAvatar("avAvatarMan2"        , R.mipmap.avatar_hombre2 , 1, username) &
                        avatarDB.insertAvatar("avAvatarWoman2"      , R.mipmap.avatar_mujer2  , 0, username) &
                        avatarDB.insertAvatar("avAvatarManHipster"  , R.mipmap.avatar_hipster1, 0, username) &
                        avatarDB.insertAvatar("avAvatarWomanHipster", R.mipmap.avatar_hipster2, 0, username);
                break;
            //2 avatars
            case 3:
                bValueRet=avatarDB.insertAvatar("avAvatarMan1"        , R.mipmap.avatar_hombre1  ,1, username) &
                        avatarDB.insertAvatar("avAvatarWoman1"      , R.mipmap.avatar_mujer1  , 1, username) &
                        avatarDB.insertAvatar("avAvatarMan2"        , R.mipmap.avatar_hombre2 , 1, username) &
                        avatarDB.insertAvatar("avAvatarWoman2"      , R.mipmap.avatar_mujer2  , 1, username) &
                        avatarDB.insertAvatar("avAvatarManHipster"  , R.mipmap.avatar_hipster1, 0, username) &
                        avatarDB.insertAvatar("avAvatarWomanHipster", R.mipmap.avatar_hipster2, 0, username);
                break;

        }
        return  bValueRet;

    }

    public boolean insertQuests(){
        DBQuestsManager dbQuest= new DBQuestsManager(this);
        boolean quest1Inserted=
                dbQuest.insertQuest("Q1Tr1", "", "Quest1","Las Rozas"          , "Las Rozas","" ,"Traveler",  5 ,  5 ) &
                        dbQuest.insertQuest("Q1Tr2", "", "Quest1","Getafe"             , "Getafe","", "Traveler",  5 ,  5 ) &
                        dbQuest.insertQuest("Q1Vt1", "", "Quest1","Leganes"            , "Leganés","", "Veteran" ,  10 , 5 ) &
                        dbQuest.insertQuest("Q1Vt2", "", "Quest1","Majadahonda"        , "Majadahonda","", "Veteran" ,  10 , 5 ) &
                        dbQuest.insertQuest("Q1Vt3", "", "Quest1","Alcorcón"           , "Alcorcón","", "Veteran" ,  10 , 5 ) &
                        dbQuest.insertQuest("Q1Ch1", "", "Quest1","Fuenlabrada"        , "Fuenlabrada","", "Champion",  10 , 10) &
                        dbQuest.insertQuest("Q1Ch2", "", "Quest1","Humanes de Madrid"  , "Humanes de Madrid","", "Champion",  10 , 10) &
                        dbQuest.insertQuest("Q1Ch3", "", "Quest1","Móstoles"           , "Móstoles","", "Champion",  10 , 10) &
                        dbQuest.insertQuest("Q1He1", "", "Quest1","Pinto"              , "Pinto","", "Hero"    ,  15 , 10) &
                        dbQuest.insertQuest("Q1He2", "", "Quest1","Parla"              , "Parla","", "Hero"    ,  15 , 10) &
                        dbQuest.insertQuest("Q1He3", "", "Quest1","Coslada"            , "Coslada","", "Hero"    ,  15 , 10) &
                        dbQuest.insertQuest("Q1Le1", "", "Quest1","Madrid"             , "Madrid","", "Legend"  ,  15 , 15) &
                        dbQuest.insertQuest("Q1Le2", "", "Quest1","Torrejón de Ardoz"  , "Torrejón de Ardoz","", "Legend"  ,  15 , 15);

        boolean quest2Inserted=
                dbQuest.insertQuest("Q1Tr1", "Waste evaluation"   , "Quest2","Calle del Maestro, Leganés,Madrid"                      , "Leganés","Calle del Maestro"           , "Traveler"  ,  10 , 5 ) &
                        dbQuest.insertQuest("Q1Tr2", "Street status"      , "Quest2","Calle Vía Dublín, Madrid, Comunidad de Madrid"          , "Madrid","Calle Vía Dublín"             , "Traveler"  ,  10 , 5 ) &
                        dbQuest.insertQuest("Q1Vt1", "Park evaluation"    , "Quest2","Av. de Menéndez Pelayo,Madrid, Comunidad de Madrid"    , "Madrid","Avenida de Menéndez Pelayo"    , "Veteran"   ,  15 , 10) &
                        dbQuest.insertQuest("Q1Vt2", "Waste evaluation"   , "Quest2","Calle Vía Dublín, Madrid, Comunidad de Madrid"          , "Madrid","Calle Vía Dublín", "Veteran"  ,  15 , 10) &
                        dbQuest.insertQuest("Q1Vt3", "Car crush wastes"   , "Quest2","Calle de Eugenia de Montijo,Madrid, Comunidad de Madrid", "Madrid","Calle de Eugenia de Montijo"  , "Veteran"   ,  15 , 10) &
                        dbQuest.insertQuest("Q1Ch1", "Minor car accident" , "Quest2","Calle del Arenal, Madrid, Comunidad de Madrid"          , "Madrid","Calle del Arenal", "Champion"  ,  20 , 15) &
                        dbQuest.insertQuest("Q1Ch2", "Park evaluation"    , "Quest2","Carr. Cdad. Universitaria,Madrid, Comunidad de Madrid " , "Madrid","Carretera Ciudad Universitaria", "Champion"  ,  20 , 15) &
                        dbQuest.insertQuest("Q1Ch3", "Waste evaluation"   , "Quest2","Av. Complutense, Madrid, Comunidad de Madrid          " , "Madrid","Avenida Complutense", "Champion"  ,  20 , 15) &
                        dbQuest.insertQuest("Q1He1", "Damaged building"   , "Quest2","Carr. Cdad. Universitaria,Madrid, Comunidad de Madrid " , "Madrid","Carretera Ciudad Universitaria", "Hero"      ,  20 , 20) &
                        dbQuest.insertQuest("Q1He2", "Car accident"       , "Quest2","Calle de Eugenia de Montijo,Madrid, Comunidad de Madrid", "Madrid","Calle de Eugenia de Montijo", "Hero"      ,  20 , 20) &
                        dbQuest.insertQuest("Q1He3", "Storm Remains"      , "Quest2","Av. de Logroño, Madrid, Comunidad de Madrid"            , "Madrid","Avenida de Logroño", "Hero"      ,  20 , 20) &
                        dbQuest.insertQuest("Q1Le1", "Burning"            , "Quest2","Calle del Arenal, Madrid, Comunidad de Madrid"          , "Madrid","Calle del Arenal", "Legend"    ,  25 , 25) &
                        dbQuest.insertQuest("Q1Le2", "Car accident"       , "Quest2","Av. de Atenas, Alcorcón, Comunidad de Madrid"           , "Alcorcón","Avenida de Atenas", "Legend"    ,  25 , 25) ;

        return quest1Inserted && quest2Inserted;

    }

    @Override
    public void onBackPressed() {
    }

    /*
     * Desc: onClickListener for button bLogIn
     *       Check if the login was successfull
     */
    public void onClickLogin(View v){
        //If the login was correct
        if (checkLogIn()) {
            // If the loggin is successfoul, save the user as a logged user into a shared preferences

            String username=etUser.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("username", username);

            //Using a switch, put the colors into the shared preferences based on the color selected by user in the bbdd
            switch(colorSelected){
                //DefaultColor
                case 0:
                    editor.putString("colorprimary", colors[0][1]);
                    editor.putString("colorsecondary",colors[0][2]);
                    break;
                //Red
                case 1:
                    editor.putString("colorprimary", colors[1][1]);
                    editor.putString("colorsecondary",colors[1][2]);
                    break;
                //Blue
                case 2:
                    editor.putString("colorprimary", colors[2][1]);
                    editor.putString("colorsecondary",colors[2][2]);
                    break;
                //Green
                case 3:
                    editor.putString("colorprimary", colors[3][1]);
                    editor.putString("colorsecondary",colors[3][2]);
                    break;
                //Purple
                case 4:
                    editor.putString("colorprimary", colors[4][1]);
                    editor.putString("colorsecondary",colors[4][2]);
                    break;
                //Yellow
                case 5:
                    editor.putString("colorprimary", colors[4][1]);
                    editor.putString("colorsecondary",colors[4][2]);
                    break;
                //Pink
                case 6:
                    editor.putString("colorprimary", colors[5][1]);
                    editor.putString("colorsecondary",colors[5][2]);
                    break;
                //Grey
                case 7:
                    editor.putString("colorprimary", colors[6][1]);
                    editor.putString("colorsecondary",colors[6][2]);
                    break;
                default:
                    break;
            }
            changeToHomeActivity();
            editor.commit();


        }
        //Wrong login
        else {
            //Change the retries text view
            tvFailLogin.setVisibility(View.VISIBLE);
            tvFailLogin.setBackgroundColor(Color.RED);
            retriesLogin--;
            tvFailLogin.setText(Integer.toString(retriesLogin));
            //If retries==0, set the login button to not enabled
            if (retriesLogin == 0) {
               bLogin.setEnabled(false);
            }

        }
    }

    /*
     * Desc: onClickListener for button bLogIn
     *       Swap to another activity (SignIn)
     */
    public void onClickSignIn(View v){
         //Crear un nuevo intent
         Intent myIntent = new Intent(v.getContext(), SignIn.class);
         //Iniciar actividad
         startActivity(myIntent);
    }

    /*
     * Desc: check the loggin process using the data from the DDBB and the data setted in the Text views
     * Ret:  A boolean true if the login was correct
     *
     */
    public boolean checkLogIn(){
        //Auxiliar strings
        String userS,passwordS,passwordSBBD;
        userS= etUser.getText().toString();
        passwordS =etPassword.getText().toString();
        //Control boolean
        boolean logged=true;
        //Database manager
        DBUserManager managerDB = new DBUserManager(this);

        //Select the user
        Cursor resultQuery= managerDB.selectUser(userS);
        //If the user exists
        if(resultQuery.moveToFirst()==true){
            //Set the color selected by the user (or default = 0)
            colorSelected = resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR));

            //Get the password by searching first the column index
            passwordSBBD = resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD));
            //if the password match
            if(passwordS.compareTo(passwordSBBD)==0){
                Toast.makeText(getApplicationContext(), "Login correct", Toast.LENGTH_SHORT).show();
            }
            //If password dont match
            else{
                logged=false;
                Toast.makeText(getApplicationContext(), "Wrong password.", Toast.LENGTH_SHORT).show();
            }
        }
        //If user dont exist
        else{
            logged=false;
            Toast.makeText(getApplicationContext(), "Wrong user.", Toast.LENGTH_SHORT).show();
        }

        return logged;
    }

    /*
     * Desc: Perform the launch of a new intent to navegate to HomeScreen activity
     *
     */
    public void changeToHomeActivity(){
        //Create and launch a new activity
        Intent myIntent = new Intent(context, HomeScreenActivity.class);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences.edit().putInt("quest_notifications", 2).commit();
        startActivity(myIntent);

    }

    /*
         * Desc: Check the permissions and request them to the user if necessary
         *  */
    public void checkPermissions(){
        //Check if some of the core permissions are not already granted
        if ((ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
                || (ContextCompat.checkSelfPermission(LoginActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)) {
            Toast.makeText(this, "Some permissions are not granted. Please enable them.", Toast.LENGTH_SHORT).show();

            //If so, request the activation of this permissions
            ActivityCompat.requestPermissions(LoginActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }
        else{
            //Permissions already granted
        }
    }




}
