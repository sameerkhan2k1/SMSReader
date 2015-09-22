package com.sameer.smsreader;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import static android.R.id.list;
import static com.sameer.smsreader.R.id.body;
import static com.sameer.smsreader.R.id.date;
import static com.sameer.smsreader.R.id.number;
import static com.sameer.smsreader.R.layout.activity_main;
import static com.sameer.smsreader.R.layout.item_row;

public class MainActivity extends AppCompatActivity {
    private SimpleCursorAdapter mAdapter;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);

        Cursor cursor = getContentResolver().query(Telephony.Sms.CONTENT_URI,
                new String[] {"_id", "address", "body", "date"},
                null, null, null);

        mAdapter = new SimpleCursorAdapter(this, item_row, cursor,
                new String[] {"address", "body", "date"},
                new int[] {number, body, date},
                0);

        mListView = (ListView) findViewById(list);
        mListView.setAdapter(mAdapter);
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
}
