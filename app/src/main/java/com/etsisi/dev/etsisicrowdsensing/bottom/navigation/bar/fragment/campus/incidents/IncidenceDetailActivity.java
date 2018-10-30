package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidents;

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

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;

import org.w3c.dom.Text;

public class IncidenceDetailActivity extends AppCompatActivity {

    private static final String TAG = "IncidenceDetailActivity";

    private final int INCIDENCE_SENT = 0;
    private final int INCIDENCE_CHECKED = 1;

    private String DELETE_INCIDENCE_EXTRA = "delete_incidence";

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

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        bubbleImageView = findViewById(R.id.bubbleImageView);
        category = (TextView) findViewById(R.id.category);
        date = (TextView) findViewById(R.id.date);
        problemRoot = findViewById(R.id.problem_root);
        location = findViewById(R.id.location);
        stateImage = findViewById(R.id.state_image);

        locationPrompt = findViewById(R.id.location_prompt);
        problemPrompt = findViewById(R.id.problem_prompt);
        stateMessage = findViewById(R.id.state_message);


        Bundle data = getIntent().getExtras();

        /**
         * Get incidence
         */
        incidence = (Incidence) data.getParcelable("incidence");

        category.setText(incidence.getCategory());
        problemRoot.setText(incidence.getProblemRoot());

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
            bubbleImageView.setImageResource(MATERIAL_ICON);
            problemPrompt.setText(R.string.incidence_detail_damaged_run_out_prompt);
        } else if (category.equals(ATMOSPHERE_STRING)) {
            bubbleImageView.setImageResource(ATMOSPHERE_ICON);
            problemPrompt.setText(R.string.incidence_detail_problem_prompt);
        } else if (category.equals(FACILITIES_STRING)) {
            bubbleImageView.setImageResource(FACILITIES_ICON);
            problemPrompt.setText(R.string.incidence_detail_damaged_prompt);
        } else
            Log.e(TAG, "Found category doesn't match any type.");

        /**
         * Check incidence state
         */
        if (incidence.getState() == INCIDENCE_SENT)
            stateMessage.setText(R.string.incidence_under_revision);
        else if (incidence.getState() == INCIDENCE_CHECKED)
            stateMessage.setText(R.string.incidence_checked);


        Button deletebtn = findViewById(R.id.delete_button);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog confirmationDialog = deletionConfirmationDialog();
                confirmationDialog.show();
                confirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));
            }
        });
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
        builder.setMessage("¿Estás seguro de que deseas eliminar la incidencia?");
        // Add the buttons
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = getIntent();
                i.putExtra(DELETE_INCIDENCE_EXTRA, incidence);
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
