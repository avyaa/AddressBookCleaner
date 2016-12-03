package com.avyaa.addressbookcleaner;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void scan(){
        // Do something in response to button click
        Log.v(TAG,"Starting the Address book scan process now");



        Cursor cur = getContactsWithoutPhoneNumbers();

        /*while (cur.moveToNext()){

            for (String thisColumnName :cur.getColumnNames()) {
                Log.v(TAG,thisColumnName + " = "+cur.getString(cur.getColumnIndex(thisColumnName)));
            }

        }*/

        Log.d(TAG,"Cursor returned with number of rows = "+cur.getCount());

        ListView lv = (ListView) findViewById(R.id.list);


        String[] fields = new String[] {ContactsContract.Data.DISPLAY_NAME};

        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this,
                        R.layout.contacts_list_item,
                        cur,
                        fields,
                        new int[] {R.id.text1});

        Log.d(TAG,"Adapter returned with number of rows = "+ adapter.getCount());


        lv.setAdapter(adapter);
    }


    /** Called when the user clicks the Send button */
    public void startScan(View view) {
        scan();

    }


    private Cursor getContactsWithoutPhoneNumbers() {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;

        String[] projection =null;
        String selection = "has_phone_number = ?";
        String[] selectionArgs = new String[] {"0"};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }

}
