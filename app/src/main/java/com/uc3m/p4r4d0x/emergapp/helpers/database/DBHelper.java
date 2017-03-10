package com.uc3m.p4r4d0x.emergapp.helpers.database;

/**
 *
 * Created by Alvaro Loarte Rodríguez on 19/02/16.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

        private static final String DB_NAME ="eReporterDDBB.sqlite";
        private static final int DB_SCHEME_VERSION =86;
        public Context cntx;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_SCHEME_VERSION);
            cntx=context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DBUserManager.CREATE_TABLE);
            db.execSQL(DBTitlesManager.CREATE_TABLE);
            db.execSQL(DBAchievementsManager.CREATE_TABLE);
            db.execSQL(DBAvatarsManager.CREATE_TABLE);
            db.execSQL(DBQuestsManager.CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS users; ");
            db.execSQL("DROP TABLE IF EXISTS titles; ");
            db.execSQL("DROP TABLE IF EXISTS achievements; ");
            db.execSQL("DROP TABLE IF EXISTS avatars; ");
            db.execSQL("DROP TABLE IF EXISTS quests; ");
            db.execSQL(DBUserManager.CREATE_TABLE);
            db.execSQL(DBTitlesManager.CREATE_TABLE);
            db.execSQL(DBAchievementsManager.CREATE_TABLE);
            db.execSQL(DBAvatarsManager.CREATE_TABLE);
            db.execSQL(DBQuestsManager.CREATE_TABLE);


        }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if(!db.isReadOnly()){
            //Enable foreign keys
            db.execSQL("PRAGMA foreign_keys=ON;");

        }
    }


}





