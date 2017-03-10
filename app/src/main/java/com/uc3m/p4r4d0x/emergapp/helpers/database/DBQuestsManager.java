package com.uc3m.p4r4d0x.emergapp.helpers.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Alvaro Loarte Rodríguez on 23/07/16.
 *
 */

public class DBQuestsManager {

    //Table Name
    public static final String TABLE_NAME="Quests";
    //Fields Name
    public static final String TQ_ID = "_id";
    public static final String TQ_NAME_ID= "nameid";
    public static final String TQ_NAME= "name";
    public static final String TQ_TYPE= "type";
    public static final String TQ_DESCRIPTION="description";
    public static final String TQ_INTERNAL_CITY="intcity";
    public static final String TQ_INTERNAL_STREET="intstreet";
    public static final String TQ_LEVEL="level";
    public static final String TQ_AP_REWARD="apreward";
    public static final String TQ_XP_REWARD="xpreward";






    //Sql sentence for building the table Users
    public static final String CREATE_TABLE = "create table "+TABLE_NAME+" ("
            + TQ_ID             + " integer primary key autoincrement,"
            + TQ_NAME_ID              + " text not null,"
            + TQ_NAME                 + " text not null,"
            + TQ_TYPE                 + " text not null, "
            + TQ_DESCRIPTION          + " text not null,"
            + TQ_INTERNAL_CITY        + " text not null,"
            + TQ_INTERNAL_STREET        + " text not null,"
            + TQ_LEVEL                + " text not null,"
            + TQ_AP_REWARD            + " integer not null,"
            + TQ_XP_REWARD            + " integer not null);";


    //DBHelper & SQLIteDatabase objects
    private DBHelper helper;
    private SQLiteDatabase db;

    //Constructor
    public DBQuestsManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    /*
    * Desc: Fills an ContentValue object with data
    * Param: quest data
    * Ret: A filled ContentValues object
    * */
    public ContentValues generateCVUser( String nameId, String name,String type, String description,
                                         String intCity,String intStreet,String level,
                                         int ap_points,int xp_points){

        ContentValues contentV = new ContentValues();
        contentV.put(TQ_NAME_ID,nameId);
        contentV.put(TQ_NAME,name);
        contentV.put(TQ_TYPE,type);
        contentV.put(TQ_DESCRIPTION,description);
        contentV.put(TQ_INTERNAL_CITY,intCity);
        contentV.put(TQ_INTERNAL_STREET,intStreet);
        contentV.put(TQ_LEVEL,level);
        contentV.put(TQ_AP_REWARD,ap_points);
        contentV.put(TQ_XP_REWARD,xp_points);
        return contentV;
    }

    /*
    * Desc: Insert on database a new quest
    * Param: quest data
    * */
    public boolean insertQuest(String nameId, String name, String type,String description,
                               String intCity,String intStreet,String level,
                              int ap_points,int xp_points){

        long retValue=0;
        retValue=db.insert(TABLE_NAME, null, generateCVUser(nameId, name,type, description, intCity,intStreet,
                                                            level,  ap_points,xp_points));
        if(retValue==-1 || retValue==0){
            return false;
        }
        else{
            return true;
        }

    }

    /*
    * Desc: Select from the database a quest
    * Param: quest id
    * Ret: Cursor object with the information
    * */
    public Cursor selectQuest(String questId,String level){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TQ_TYPE  + "=\"" + questId + "\""
                                                         + " and "   + TQ_LEVEL + "=\"" + level   + "\""
                                                         + "ORDER BY RANDOM() LIMIT 1;", null);
    }

    /*
    * Desc: Select from the database a quest
    * Param: quest id
    * Ret: Cursor object with the information
    * */
    public Cursor selectSingleQuest(String questId){
        return db.rawQuery("SELECT * from " + TABLE_NAME + " where " + TQ_TYPE  + "=\"" + questId + "\";", null);
    }



    /*
    * Desc: Check if the quest exist on the database
    * Param: the quest id
    * Ret: true if quest exist, false  if not
    * */
    public boolean questExist(String questId,String level){
        //Execute select and return it if there is an element
        return selectQuest(questId,level).moveToFirst();
    }




    }

