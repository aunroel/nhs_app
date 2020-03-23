package com.uk.ac.ucl.carefulai.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.uk.ac.ucl.carefulai.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class SearchContactActivity extends AppCompatActivity {

    private SearchView searchView;
    private ListView contactsList;
    private SharedPreferences careNetworkPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_search_layout);

        contactsList = findViewById(R.id.contact_list);

        careNetworkPreferences = this.getSharedPreferences("careNetwork", 0);

        intent = getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(onQueryTextListener);

        return super.onCreateOptionsMenu(menu);
    }

    private SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    Cursor contacts = getListOfContactNames(query);
//                    String[] cursorColumns = {
//                            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY };
//                    int[] viewIds = {R.id.tv_name};
//
//                    SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
//                            SearchContactActivity.this,
//                            R.layout.contact_item_layout,
//                            contacts,
//                            cursorColumns,
//                            viewIds,
//                            0);
//
//                    contactsList.setAdapter(simpleCursorAdapter);
//                    contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> adapterView,
//                                                View view, int i, long l) {
//                            //get contact details and display
//                            TextView nameField = view.findViewById(R.id.tv_name);
//                            TextView phoneField = view.findViewById(R.id.tv_phone);
//                            submitContact(careNetworkPreferences, nameField.getText().toString(), phoneField.getText().toString());
//                        }
//                    });
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Cursor contacts = getListOfContactNames(newText);
                    ContactsAdapter cursorAdapter = new ContactsAdapter
                            (SearchContactActivity.this, contacts, searchView, intent);
                    searchView.setSuggestionsAdapter(cursorAdapter);
                    return true;
                }
            };

    public Cursor getListOfContactNames(String searchText) {

        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        ContactsContract.CommonDataKinds.Identity._ID,
                        ContactsContract.CommonDataKinds.Identity.LOOKUP_KEY,
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY,
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                };

        Uri uri = ContactsContract.CommonDataKinds.Contactables.CONTENT_URI;

        String selection = ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME_PRIMARY + " LIKE ?";
        String[] selectionArgs = new String[]{"%"+searchText+"%"};

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        return cur;
    }


}


