package com.sameer.smsreader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * Created by sameer on 14/09/15.
 */
public class IncomingSMS extends BroadcastReceiver {
    final static String TAG = IncomingSMS.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String msgBody = smsMessage.getDisplayMessageBody();
                String phNumber = smsMessage.getDisplayOriginatingAddress();
                Toast.makeText(context, phNumber + "\n" + msgBody, Toast.LENGTH_LONG).show();
            }
        }
    }
}