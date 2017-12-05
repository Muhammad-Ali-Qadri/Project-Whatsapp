package com.example.muhammadaliqadri.whatsapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Muhammad Ali Qadri on 02/12/2017.
 */

public class UserOpenHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "User.db";
    public static final String USER_PROFILE_TABLE = "UserProfiles";
    public static final String USERS_TABLE = "Users";
    public static final String CHAT_TABLE = "Chat";


    public UserOpenHelper(Context c){
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + USER_PROFILE_TABLE + "(UID TEXT PRIMARY KEY, " +
                "USERNAME TEXT, PHONENUMBER TEXT, STATUS TEXT, PROFILEIMAGE BLOB)");

        sqLiteDatabase.execSQL("create table " + USERS_TABLE + "(UID TEXT PRIMARY KEY, " +
                "USERNAME TEXT, PHONENUMBER TEXT, STATUS TEXT, PROFILEIMAGE BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //on upgrade drop older tables
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_PROFILE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
