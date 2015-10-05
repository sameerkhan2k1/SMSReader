package com.sameer.smsreader;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.sameer.smsreader.data.SMSContract;

/**
 * Created by sameer on 14/09/15.
 */
public class IncomingSMS extends BroadcastReceiver {
    final static String TAG = IncomingSMS.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String msgBody = smsMessage.getMessageBody();
                String phNumber = smsMessage.getOriginatingAddress();
                String date = String.valueOf(smsMessage.getTimestampMillis());
                Toast.makeText(context, phNumber + "\n" + msgBody, Toast.LENGTH_LONG).show();

                ContentValues values = new ContentValues();
                values.put(SMSContract.SMS_ADDRESS, phNumber);
                values.put(SMSContract.SMS_BODY, msgBody);
                values.put(SMSContract.SMS_DATE, date);
                context.getContentResolver().insert(SMSContract.CONTENT_URI, values);
            }
        }
    }
}