package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewIncidenceActivity extends AppCompatActivity implements View.OnClickListener{

    private int MAX_LENGTH_BLOQUE = 2;
    private int MAX_LENTH_AULA = 4;

    private String[] materialObjectsArray;
    private String[] atmosphereObjectsArray;
    private String[] facilitiesObjectsArray;

    private TextView causePrompt;
    private EditText locationNumberEditText;
    private RadioGroup locationGroup;
    private Spinner problemRootSpinner;
    private ArrayAdapter<String> problemRootAdapter;
    private RadioGroup categoryGroup;

    private String selectedCategory;
    private String selectedProblemRoot;
    private int inputLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_incidence);

        View parentView = findViewById(R.id.parentView);
        // Hide Keyboard when empty selection
        setupUI(parentView);

        materialObjectsArray = getResources().getStringArray(R.array.material_objects_array);
        atmosphereObjectsArray = getResources().getStringArray(R.array.atmosphere_objects_array);
        facilitiesObjectsArray = getResources().getStringArray(R.array.facilities_objects_array);

        causePrompt = findViewById(R.id.cause_question);
        locationNumberEditText = findViewById(R.id.numberTextView);

        categoryGroup = findViewById(R.id.categoryGroup);

        locationGroup = findViewById(R.id.locationRadioGroup);
        locationGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                locationNumberEditText.setText(null);
                switch(i){
                    case R.id.aulaButton:
                        locationNumberEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENTH_AULA)});
                        break;
                    case R.id.bloqueButton:
                        locationNumberEditText.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH_BLOQUE)});
                        break;
                    default:
                        finish();
                }

            }
        });

        // Set content to selected radio button option
        causePrompt.setText(R.string.new_incidence_damaged_run_out_question);

        problemRootSpinner = (Spinner) findViewById(R.id.objectSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        // Set content to selected radio button option
        problemRootAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materialObjectsArray);
        // Specify the layout to use when the list of choices appears
        problemRootAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        problemRootSpinner.setAdapter(problemRootAdapter);

        problemRootSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(NewIncidenceActivity.this, "Selected is -> " + adapterView.getItemAtPosition(i).toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Button materialBtn = findViewById(R.id.materialRadioBtn);
        Button atmosphereBtn = findViewById(R.id.atmosphereRadioBtn);
        Button facilitiesBtn = findViewById(R.id.facilitiesRadioBtn);

        materialBtn.setOnClickListener(this);
        atmosphereBtn.setOnClickListener(this);
        facilitiesBtn.setOnClickListener(this);





        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(this);

        Button addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        locationNumberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addBtn.setEnabled(editable.toString().length() > 0);
            }
        });


    }

    @Override
    public void onClick(View v) {
        ArrayAdapter<String> newAdapter;
        switch(v.getId()){
            case R.id.materialRadioBtn:
                causePrompt.setText(R.string.new_incidence_damaged_run_out_question);
                newAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, materialObjectsArray);
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                problemRootSpinner.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
                break;
            case R.id.atmosphereRadioBtn:
                causePrompt.setText(R.string.new_incidence_problem_question);
                newAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, atmosphereObjectsArray);
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                problemRootSpinner.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
                break;
            case R.id.facilitiesRadioBtn:
                causePrompt.setText(R.string.new_incidence_damaged_question);
                newAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, facilitiesObjectsArray);
                newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                problemRootSpinner.setAdapter(newAdapter);
                newAdapter.notifyDataSetChanged();
                break;
            case R.id.cancelBtn:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.addBtn:
                int checkedCategoryButtonId = categoryGroup.getCheckedRadioButtonId();
                RadioButton categoryButton = findViewById(checkedCategoryButtonId);
                selectedCategory = categoryButton.getText().toString();
                selectedProblemRoot = problemRootSpinner.getSelectedItem().toString();
                inputLocation = Integer.parseInt(locationNumberEditText.getText().toString());
                Date todayDate = Calendar.getInstance().getTime();

                // USER IDENTIFICATOR
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                String idDate = new SimpleDateFormat("ddMMyyyy").format(todayDate);
                String incidenceId = mAuth.getCurrentUser().getUid() + selectedCategory + selectedProblemRoot + inputLocation + idDate;
                incidenceId = incidenceId.toLowerCase();
                incidenceId = incidenceId.replaceAll("\\s", "");
                incidenceId = incidenceId.replaceAll("á", "a");
                incidenceId = incidenceId.replaceAll("é", "e");
                incidenceId = incidenceId.replaceAll("í", "i");
                incidenceId = incidenceId.replaceAll("ó", "o");
                incidenceId = incidenceId.replaceAll("ú", "u");

                Incidence newIncidence = new Incidence(incidenceId, mAuth.getCurrentUser().getUid(), selectedCategory, selectedProblemRoot, inputLocation, todayDate);
                //Toast.makeText(NewIncidenceActivity.this, "New incidence values are -> " + selectedCategory + " , " + selectedProblemRoot + " , " + inputLocation + " , " + todayDate.toString(),Toast.LENGTH_SHORT).show();

                // Send the new event back to parent activity
                Intent i = getIntent();
                i.putExtra(CampusFragment.NEW_INCIDENCE_EXTRA, newIncidence);
                setResult(RESULT_OK, i);
                finish();
        }

    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
