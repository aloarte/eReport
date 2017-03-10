package com.uc3m.p4r4d0x.emergapp.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alvaro Loarte Rodríguez on 22/07/16.
 *
 */

public class DBAchievementsManager {

    //Table Name
    public static final String TABLE_NAME="Achievements";
    //Fields Name
    public static final String TA_ID = "_id";

    public static final String TA_NAME_ID = "nameid";
    public static final String TA_NAME_ACHIEVEMENT="nameachievement";
    public static final String TA_REWARD_AP="rewardap";
    public static final String TA_REWARD_XP="rewardxp";
    public static final String TA_PROGRESS="progress";
    public static final String TA_PROGRESS_MAX="progressmax";
    public static final String TA_COMPLETED="completed";
    public static final String TA_DISPLAYED="displayed";
    public static final String TA_USER_NAME= "username";

    //Sql sentence for building the table achievements
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + TA_ID                  + " integer primary key autoincrement,"
            + TA_NAME_ID             + " text not null,"
            + TA_NAME_ACHIEVEMENT    + " text not null,"
            + TA_REWARD_AP           + " integer not null,"
            + TA_REWARD_XP           + " integer not null,"
            + TA_PROGRESS            + " integer not null,"
            + TA_PROGRESS_MAX        + " integer not null,"
            + TA_COMPLETED           + " integer not null,"
            + TA_DISPLAYED           + " integer not null,"
            + TA_USER_NAME           + " text not null,"
            + " FOREIGN KEY (" + TA_USER_NAME + ") REFERENCES "+ DBUserManager.TABLE_NAME + "(" + DBUserManager.TU_NAME + ") ON DELETE CASCADE );";

    //DBHelper & SQLIteDatabase objects
    private DBHelper helper;
    private SQLiteDatabase db;

    //Constructor
    public DBAchievementsManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
    * Desc: Fills an ContentValue object with data
    * Param: achievements data
    * Ret: A filled ContentValues object
    * */
    public ContentValues generateCVAchievement( String nameId,String nameAchievement,
                                          int rewardAP, int rewardXP, int progress,int progressMax,
                                          int achievementObtained,int displayed, String  userName ){

        ContentValues contentV = new ContentValues();
        contentV.put(TA_NAME_ID, nameId);
        contentV.put(TA_NAME_ACHIEVEMENT, nameAchievement);
        contentV.put(TA_REWARD_AP, rewardAP);
        contentV.put(TA_REWARD_XP, rewardXP);
        contentV.put(TA_PROGRESS, progress);
        contentV.put(TA_PROGRESS_MAX, progressMax);
        contentV.put(TA_COMPLETED, achievementObtained);
        contentV.put(TA_DISPLAYED, displayed );
        contentV.put(TA_USER_NAME,userName);

        return contentV;
    }

    /*
    * Desc: Insert on database a new achievement
    * Param: achievements data
    * */
    public boolean insertAchievement(String nameId,String nameAchievement,int progressMax,
                                        int rewardAP, int rewardXP, int obtained, int displayed, String  userName ){


        int progress=0;
        long retValue=0;
        retValue=db.insert(TABLE_NAME, null, generateCVAchievement(nameId, nameAchievement,
                rewardAP, rewardXP, progress,progressMax,
                obtained,displayed, userName));
        if(retValue==-1 || retValue==0){
            return false;
        }
        else return true;




    }

    /*
    * Desc: Select from the database an achievement
    * Param: the achievement id and the user name
    * Ret: Cursor object with the information
    * */
    public Cursor selectAchievement(String achievementId,String userName){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TA_NAME_ID   + "=\"" + achievementId + "\""
                                                         +  "and "    + TA_USER_NAME + "=\"" + userName + "\";" , null);
    }

    /*
     * Desc: Upgrade one achievement in the database
     * Param: the data to upgrade the achievement
     * Ret: Long with the amount of elements affected
    * */
    public long upgradeAchievement( String nameId,String nameAchievement,
                                    int rewardAP, int rewardXP, int progress,int progressMax,
                                    int achievementObtained,int displayed ,String  userName ){

        int id=0;

        //Get the achievements
        Cursor resultQuery= selectAchievement(nameId, userName);

        //If the achievements exists
        if(resultQuery.moveToFirst()==true) {

            //Get the id to make the update
            id = resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_ID));
            //Update it
            return db.update(
                    TABLE_NAME,
                    generateCVAchievement(nameId, nameAchievement,
                            rewardAP, rewardXP, progress,progressMax,
                            achievementObtained,displayed, userName),
                    TA_ID + " LIKE ? ",new String[]{""+id});
        }
        else return -1;
    }


    /*
     * Desc: Upgrade one achievement with the specific content. Calls upgradeAchievement
     * Param: the data to upgrade the achievement
     * Ret: Long with the amount of elements affected
    * */
    public long upgradeAchievementObtained(String nameId, int achievementObtained ,int progress,int displayed,String nameUser ){
        long retValue=-1;
        Cursor resultQuery= selectAchievement(nameId, nameUser);

        //If the achievements exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeAchievement(
                    resultQuery.getString(resultQuery.getColumnIndex(DBAchievementsManager.TA_NAME_ID)),
                    resultQuery.getString(resultQuery.getColumnIndex(DBAchievementsManager.TA_NAME_ACHIEVEMENT)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_REWARD_AP)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_REWARD_XP)),
                    progress,
                    resultQuery.getInt(resultQuery.getColumnIndex(DBAchievementsManager.TA_PROGRESS_MAX)),
                    achievementObtained,
                    displayed,
                    resultQuery.getString(resultQuery.getColumnIndex(DBAchievementsManager.TA_USER_NAME))

            );
        }
        return retValue;


    }

    /*
    * Desc: Check if the achievement exist on the database
    * Param: the id of the title and the name of the user
    * Ret: true if title exist, false  if not
    * */
    public boolean achievementsExist(String nameId, String userName){
        //Execute select and return if there is an element
        return selectAchievement(nameId, userName).moveToFirst();
    }




    }

