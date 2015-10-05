package com.sameer.smsreader.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;

/**
 * Created by Sameer on 04/10/15.
 */
public class SMSContract {
    private static final String TAG = SMSContract.class.getSimpleName();

    //Constants for identifying table and columns
    public static final String TABLE_SMS = "sms";
    public static final String _ID = "_id";
    public static final String SMS_ADDRESS = "address";
    public static final String SMS_BODY = "body";
    public static final String SMS_DATE = "date";

    //SQL to create table
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_SMS + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SMS_ADDRESS + " TEXT, " +
                    SMS_BODY + " TEXT, " +
                    SMS_DATE + " TEXT" + ")";

    public static final String CONTENT_AUTHORITY = SMSContract.class.getPackage().getName();
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // create content uri
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_SMS).build();

    // create cursor of base type directory for multiple entries
    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_SMS;

    // create cursor of base type item for single entry
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_SMS;

    // for building URIs on insertion
    public static Uri buildSMSUri(long id){
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
