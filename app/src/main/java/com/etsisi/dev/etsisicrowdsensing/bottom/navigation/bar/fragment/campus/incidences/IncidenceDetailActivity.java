package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;

import java.text.DateFormat;
import java.util.Locale;

public class IncidenceDetailActivity extends AppCompatActivity {

    private static final String TAG = "IncidenceDetailActivity";

    private final int INCIDENCE_SENT = 0;
    private final int INCIDENCE_CHECKED = 1;

    private final int MATERIAL_ICON = R.drawable.material_icon_hd;
    private final int ATMOSPHERE_ICON = R.drawable.atmosphere_icon_hd;
    private final int FACILITIES_ICON = R.drawable.facilities_icon_hd;

    private static String MATERIAL_STRING;
    private static String ATMOSPHERE_STRING;
    private static String FACILITIES_STRING;

    private Incidence incidence;

    ImageView bubbleImageView;
    TextView category;
    TextView problemPrompt;
    TextView date;
    TextView problemRoot;
    TextView locationPrompt;
    TextView location;
    ImageView stateImage;
    TextView stateMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidence_detail);

        MATERIAL_STRING = getString(R.string.incidence_material_category);
        ATMOSPHERE_STRING = getString(R.string.incidence_atmosphere_category);
        FACILITIES_STRING = getString(R.string.incidence_facilities_category);



        bubbleImageView = findViewById(R.id.imageView);
        loadLogoImage(R.drawable.bubble_warning, bubbleImageView);
        category = (TextView) findViewById(R.id.category);
        date = (TextView) findViewById(R.id.date);
        problemRoot = findViewById(R.id.reason_label);
        location = findViewById(R.id.location);
        stateImage = findViewById(R.id.state_image);
        loadLogoImage(R.drawable.incidence_tracing_sent, stateImage);

        locationPrompt = findViewById(R.id.location_prompt);
        problemPrompt = findViewById(R.id.problem_prompt);
        stateMessage = findViewById(R.id.state_message);

        Button okBtn = findViewById(R.id.okBtn);


        Bundle data = getIntent().getExtras();

        /**
         * Get incidence
         */
        incidence = (Incidence) data.getParcelable("incidence");


        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Show back arrow
        if(incidence.getState() == 0) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        else{
            okBtn.setVisibility(View.VISIBLE);
            okBtn.setOnClickListener(view -> {
                Intent i = getIntent();
                i.putExtra(CampusFragment.DELETE_INCIDENCE_ID_EXTRA, incidence.getId());
                setResult(RESULT_OK, i);
                finish();
            });
        }



        category.setText(incidence.getCategory());
        problemRoot.setText(incidence.getProblemRoot());
        // TODO
        Locale locale = getResources().getConfiguration().locale;
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String formattedDate = dateFormat.format(incidence.getDate());
        date.setText(formattedDate);

        /**
         * Check if location is a part of the building or a room
         */
        if (String.valueOf(incidence.getLocation()).length() > 2)
            locationPrompt.setText(R.string.bloque);
        else
            locationPrompt.setText(R.string.aula);

        location.setText(String.valueOf(incidence.getLocation()));


        /**
         * Check incidence category
         */
        String category = incidence.getCategory();

        if (category.equals(MATERIAL_STRING)) {
            //bubbleImageView.setImageResource(MATERIAL_ICON);
            loadLogoImage(MATERIAL_ICON, bubbleImageView);
            problemPrompt.setText(R.string.incidence_detail_damaged_run_out_prompt);

        } else if (category.equals(ATMOSPHERE_STRING)) {
            loadLogoImage(ATMOSPHERE_ICON, bubbleImageView);
            problemPrompt.setText(R.string.incidence_detail_problem_prompt);
        } else if (category.equals(FACILITIES_STRING)) {
            loadLogoImage(FACILITIES_ICON, bubbleImageView);
            problemPrompt.setText(R.string.incidence_detail_damaged_prompt);
        } else
            Log.e(TAG, "Found category doesn't match any type.");


        Button deletebtn = findViewById(R.id.delete_button);


        /**
         * Check incidence state
         */
        if (incidence.getState() == INCIDENCE_SENT) {
            stateMessage.setText(R.string.incidence_under_revision);
            loadLogoImage(R.drawable.incidence_tracing_sent, stateImage);
            //stateImage.setImageResource(R.drawable.incidence_tracing_sent);
        }
        else if (incidence.getState() == INCIDENCE_CHECKED) {
            //stateMessage.setText(R.string.incidence_checked);
            stateMessage.setText(R.string.incidence_checked);
            loadLogoImage(R.drawable.incidence_tracing_reviewed, stateImage);
            //stateImage.setImageResource(R.drawable.incidence_tracing_reviewed);
            deletebtn.setVisibility(View.INVISIBLE);
        }


        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog confirmationDialog = deletionConfirmationDialog();
                confirmationDialog.show();
                confirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });
    }

    public void loadLogoImage(int logoResourceId, ImageView imageViewContainer) {
        Glide
                .with(this)
                .load(logoResourceId)
                .into(imageViewContainer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AlertDialog deletionConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.incidence_deletion_confirmation);
        // Add the buttons
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = getIntent();
                i.putExtra(CampusFragment.DELETE_INCIDENCE_ID_EXTRA, incidence.getId());
                setResult(RESULT_OK, i);
                finish();
            }
        });
        builder.setNegativeButton(R.string.material_calendar_negative_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        return dialog;
    }


}
