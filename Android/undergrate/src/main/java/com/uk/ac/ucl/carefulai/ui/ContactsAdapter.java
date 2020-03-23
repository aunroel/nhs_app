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
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;
    private String nameKey;
    private String phoneKey;
    private SharedPreferences careNetworkPreferences;
    private boolean isSetup;

    ContactsAdapter(Context context, Cursor cursor, SearchView sv, Intent intent) {
        super(context, cursor, false);
        mContext = context;
        searchView = sv;
        mLayoutInflater = LayoutInflater.from(context);
        int contactId = intent.getIntExtra("contactId", 0);
        isSetup = intent.getBooleanExtra("isSetupCN", false);
        nameKey = "nameKey" + contactId;
        phoneKey = "phoneKey" + contactId;
        careNetworkPreferences = mContext.getSharedPreferences("careNetwork", 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = mLayoutInflater.inflate(R.layout.contact_item_layout, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        String name = cursor.getString(cursor.getColumnIndex(
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY));
        String phone = cursor.getString(cursor.getColumnIndex(
                ContactsContract.CommonDataKinds.Phone.NUMBER));

        TextView nameTv =  view.findViewById(R.id.tv_name);
        nameTv.setText(name);

        TextView phoneNo = view.findViewById(R.id.tv_phone);
        phoneNo.setText(phone);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView pName = (TextView) view.findViewById(R.id.tv_name);
                TextView pNumber = (TextView) view.findViewById(R.id.tv_phone);
                searchView.setIconified(true);
                //get contact details and display
                Toast.makeText(context, "Selected Contact "+pName.getText(),
                        Toast.LENGTH_LONG).show();
                submitContact(careNetworkPreferences, pName.getText().toString(), pNumber.getText().toString());

            }
        });
    }

    private void submitContact(SharedPreferences careNetworkPreferences, String name, String phone) {
        SharedPreferences.Editor editor = careNetworkPreferences.edit();

        editor.putString(nameKey, name);
        editor.putString(phoneKey, phone);
        editor.commit();

        if (isSetup) {
            Intent intent = new Intent(mContext, PrimaryCareNetworkActivity.class);
            mContext.startActivity(intent);
        }
        else {
            Intent intent = new Intent(mContext, AppActivity.class);
            intent.putExtra("contactEdit", true);
            mContext.startActivity(intent);
        }

    }
}
