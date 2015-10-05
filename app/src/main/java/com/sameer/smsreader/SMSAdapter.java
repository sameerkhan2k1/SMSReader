package com.sameer.smsreader;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

/**
 * Created by Sameer on 04/10/15.
 */
public class SMSAdapter extends CursorAdapter {
    private static final String TAG = SMSAdapter.class.getSimpleName();

    private Context mContext;

    public SMSAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int index = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS);
        String string = cursor.getString(index);
        viewHolder.numberTextView.setText(Utility.getContactName(context, string));

        index = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
        string = cursor.getString(index);
        viewHolder.bodyTextView.setText(string);

        index = cursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
        string = cursor.getString(index);
        viewHolder.dateTextView.setText(Utility.getFormattedMonthDay(string));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    public static class ViewHolder {
        public final TextView numberTextView;
        public final TextView bodyTextView;
        public final TextView dateTextView;

        public ViewHolder(View view){
            numberTextView = (TextView) view.findViewById(R.id.number);
            bodyTextView = (TextView) view.findViewById(R.id.body);
            dateTextView = (TextView) view.findViewById(R.id.date);
        }
    }
}
