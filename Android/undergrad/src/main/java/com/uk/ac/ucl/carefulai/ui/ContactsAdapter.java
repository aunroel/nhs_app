package com.uk.ac.ucl.carefulai.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.ac.ucl.carefulai.R;
import com.uk.ac.ucl.carefulai.ui.start.PrimaryCareNetworkActivity;


public class ContactsAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater; //get the menu inflater defined in SearchContactActivity
    private Context mContext; //context from SearchContactActivity
    private SearchView searchView; //search view from SearchContactActivity
    private String nameKey; //string used to set the chosen name in careNetworkPreferences
    private String phoneKey; //string used to set the chosen phone in careNetworkPreferences
    private SharedPreferences careNetworkPreferences; //careNetworkPreferences to commit changes to the Care Network
    private boolean isSetup; //if activity was launched from Care Network in main flow or from setup flow

    //Constructor to set the context, cursor containing the contacts, search view, and get the contactId and isSetupCN extras
    ContactsAdapter(Context context, Cursor cursor, SearchView sv, Intent intent) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
        int contactId = intent.getIntExtra("contactId", 0);
        isSetup = intent.getBooleanExtra("isSetupCN", false);
        nameKey = "nameKey" + contactId; //correct keys for the selected TextView in PrimaryCareNetworkActivity or ConfigFragment
        phoneKey = "phoneKey" + contactId;
        careNetworkPreferences = mContext.getSharedPreferences("careNetwork", 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) { //used to create a new view when a contact is searched
        View v = mLayoutInflater.inflate(R.layout.contact_item_layout, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) { //binds data to the view created

        String name = cursor.getString(cursor.getColumnIndex(
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)); //get the name field from the Cursor passed in from SearchContactActivity
        String phone = cursor.getString(cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER)); //get the phone field from the Cursor passed in from SearchContactActivity

        TextView nameTv =  view.findViewById(R.id.tv_name); //TextView in an individual menu item for the name
        nameTv.setText(name); //set the TextView value to the name

        TextView phoneNo = view.findViewById(R.id.tv_phone); //TextView in an individual menu item for the phone number
        phoneNo.setText(phone); //set the TextView value to the phone

        //on selection, get the name and phone and store in careNetworkPreferences
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pName = (TextView) view.findViewById(R.id.tv_name); //get the name TextView
                TextView pNumber = (TextView) view.findViewById(R.id.tv_phone); //get the phone TextView
                searchView.setIconified(true);
                //get contact details and display
                Toast.makeText(context, "Selected Contact "+pName.getText(),
                        Toast.LENGTH_LONG).show(); //show the user the contact they selected
                submitContact(careNetworkPreferences, pName.getText().toString(), pNumber.getText().toString()); //store in preferences

            }
        });
    }

    private void submitContact(SharedPreferences careNetworkPreferences, String name, String phone) { //store in preferences
        SharedPreferences.Editor editor = careNetworkPreferences.edit();

        editor.putString(nameKey, name); //use nameKey (which is equal to "nameKey" + contactId) to store the name in preferences
        editor.putString(phoneKey, phone); //use phoneKey (which is equal to "phoneKey" + contactId) to store the name in preferences
        editor.commit(); //save data

        if (isSetup) { //if activity from setup flow, go back to PrimaryCareNetworkActivity
            Intent intent = new Intent(mContext, PrimaryCareNetworkActivity.class);
            mContext.startActivity(intent);
        }
        else { //otherwise back to main flow (AppActivity), and set the "contactEdit" extra to go to the CareNetwork tab
            Intent intent = new Intent(mContext, AppActivity.class);
            intent.putExtra("contactEdit", true);
            mContext.startActivity(intent);
        }
    }
}
