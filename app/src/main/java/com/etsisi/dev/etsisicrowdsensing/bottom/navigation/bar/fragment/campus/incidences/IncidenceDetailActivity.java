package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

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

        Bundle data = getIntent().getExtras();

        Incidence incidence = (Incidence) data.getParcelable("incidence");

        TextView incidenceTitle = findViewById(R.id.incidenceTitle);
        incidenceTitle.setText(incidence.getText());
    }
}
