package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.etsisi.dev.etsisicrowdsensing.R;

public class IncidenceDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidence_detail);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
