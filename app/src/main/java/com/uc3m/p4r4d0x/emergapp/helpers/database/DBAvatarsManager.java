package com.uc3m.p4r4d0x.emergapp.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alvaro Loarte Rodríguez on 22/07/16.
 *
 */

public class DBAvatarsManager {

    //Table Name
    public static final String TABLE_NAME="Avatars";
    //Fields Name
    public static final String TAV_ID = "_id";
    public static final String TAV_NAME_ID = "nameid";
    public static final String TAV_SOURCE = "source";
    public static final String TAV_UNLOCKED="unlocked";
    public static final String TAV_USER_NAME= "username";

    //Sql sentence for building the table titles
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + TAV_ID            + " integer primary key autoincrement,"
            + TAV_NAME_ID       + " text not null,"
            + TAV_SOURCE        + " integer not null,"
            + TAV_UNLOCKED      + " integer not null,"
            + TAV_USER_NAME     + " text not null,"
            + " FOREIGN KEY (" + TAV_USER_NAME + ") REFERENCES "+ DBUserManager.TABLE_NAME + "(" + DBUserManager.TU_NAME + ") ON DELETE CASCADE );";

    //DBHelper & SQLIteDatabase objects
    private DBHelper helper;
    private SQLiteDatabase db;

    //Constructor
    public DBAvatarsManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
    * Desc: Fills an ContentValue object with data
    * Param: Strings with title data
    * Ret: A filled ContentValues object
    * */
    public ContentValues generateCVAvatar( String nameId,int avatarSource, int avatarUnlocked ,String userName ){

        ContentValues contentV = new ContentValues();
        contentV.put(TAV_NAME_ID, nameId);
        contentV.put(TAV_SOURCE,avatarSource);
        contentV.put(TAV_UNLOCKED,avatarUnlocked);
        contentV.put(TAV_USER_NAME,userName);

        return contentV;
    }

    /*
    * Desc: Insert on database a new avatar
    * Param:  avatar data
    * */
    public boolean insertAvatar( String nameId,int avatarSource,int avatarUnlocked, String userName ){



            long retValue=0;
            retValue=db.insert(TABLE_NAME, null, generateCVAvatar(nameId, avatarSource, avatarUnlocked, userName));
            if(retValue==-1 || retValue==0){
                return false;
            }
            else return true;
    }

    /*
    * Desc: Select from the database an title
    * Param: the avatar id and the user name
    * Ret: Cursor object with the information
    * */
    public Cursor selectAvatar(String titleId,String userName){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TAV_NAME_ID   + "=\"" + titleId + "\""
                                                         +  "and "    + TAV_USER_NAME + "=\"" + userName + "\";" , null);
    }

    /*
    * Desc: Select from the database an title
    * Param: the avatar id and the user name
    * Ret: Cursor object with the information
    * */
    public Cursor selectUserTitles(String userName){
        return db.rawQuery(
                "SELECT * from " + TABLE_NAME + " where " +  TAV_USER_NAME + "=\"" + userName + "\";"
                , null);
    }

    /*
     * Desc: Upgrade one avatar in the database
     * Param: the avatar id and the user name
     * Ret: Long with the amount of elements affected
    * */
    public long upgradeAvatar(String nameId,int avatarSource, int avatarUnlocked ,String userName ){

        int id=0;

        //Get the title
        Cursor resultQuery= selectAvatar(nameId, userName);

        //If the title exists
        if(resultQuery.moveToFirst()==true) {

            //Get the id to make the update
            id = resultQuery.getInt(resultQuery.getColumnIndex(DBAvatarsManager.TAV_ID));
            //Update it
            return db.update(
                    TABLE_NAME,
                    generateCVAvatar(nameId,avatarSource ,avatarUnlocked, userName)
                    , TAV_NAME_ID + " LIKE ? ",new String[]{nameId});
        }
        else return -1;
    }

    /*
     * Desc: Upgrade one avatar in the database
     * Param: the avatar id and the user name and if the avatar is unlocked
     * Ret: Long with the amount of elements affected
     * */
    public long upgradeAvatarUnlocked(String nameId,int avatarUnlocked ,String userName ){
        long retValue=-1;
        Cursor resultQuery= selectAvatar(nameId, userName);

        //If the title exists
        if(resultQuery.moveToFirst()==true) {
            retValue=upgradeAvatar(
                    resultQuery.getString(resultQuery.getColumnIndex(DBAvatarsManager.TAV_NAME_ID)),
                    resultQuery.getInt(resultQuery.getColumnIndex(DBAvatarsManager.TAV_SOURCE)),
                    avatarUnlocked,
                    resultQuery.getString(resultQuery.getColumnIndex(DBAvatarsManager.TAV_USER_NAME))

            );
        }
        return retValue;


    }

    /*
    * Desc: Check if the avatar exist on the database
    * Param: the avatar id and the user name
    * Ret: true if avatar exist, false  if not
    * */
    public boolean avatarExist(String titleId, String userName){
        //Execute select and return if there is an element
        return selectAvatar(titleId, userName).moveToFirst();
    }




    }

