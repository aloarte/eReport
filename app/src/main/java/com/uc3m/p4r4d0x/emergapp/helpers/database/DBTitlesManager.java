package com.uc3m.p4r4d0x.emergapp.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alvaro Loarte Rodríguez on 22/07/16.
 *
 */

public class DBTitlesManager {

    //Table Name
    public static final String TABLE_NAME="Titles";
    //Fields Name
    public static final String TT_ID = "_id";
    public static final String TT_NAME_ID = "nameid";
    public static final String TT_OBTAINED="obtained";
    public static final String TT_USER_NAME= "username";

    //Sql sentence for building the table titles
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + TT_ID            + " integer primary key autoincrement,"
            + TT_NAME_ID       + " text not null,"
            + TT_OBTAINED      + " integer not null,"
            + TT_USER_NAME     + " text not null,"
            + " FOREIGN KEY (" + TT_USER_NAME + ") REFERENCES "+ DBUserManager.TABLE_NAME + "(" + DBUserManager.TU_NAME + ") ON DELETE CASCADE );";

    //DBHelper & SQLIteDatabase objects
    private DBHelper helper;
    private SQLiteDatabase db;

    //Constructor
    public DBTitlesManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
    * Desc: Fills an ContentValue object with data
    * Param: Strings with title data
    * Ret: A filled ContentValues object
    * */
    public ContentValues generateCVtitle( String nameId, int titleObtained ,String userName ){

        ContentValues contentV = new ContentValues();
        contentV.put(TT_NAME_ID, nameId);
        contentV.put(TT_OBTAINED,titleObtained);
        contentV.put(TT_USER_NAME,userName);

        return contentV;
    }

    /*
    * Desc: Insert on database a new title
    * Param:  title data
    * */
    public boolean inserttitle(String nameId, String nameUser,int obtained){
     long retValue=0;
        retValue=db.insert(TABLE_NAME, null, generateCVtitle(nameId, obtained, nameUser));
        if(retValue==-1 || retValue==0){
            return false;
        }
        else return true;

    }

    /*
    * Desc: Select from the database an title
    * Param: the title id and the user name
    * Ret: Cursor object with the information
    * */
    public Cursor selecttitle(String titleId,String userName){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TT_NAME_ID   + "=\"" + titleId + "\""
                                                         +  "and "    + TT_USER_NAME + "=\"" + userName + "\";" , null);
    }

    /*
    * Desc: Select from the database an title
    * Param: the title id and the user name
    * Ret: Cursor object with the information
    * */
    public Cursor selectUserTitles(String userName){
        return db.rawQuery(
                "SELECT * from " + TABLE_NAME + " where " +  TT_USER_NAME + "=\"" + userName + "\";"
                , null);
    }



    /*
     * Desc: Upgrade one title in the database
     * Param: the title id and the user name
     * Ret: Long with the amount of elements affected
    * */
    public long upgradetitle( String nameId, int titleObtained ,String userName ){

        int id=0;

        //Get the title
        Cursor resultQuery= selecttitle(nameId,userName);

        //If the title exists
        if(resultQuery.moveToFirst()==true) {

            //Get the id to make the update
            id = resultQuery.getInt(resultQuery.getColumnIndex(DBTitlesManager.TT_ID));
            //Update it
            return db.update(
                    TABLE_NAME,
                    generateCVtitle(nameId, titleObtained, userName)
                    , TT_NAME_ID + " LIKE ? ",new String[]{nameId});
        }
        else return -1;
    }

    /*
     * Desc: Upgrade one title in the database
     * Param: the title id and the user name and if the title is obtained
     * Ret: Long with the amount of elements affected
     * */
    public long upgradeTitleObtained(String nameId, int titleObtained ,String nameUser ){
        long retValue=-1;
        Cursor resultQuery= selecttitle(nameId,nameUser);

        //If the title exists
        if(resultQuery.moveToFirst()) {
            retValue=upgradetitle(
                    resultQuery.getString(resultQuery.getColumnIndex(DBTitlesManager.TT_NAME_ID)),
                    titleObtained,
                    resultQuery.getString(resultQuery.getColumnIndex(DBTitlesManager.TT_USER_NAME))

            );
        }
        return retValue;


    }

    /*
    * Desc: Check if the title exist on the database
    * Param: the title id and the user name
    * Ret: true if title exist, false  if not
    * */
    public boolean titleExist(String titleId, String userName){
        //Execute select and return if there is an element
        return selecttitle(titleId,userName).moveToFirst();
    }




    }

