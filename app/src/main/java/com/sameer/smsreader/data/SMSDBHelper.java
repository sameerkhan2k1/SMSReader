package com.sameer.smsreader.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sameer on 04/10/15.
 */
public class SMSDBHelper extends SQLiteOpenHelper {
    private static final String TAG = SMSDBHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "private_sms.db";
    private static final int DATABASE_VERSION = 1;

    public SMSDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SMSContract.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SMSContract.TABLE_SMS);
        onCreate(db);
    }
}
