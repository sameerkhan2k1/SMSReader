package com.sameer.smsreader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.format.DateUtils;
import android.text.format.Time;

import java.text.SimpleDateFormat;

/**
 * Created by Sameer on 04/10/15.
 */
public class Utility {
//    public static final String DATE_FORMAT = "yyyyMMdd";
//    public static final long DAY_IN_MILLIS = 24 * 60 * 60 * 1000;

    public static String getContactName(Context context, String number) {
        String name = number;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));

        Cursor cursor = context.getContentResolver().query(uri,
                new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst())
                name = cursor.getString(0);

            cursor.close();
        }

        return name;
    }

    public static String getFormattedMonthDay(String dateInMillis) {
        long smsTime = Long.parseLong(dateInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat();

        if (DateUtils.isToday(smsTime)) {
            dateFormat = new SimpleDateFormat("HH:mm");
        }
        else {
            dateFormat = new SimpleDateFormat("MMM dd");
        }

        return dateFormat.format(smsTime);
    }
}
