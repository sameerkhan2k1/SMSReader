package com.sameer.smsreader;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.sameer.smsreader.data.SMSContract;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private CursorAdapter mAdapter;
    private ListView mListView;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
//                if (mCursor.moveToFirst() && mCursor.moveToPosition(position)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(cursor.getString(
                            cursor.getColumnIndex(SMSContract.SMS_BODY)));
                    builder.setTitle(Utility.getContactName(
                            getApplicationContext(),
                            cursor.getString(cursor.getColumnIndex(SMSContract.SMS_ADDRESS))));

//                    builder.setMessage(mCursor.getString(mCursor.getColumnIndex(SMSContract.SMS_BODY)));
//                    builder.setTitle(Utility.getContactName(getApplicationContext(),
//                            mCursor.getString(mCursor.getColumnIndex(SMSContract.SMS_ADDRESS))));

                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCursor = getContentResolver().query(SMSContract.CONTENT_URI,
                new String[] {"_id", "address", "body", "date"},
                null, null, null);

        insertSMSFromProviderToDB();

        getSupportLoaderManager().initLoader(0, null, this);
        mAdapter = new SMSAdapter(this, mCursor, 0);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, SMSContract.CONTENT_URI,
                new String[] {"_id", "address", "body", "date"},
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void insertSMSFromProviderToDB() {
        if (mCursor.getCount() == 0) {
            Cursor SMSCursor = getContentResolver().query(Telephony.Sms.CONTENT_URI,
                    new String[] {"_id", "address", "body", "date"},
                    null, null, null);

            int numOfSMS = SMSCursor.getCount();

            if (numOfSMS !=0) {
                SMSCursor.moveToFirst();
                ContentValues[] SMSArray = new ContentValues[numOfSMS];

                for (int i = 0; i < numOfSMS; i++) {
                    SMSArray[i] = new ContentValues();
                    int index = SMSCursor.getColumnIndex(Telephony.TextBasedSmsColumns.ADDRESS);
                    SMSArray[i].put(SMSContract.SMS_ADDRESS, SMSCursor.getString(index));

                    index = SMSCursor.getColumnIndex(Telephony.TextBasedSmsColumns.BODY);
                    SMSArray[i].put(SMSContract.SMS_BODY, SMSCursor.getString(index));

                    index = SMSCursor.getColumnIndex(Telephony.TextBasedSmsColumns.DATE);
                    SMSArray[i].put(SMSContract.SMS_DATE, SMSCursor.getString(index));

                    SMSCursor.moveToNext();
                }

                // bulkInsert our ContentValues array
                getContentResolver().bulkInsert(SMSContract.CONTENT_URI, SMSArray);
            }
        }
    }
}
