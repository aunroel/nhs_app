package com.uk.ac.ucl.carefulai.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;


public class SearchContactActivity extends AppCompatActivity {

    private SearchView searchView; //the SearchView that holds the adapter that allows the user to search for contacts
    private Intent intent; //intent used to create the activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_search_layout); //sets the layout


        intent = getIntent(); //fetches the intent that searches the activity
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu); //dropdown menu for the contacts

        searchView = (SearchView) menu.findItem(R.id.search).getActionView(); //gets the search button
        searchView.setSubmitButtonEnabled(false); //enables search button
        searchView.setOnQueryTextListener(onQueryTextListener); //sets the suggestion adapter defined below to the search view

        return super.onCreateOptionsMenu(menu);
    }

    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) { //needed for any onQueryTextListener, no submit button so just return
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) { //suggestions as the user types
                    Cursor contacts = getListOfContactNames(newText); //cursor that iterates through native contacts, defined below
                    //ContactsAdaptor defined in separate class, renders each option and does the onClick, need intent to get contactId and isSetupCN
                    ContactsAdapter cursorAdapter = new ContactsAdapter
                            (SearchContactActivity.this, contacts, searchView, intent);
                    searchView.setSuggestionsAdapter(cursorAdapter); //set the adaptor to the search view
                    return true;
                }
            };

    public Cursor getListOfContactNames(String searchText) {

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        //list of fields required for each contact, ID and LOOKUP_KEY required to internally iterate through the native list
        // the display name and number given to front end

        String[] mProjection =
                {
                        ContactsContract.CommonDataKinds.Identity._ID,
                        ContactsContract.CommonDataKinds.Identity.LOOKUP_KEY,
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        Uri uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI; //URI defining data types required for the cursor

        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY + " LIKE ?"; //select by name
        String[] selectionArgs = new String[]{"%"+searchText+"%"}; //use the user's text to search by name

        cur = cr.query(uri, mProjection, selection, selectionArgs, null); //query the native contacts

        return cur;
    }

}


