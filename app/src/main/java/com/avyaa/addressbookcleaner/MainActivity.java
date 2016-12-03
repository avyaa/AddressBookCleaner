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

public class MainActivity extends AppCompatActivity implements AsyncResponse {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void scanAndDelete(){
        Log.v(TAG,"Starting the Address book scanAndDelete process now");
        LoadContactsAsyncTask task = new LoadContactsAsyncTask(this,this);
        task.execute();
   }


    /** Called when the user clicks the Scan button */
    public void startScan(View view) {
        scanAndDelete();
    }


    @Override
    public void processFinish(List<String> contacts) {
        ListView lv = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.contacts_list_item,R.id.text1,contacts);
        Log.d(TAG,"Adapter returned with number of rows = "+ adapter.getCount());
        TextView numOfRecords = (TextView) findViewById(R.id.record_num);
        if (numOfRecords!=null) {
            numOfRecords.setText(adapter.getCount()+"");
        }
        Log.v(TAG,"List item is "+lv);
        lv.setAdapter(adapter);
    }
}
