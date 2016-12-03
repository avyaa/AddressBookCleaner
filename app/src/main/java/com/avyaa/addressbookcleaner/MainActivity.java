package com.avyaa.addressbookcleaner;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void scanAndDelete(){
        // Do something in response to button click
        Log.v(TAG,"Starting the Address book scanAndDelete process now");
        Cursor cur = getContactsWithoutPhoneNumbers();
        Log.d(TAG,"Cursor returned with number of rows = "+cur.getCount());
        ListView lv = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.contacts_list_item,R.id.text1,deleteContacts(cur));
        Log.d(TAG,"Adapter returned with number of rows = "+ adapter.getCount());
        TextView numOfRecords = (TextView) findViewById(R.id.record_num);
        if (numOfRecords!=null) {
            numOfRecords.setText(adapter.getCount()+"");
        }
        Log.v(TAG,"List item is "+lv);
        lv.setAdapter(adapter);
    }


    /** Called when the user clicks the Send button */
    public void startScan(View view) {
        scanAndDelete();
    }


    private Cursor getContactsWithoutPhoneNumbers() {
        // Run query
        ContentResolver cr = getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection =null;
        String selection = "has_phone_number = ?";
        String[] selectionArgs = new String[] {"0"};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        return cr.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    private List<String> deleteContacts(Cursor cur){
        List<String> deletedContacts = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        while (cur.moveToNext()){
            String contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if (contactName==null) {
                contactName="";
            }
            deletedContacts.add(contactName);
            try{
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                Log.v(TAG,"Delete URI is " + uri.toString());
                cr.delete(uri, null, null);
            }
            catch(Exception e)
            {
                System.out.println(e.getStackTrace());
            }
        }
        return deletedContacts;
    }

}
