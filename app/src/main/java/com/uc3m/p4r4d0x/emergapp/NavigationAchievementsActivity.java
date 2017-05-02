package com.uc3m.p4r4d0x.emergapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAchievementsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBAvatarsManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBTitlesManager;
import com.uc3m.p4r4d0x.emergapp.helpers.database.DBUserManager;

public class NavigationAchievementsActivity extends NavigationActivityInner {

    int achievementObtained=0;


    @Override
    protected void onCreate(Bundle savedInstanceState, int childLayout) {
        super.onCreate(savedInstanceState, childLayout);


    }



    //-----------------------------------------------------------//
    //---------------------ACHIEVEMENTS METHODS------------------//
    //-----------------------------------------------------------//

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

    /*
    * Desc: upgrade the novel achievement if is not already obtained
    * Param:string with the name of the novel achievement
    * */
    public void upgradeAchievementNovel(String achievement){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");


        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement(achievement, username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()) {
            //If the achievement is not obtained already
            if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0){
                //Upgrade the achievement to obtained
                managerDBAchiements.upgradeAchievementObtained(achievement, 1, 0, 1, username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaNovel();
                achievementObtained=1;



            }

        }

    }

    /*
    * Desc: upgrade the expert achievement if is not already obtained
    * Param:string with the name of the expert achievement
    * */
    public void upgradeAchievementExpert(String achievement){
        //Get sharedpreferences item and the username asociated
        sharedpreferences                  = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username                    = sharedpreferences.getString("username", "default");


        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement(achievement, username);
        //Check if the title selection is unlocked
        if(resultQuery.moveToFirst()) {
            //If the achievement is not obtained already
            if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0){
                //Upgrade the achievement to obtained
                managerDBAchiements.upgradeAchievementObtained(achievement, 1, 0,1, username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
                if(achievement.compareTo("aExpert6")==0){
                    //Unlock title "top reporter"
                    DBTitlesManager managerDBTitles = new DBTitlesManager(this);
                    managerDBTitles.upgradeTitleObtained("tTop", 1, username);
                    Toast.makeText(this,"New title unlocked!", Toast.LENGTH_LONG).show();
                }
            }

        }

    }

    /*
    * Desc: upgrade the secret achievement if is not already obtained
    * Param:string with the name of the secret achievement
    * */
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
                achievementObtained=1;



            }

        }

    }

    /*
    * Desc: Upgrade the meta achievement for novel achievements
    * */
    public void upgradeAchievementMetaNovel() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aNovelMeta", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            //If the achievement is not obtained already
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED)) == 0) {
                //If is the last achievement to complet progress
                if(     resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1 ==
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX))){
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aNovelMeta",
                            1,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX)),
                            1,
                            username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));

                    //Unlock the rest of achievements
                    DBUserManager managerDBUsers = new DBUserManager(this);
                    managerDBUsers.upgradeUserAchievementsUnlocked(username);
                    //Unlock title "begginer"
                    DBTitlesManager managerDBTitles = new DBTitlesManager(this);
                    managerDBTitles.upgradeTitleObtained("tBegginer",1,username);
                    Toast.makeText(this,"New title unlocked!", Toast.LENGTH_LONG).show();

                    achievementObtained=1;


                }
                else{
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aNovelMeta",
                            0,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1,
                            1, username);
                }


            }
        }
    }

    /*
    * Desc: Upgrade the meta achievement for expert achievements
    * */
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
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX)), 1, username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));

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

    /*
    * Desc: Upgrade the meta achievement for secret achievements
    * */
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
                    Toast.makeText(this,"New title unlocked!", Toast.LENGTH_LONG).show();


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

    /*
    * Desc: Upgrade ImagesLover achievement
    * param:a int with the number of images sended
    * */
    public void upgradeAchievementExpertImagesLover(int imagesSended) {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aExpert1", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            int currentNumberImages = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+imagesSended;
            int maxNumberImages     = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX));

            //If is the last achievement to complet progress
            if(currentNumberImages == maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert1",
                        1,
                        0,1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else if(currentNumberImages > maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert1",
                        1,
                        (currentNumberImages-maxNumberImages),1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else{
                if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0) {
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert1",
                            0,
                            currentNumberImages,1,
                            username);
                }
                else{
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert1",
                            1,
                            currentNumberImages,1,
                            username);
                }
            }
        }
    }

    /*
    * Desc: Upgrade VideosLover achievement
    * param:a int with the number of videos sended
    * */
    public void upgradeAchievementExpertVideosLover(int videosSended) {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aExpert2", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            int currentNumberImages = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+videosSended;
            int maxNumberImages     = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX));
            //If is the last achievement to complet progress
            if(currentNumberImages == maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert2",
                        1,
                        0,1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else if(currentNumberImages > maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert2",
                        1,
                        (currentNumberImages-maxNumberImages),1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else{
                if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0) {
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert2",
                            0,
                            currentNumberImages,1,
                            username);
                }
                else{
                    //Upgrade the achievement aNovelMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert2",
                            1,
                            currentNumberImages,1,
                            username);
                }
            }
        }
    }

    /*
    * Desc: Upgrade VideosLover achievement
    * param:a int with the number of videos sended
    * */
    public void upgradeAchievementExpertQuestsLover() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aExpert3", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            int currentNumberImages = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1;
            int maxNumberImages     = resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX));
            //If is the last achievement to complet progress
            if(currentNumberImages == maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert3",
                        1,
                        0,1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else if(currentNumberImages > maxNumberImages){
                //Upgrade the achievement aExpert1 to obtained
                managerDBAchiements.upgradeAchievementObtained("aExpert3",
                        1,
                        (currentNumberImages-maxNumberImages),1,
                        username);
                //Upgrade the XP and AP of the achievement
                changeUserStats(
                        username,
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                        resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                upgradeAchievementMetaExpert();
                achievementObtained=1;
            }
            else{
                if(resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED))==0) {
                    //Upgrade the achievement to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert3",
                            0,
                            currentNumberImages,1,
                            username);
                }
                else{
                    //Upgrade the achievement to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert3",
                            1,
                            currentNumberImages,1,
                            username);
                }
            }
        }
    }

    /*
    * Desc: Upgrade ReportingAnytwhere achievement
    * */
    public void upgradeAchievementExpertLocations() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aExpert7", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            int currentProgress= resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1;
            int maxProgress= resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX));
            //If the achievement is not obtained already
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED)) == 0) {
                //If is the last achievement to complet progress
                if(  currentProgress == maxProgress ){
                    //Upgrade the achievement aSecretMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aExpert7",
                            1,
                            maxProgress, 1, username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));
                    achievementObtained=1;
                    upgradeAchievementMetaExpert();
                }
                else{
                    //Upgrade the progress of achievement aSecretMeta
                    managerDBAchiements.upgradeAchievementObtained("aExpert7",
                            0,
                            currentProgress,1, username);
                }


            }
        }
    }

    /*
    * Desc: Upgrade FirstMyNeighbourhood achievement
    * */
    public void upgradeAchievementSecretLocations() {
        //Get sharedpreferences item and the username asociated
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String username = sharedpreferences.getString("username", "default");

        DBAchievementsManager managerDBAchiements = new DBAchievementsManager(this);
        //Make que query
        Cursor resultQuery = managerDBAchiements.selectAchievement("aSecret5", username);
        //Check if the title selection is unlocked
        if (resultQuery.moveToFirst()) {
            int currentProgress= resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS))+1;
            int maxProgress= resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_PROGRESS_MAX));

            //If the achievement is not obtained already
            if (resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_COMPLETED)) == 0) {
                //If is the last achievement to complet progress
                if(  currentProgress ==maxProgress ){
                    //Upgrade the achievement aSecretMeta to obtained
                    managerDBAchiements.upgradeAchievementObtained("aSecret5",
                            1,
                            maxProgress,1,username);
                    //Upgrade the XP and AP of the achievement
                    changeUserStats(
                            username,
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_AP)),
                            resultQuery.getInt(resultQuery.getColumnIndex(managerDBAchiements.TA_REWARD_XP)));

                    achievementObtained=1;
                    upgradeAchievementMetaSecret();
                }
                else{
                    //Upgrade the progress of achievement aSecretMeta
                    managerDBAchiements.upgradeAchievementObtained("aSecret5",
                            0,
                            currentProgress, 1,username);
                }


            }
        }
    }


    /*
    * Desc: Upgrade into the database the points and level for the logged user
    */
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

                }
            }
        }

        loadToolbar();
    }


    /*
    * Desc: check the current level with the xp points and set the next level if needed
    * Param: String with level and int with the xppoints
    * */
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
                    managerDBTitles.upgradeTitleObtained("tHero", 1, username);
                    Toast.makeText(this,"New title unlocked!", Toast.LENGTH_LONG).show();

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

    /*
    * Desc: check the current level with the xp points and set the next level if needed
    * Param: int with the appoints
    * */
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

    /*
  * Desc: Check from the DDBB if the user is in the top of the any ranking
  * */
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



}
