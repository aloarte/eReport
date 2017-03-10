package com.uc3m.p4r4d0x.emergapp.helpers.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.uc3m.p4r4d0x.emergapp.R;

/**
 * Created by Alvaro Loarte Rodríguez on 19/02/16.
 *
 */

public class DBUserManager {

    //Table Name
    public static final String TABLE_NAME="Users";
    //Fields Name
    public static final String TU_ID = "_id";
    public static final String TU_NAME= "name";
    public static final String TU_PASSWORD="password";
    public static final String TU_EMAIL="email";
    public static final String TU_DATE="date";
    public static final String TU_LEVEL="level";
    public static final String TU_AP_POINTS="ap_points";
    public static final String TU_XP_POINTS="xp_points";
    public static final String TU_TITLE="title";
    public static final String TU_COLOR="color";
    public static final String TU_AVATAR="avatar";
    public static final String TU_MODIFY_TITLE="modify_title";
    public static final String TU_MODIFY_AVATAR="modify_avatar";
    public static final String TU_MODIFY_COLOR="modify_color";
    public static final String TU_LAST_LOCATION="last_location";
    public static final String TU_REPEAT_LOCATION="repeat_location";
    public static final String TU_LOCATION1="loc1";
    public static final String TU_LOCATION2="loc2";
    public static final String TU_ACHIEVEMENTS_UNLOCKED="ach_unl";
    public static final String TU_PROGRESS_UNLOCKED="progress";
    public static final String TU_ELEMENTS_UNLOCKED="elementsunlocked";
    public static final String TU_ELEMENTS_UNLOCKED_VIEW="elementsunlockedview";
    public static final String TU_MORECOLORS_UNLOCKED="morecolors";


    //Sql sentence for building the table Users
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            //+ TU_ID             + " integer primary key autoincrement,"
            + TU_NAME           + " text primary key not null,"
            + TU_PASSWORD       + " text not null,"
            + TU_EMAIL          + " text not null,"
            + TU_DATE           + " text not null,"
            + TU_LEVEL          + " text not null,"
            + TU_AP_POINTS      + " integer not null,"
            + TU_XP_POINTS      + " integer not null,"
            + TU_TITLE          + " text,"
            + TU_COLOR          + " integer not null,"
            + TU_AVATAR         + " integer not null,"
            + TU_MODIFY_TITLE   + " integer not null,"
            + TU_MODIFY_AVATAR  + " integer not null,"
            + TU_MODIFY_COLOR   + " integer not null,"
            + TU_LAST_LOCATION  + " text,"
            + TU_REPEAT_LOCATION+ " integer,"
            + TU_LOCATION1      + " text,"
            + TU_LOCATION2      + " text,"
            + TU_ACHIEVEMENTS_UNLOCKED + " integer not null,"
            + TU_PROGRESS_UNLOCKED + " integer not null,"
            + TU_ELEMENTS_UNLOCKED + " text not null,"
            + TU_ELEMENTS_UNLOCKED_VIEW + " text not null,"
            + TU_MORECOLORS_UNLOCKED + " integer not null);";


    ;


    //DBHelper & SQLIteDatabase objects
    private DBHelper helper;
    private SQLiteDatabase db;


    //Constructor
    public DBUserManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /*


    * Desc: Fills an ContentValue object with data
    * Param: Strings with user data
    * Ret: A filled ContentValues object
    * */
    public ContentValues generateCVUser( String name, String password, String email, String date,
                                     String level,int ap_points,int xp_points, String title, int color,int avatar,
                                     int modifTitle,int modifImage, int modifColor
                                     ,String lastlocation,int timesRepeated ,String loc1,String loc2,
                                         int achievementsUnlocked,int progress,String elementsUnlocked,String elementsUnlockedViews,
                                         int morecolors){

        ContentValues contentV = new ContentValues();
        contentV.put(TU_NAME,name);
        contentV.put(TU_PASSWORD,password);
        contentV.put(TU_EMAIL,email);
        contentV.put(TU_DATE,date);
        contentV.put(TU_LEVEL,level);
        contentV.put(TU_AP_POINTS,ap_points);
        contentV.put(TU_XP_POINTS,xp_points);
        contentV.put(TU_TITLE,title);
        contentV.put(TU_COLOR,color);
        contentV.put(TU_AVATAR,avatar);
        contentV.put(TU_MODIFY_TITLE,modifTitle);
        contentV.put(TU_MODIFY_AVATAR,modifImage);
        contentV.put(TU_MODIFY_COLOR,modifColor);
        contentV.put(TU_LAST_LOCATION,lastlocation);
        contentV.put(TU_REPEAT_LOCATION,timesRepeated);
        contentV.put(TU_LOCATION1,loc1);
        contentV.put(TU_LOCATION2,loc2);
        contentV.put(TU_ACHIEVEMENTS_UNLOCKED,achievementsUnlocked);
        contentV.put(TU_PROGRESS_UNLOCKED,progress);
        contentV.put(TU_ELEMENTS_UNLOCKED,elementsUnlocked);
        contentV.put(TU_ELEMENTS_UNLOCKED_VIEW,elementsUnlockedViews);
        contentV.put(TU_MORECOLORS_UNLOCKED,morecolors);


        return contentV;
    }

    /*
    * Desc: Insert on database a new user
    * Param: Strings with user data
    * */
    public boolean insertUser(String name, String password, String email, String date){
        String level="Traveler",title="-",lastLocation="",loc1="",loc2="";
        int APpoints=0,XPpoints=0, achievementUnlocked=0,progress=0;
        int color=0;
        int modifTitle = 0, modifAvatar = 0, modifColor = 0, morecolors = 0;
        int repeatedLocation=0;
        int avatar= R.mipmap.avatar_noavatar;
        String elementsUnlocked="000000000",elementsUnlockedViews="000000000";
        long retValue=0;
        retValue=db.insert(TABLE_NAME, null, generateCVUser(name, password, email, date,level,
                                                        APpoints,XPpoints,title,color,avatar,
                                                        modifTitle,modifAvatar,modifColor,
                                                        lastLocation,repeatedLocation,loc1,loc2,achievementUnlocked,progress,
                                                        elementsUnlocked,elementsUnlockedViews,morecolors));
        if(retValue==-1 || retValue==0){
            return false;
        }
        else{
            return true;
        }

    }

    /*
    * Desc: Select from the database an user
    * Param: the nickname of the user
    * Ret: Cursor object with the information
    * */
    public Cursor selectUser(String userName){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TU_NAME + "=\"" + userName + "\";", null);
    }

    /*
    * Desc: Select from the database an user ordered by APpoints
    * Param: the nickname of the user
    * Ret: Cursor object with the information
    * */
    public Cursor selectUsersOrderedByAP(){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY "+TU_AP_POINTS + " DESC;", new String[]{});
    }

    /*
   * Desc: Select from the database an user ordered by XPpoints
   * Param: the nickname of the user
   * Ret: Cursor object with the information
   * */
    public Cursor selectUsersOrderedByXP(){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " ORDER BY "+TU_XP_POINTS + " DESC;", new String[]{});
    }


    /*
     * Desc: Upgrade one user in the database
     * Param: Strings with user data, and integers with the points and the level
     * Ret: Long with the amount of elements affected
    * */
    public long upgradeUser(String name, String password, String email, String date,
                        String level,int ap_points,int xp_points, String title, int color,int avatar,
                        int modifTitle,int modifImage, int modifColor,
                            String lastLocation,int repeatedLocation,String loc1,String loc2,
                            int achievementsUnlocked,int progress,String elementsUnlocked,
                            String elementsUnlockedViews      ,int morecolors){

        return db.update(
                TABLE_NAME,
                generateCVUser(name, password, email, date, level,
                        ap_points, xp_points, title, color, avatar,
                        modifTitle, modifImage, modifColor,
                        lastLocation, repeatedLocation, loc1, loc2, achievementsUnlocked,
                        progress, elementsUnlocked,elementsUnlockedViews,morecolors)
                , TU_NAME + " LIKE ? ", new String[]{name});
    }


    public long upgradeUserColor(String username,int color){
        long retValue=-1;
        Cursor resultQuery = selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst() == true) {
            retValue = upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    color,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))

            );
        }
        return retValue;


    }

    public long upgradeUserAvatar(String username,int avatar){
        long retValue=-1;
        Cursor resultQuery = selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst() == true) {
            retValue = upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    avatar,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;


    }

    public long upgradeUserTitle(String username,String title){
        long retValue=-1;
        Cursor resultQuery = selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst() == true) {
            retValue = upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    title,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;
    }

    public long upgradeUserLevel(String username,String rank) {
        long retValue = -1;
        Cursor resultQuery = selectUser(username);

        //If the user exists
        if (resultQuery.moveToFirst() == true) {
            retValue = upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    rank,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;
    }

    public long upgradeUserAPXPpoints(String username,int appoints,int xppoints){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue= upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    appoints,
                    xppoints,
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;


    }

    public long upgradeUserUnlockTitleAvatarColor(String username,int unlockT, int unlockA, int unlockC) {
        long retValue = -1;
        Cursor resultQuery = selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    unlockT,
                    unlockA,
                    unlockC,
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;


    }

    public long upgradeUserLastLocation(String username,String lastLocation,int timesLocation){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    lastLocation,
                    timesLocation,
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;


    }

    public long upgradeUserLocations1AND2(String username,String loc1,String loc2){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    loc1,
                    loc2,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;


    }

    public long upgradeUserAchievementsUnlocked(String username){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    1,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;
    }

    public long upgradeUserProgressRewards(String username){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED))+1,
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MORECOLORS_UNLOCKED))
            );
        }
        return retValue;
    }

    public long upgradeUserMoreColors(String username){
        long retValue=-1;
        Cursor resultQuery= selectUser(username);

        //If the user exists
        if (resultQuery.moveToFirst() == true) {
            retValue=upgradeUser(
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_NAME)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_PASSWORD)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_EMAIL)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_DATE)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LEVEL)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AP_POINTS)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_XP_POINTS)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_COLOR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_TITLE)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_AVATAR)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_MODIFY_COLOR)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LAST_LOCATION)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_REPEAT_LOCATION)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION1)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_LOCATION2)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_ACHIEVEMENTS_UNLOCKED)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBUserManager.TU_PROGRESS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBUserManager.TU_ELEMENTS_UNLOCKED_VIEW)),
                    1
            );
        }
        return retValue;
    }

    /*
    * Desc: Check if the user exist on the database
    * Param: the nickname of the user
    * Ret: true if user exist, false  if not
    * */
    public boolean userExist(String userName){
        //Execute select and return if there is an element
        return selectUser(userName).moveToFirst();
    }


    /*
    * Desc: Insert on database a new user
    * Param: Strings with user data
    * */
    public boolean insertFullFieldsUser(  String name, String password, String email
                                        , String date, String level,String title
                                        , int APpoints, int XPpoints, int color, int avatar
                                        , int modifTitle ,int modifAvatar,int modifColor,
                                          int achievementUnlocked,int progressUnlocked,int morecolors,
                                          String elementsUnlocked,String elementsUnlockedViews
                                        ){

        String lastLocation="",loc1="",loc2="";
        long retValue=0;
        int timesLocation=0;
        retValue=db.insert(TABLE_NAME, null, generateCVUser(name, password, email, date,level,
                APpoints,XPpoints,title,color,avatar,
                modifTitle,modifAvatar,modifColor,
                lastLocation,timesLocation,loc1,loc2,achievementUnlocked,progressUnlocked,
                elementsUnlocked,elementsUnlockedViews,morecolors));
        if(retValue==-1 || retValue==0){
            return false;
        }
        else{
            return true;
        }

    }




    }

