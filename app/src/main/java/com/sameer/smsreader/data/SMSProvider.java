package com.sameer.smsreader.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Sameer on 04/10/15.
 */
public class SMSProvider extends ContentProvider {
    private static final String TAG = SMSProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SMSDBHelper mOpenHelper;

    // Codes for the UriMatcher //////
    private static final int SMS = 1;
    private static final int SMS_WITH_ID = 2;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SMSContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, SMSContract.TABLE_SMS, SMS);
        matcher.addURI(authority, SMSContract.TABLE_SMS + "/#", SMS_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SMSDBHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        switch (sUriMatcher.match(uri)) {
            case SMS: {
                return mOpenHelper.getReadableDatabase().query(
                        SMSContract.TABLE_SMS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
            }

            case SMS_WITH_ID: {
                return mOpenHelper.getReadableDatabase().query(
                        SMSContract.TABLE_SMS,
                        projection,
                        SMSContract._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
            }

            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SMS: {
                return SMSContract.CONTENT_DIR_TYPE;
            }
            case SMS_WITH_ID: {
                return SMSContract.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case SMS: {
                long _id = db.insert(SMSContract.TABLE_SMS, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = SMSContract.buildSMSUri(_id);
                }
                else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case SMS: {
                db.beginTransaction();
                int numInserted = 0;

                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }

                        long _id = -1;

                        try {
                            _id = db.insertOrThrow(SMSContract.TABLE_SMS, null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.e(TAG, "Attempting to insert " +
                                    value.getAsString(SMSContract._ID) +
                                    " but value is already in database.");
                        }

                        if (_id != -1) {
                            numInserted++;
                        }
                    }

                    if (numInserted > 0) {
                        db.setTransactionSuccessful();
                    }
                } finally {
                    db.endTransaction();
                }

                if (numInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return numInserted;
            }

            default: {
                return super.bulkInsert(uri, values);
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numDeleted;

        switch (sUriMatcher.match(uri)) {
            case SMS: {
                numDeleted = db.delete(SMSContract.TABLE_SMS, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + SMSContract.TABLE_SMS + "'");
                break;
            }

            case SMS_WITH_ID: {
                numDeleted = db.delete(SMSContract.TABLE_SMS, SMSContract._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" + SMSContract.TABLE_SMS + "'");
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (values == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch  (sUriMatcher.match(uri)) {
            case SMS: {
                numUpdated = db.update(SMSContract.TABLE_SMS, values, selection, selectionArgs);
                break;
            }

            case SMS_WITH_ID: {
                numUpdated = db.update(SMSContract.TABLE_SMS, values,
                        SMSContract._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }

            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }
}
