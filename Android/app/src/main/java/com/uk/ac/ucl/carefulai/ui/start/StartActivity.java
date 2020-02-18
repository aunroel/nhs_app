package com.uk.ac.ucl.carefulai.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.uk.ac.ucl.carefulai.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setTitle("Welcome to Careful AI!");
    }

    public void permissionsscreen(View view) {
        startActivity(new Intent(this, PermissionsActivity.class));
    }

}