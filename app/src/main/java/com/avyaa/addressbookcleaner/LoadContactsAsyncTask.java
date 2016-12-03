package com.avyaa.addressbookcleaner;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by santhoshkuppusamy on 12/3/16.
 */

public class LoadContactsAsyncTask extends AsyncTask<Void, Void, List<String>> {

    private static final String TAG = "LoadContactsAsyncTask";

    private ProgressDialog pd;

    private final Context context;

    private final AsyncResponse delegate;


    public LoadContactsAsyncTask(Context context,AsyncResponse delegate){
        this.context=context;
        this.delegate=delegate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = ProgressDialog.show(context, "Finding and Deleting Contacts",
                "Please Wait...");
    }


    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> deletedContacts = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection =null;
        String selection = "has_phone_number = ?";
        String[] selectionArgs = new String[] {"0"};
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME +
                " COLLATE LOCALIZED ASC";
        Cursor cur =  cr.query(uri, projection, selection, selectionArgs, sortOrder);
        Log.d(TAG,"Cursor returned with number of rows = "+cur.getCount());
        while (cur.moveToNext()){
            String contactName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            if (contactName==null) {
                contactName="";
            }
            deletedContacts.add(contactName);
            performDelete(cr,cur);

        }
        return deletedContacts;

    }

    private void performDelete(ContentResolver cr,Cursor cur){
        try{
            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
            Log.v(TAG,"Delete URI is " + uri.toString());
            cr.delete(uri, null, null);
        }catch(Exception e) {
            Log.e(TAG,"Error while deleting contact",e);
        }
    }

    @Override
    protected void onPostExecute(List<String> contacts) {
       super.onPostExecute(contacts);
        pd.cancel();
        delegate.processFinish(contacts);
    }
}
