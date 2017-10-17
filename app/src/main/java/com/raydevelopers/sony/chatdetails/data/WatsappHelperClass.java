package com.raydevelopers.sony.chatdetails.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SONY on 16-10-2017.
 */

public class WatsappHelperClass extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "users.db";
    public static final int DATABASE_VERSION = 1;

    public WatsappHelperClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_NEWS_TABLE =
                "CREATE TABLE " +
                        WatsappContract.UserEnrty.TABLE_NAME +
                        " ( " +WatsappContract.UserEnrty._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + WatsappContract.UserEnrty.COULMN_UNAME + " TEXT NOT NULL,"+
                        WatsappContract.UserEnrty.COULMN_UCONTACT + " TEXT NOT NULL,"+
                        WatsappContract.UserEnrty.COULMN_TIME+" TEXT NOT NULL,"+
                        WatsappContract.UserEnrty.COULMN_TEXT+" TEXT NOT NULL" +" )";
        db.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WatsappContract.UserEnrty.TABLE_NAME);
        onCreate(db);
    }
}
