package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.etsisi.dev.etsisicrowdsensing.DatePickerFragment;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.TimePickerFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Event;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewEventActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    private static final String TAG = "NewEventActivity";

    public static final String NEW_EVENT_EXTRA = "new_event";

    private int MAX_LENGTH_BLOQUE = 2;
    private int MAX_LENTH_AULA = 4;
    /**
     * Kind values
     */
    private final String ENTREGA_KIND = "ENTREGA";
    private final String EXAMEN_KIND = "EXAMEN";
    private final String PRESENTACION_KIND = "PRESENTACION";

    private TextView selectedDateLabelView;

    private TextView subjectView;
    private TextView descriptionView;
    private TextView locationNumberView;
    private RadioGroup eventKindRadioGroup;
    private RadioGroup locationRadioGroup;

    private Button addBtn;
    private Button cancelBtn;

    private DialogFragment fragment;;

    private TextView selectedTimeView;
    private TextView selectedDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        /**
         * Buttons
         */
        addBtn = findViewById(R.id.add_button);
        cancelBtn = findViewById(R.id.cancel_button);

        /**
         * Form
         */
        subjectView = findViewById(R.id.subjectTextView);
        descriptionView = findViewById(R.id.descriptionTextView);
        locationNumberView = findViewById(R.id.numberTextView);
        eventKindRadioGroup = findViewById(R.id.eventKindRadioGroup);
        locationRadioGroup = findViewById(R.id.locationRadioGroup);

        selectedDateLabelView = findViewById(R.id.selected_date);


        locationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });


        selectedTimeView = findViewById(R.id.selected_time);
        selectedDateView = findViewById(R.id.selected_date);


        ImageButton dateButton = findViewById(R.id.date_button);
        ImageButton timeButton = findViewById(R.id.time_button);

        dateButton.setOnClickListener(this);
        timeButton.setOnClickListener(this);


        addBtn.setOnClickListener(this);

        cancelBtn.setOnClickListener(this);

        formChangedListeners();

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        if(view.getId() == R.id.aulaButton) {
            locationNumberView.setVisibility(View.VISIBLE);
            locationNumberView.setText("");
            locationNumberView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENTH_AULA)});
        }
        else if (view.getId() == R.id.bloqueButton){
            locationNumberView.setVisibility(View.VISIBLE);
            locationNumberView.setText("");
            locationNumberView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(MAX_LENGTH_BLOQUE)});
        }
        else if(view.getId() == R.id.moodleButton)
            locationNumberView.setVisibility(View.INVISIBLE);

        addBtn.setEnabled(isFormFilled());
    }

    public boolean isFormFilled(){
        boolean result = true;
         if(selectedTimeView.getText().length() == 0)
             result = false;
         if(selectedDateView.getText().length() == 0)
             result = false;
         if(subjectView.getText().length() == 0)
             result = false;
         if(descriptionView.getText().length() == 0)
             result = false;
         if(locationNumberView.getVisibility() == View.VISIBLE)
             if(locationNumberView.getText().length() == 0)
                 result = false;

         return result;
    }

    public void formChangedListeners(){
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                addBtn.setEnabled(isFormFilled());
            }
        };

        selectedDateView.addTextChangedListener(tw);
        selectedTimeView.addTextChangedListener(tw);
        subjectView.addTextChangedListener(tw);
        descriptionView.addTextChangedListener(tw);
        locationNumberView.addTextChangedListener(tw);
    }

    public Event extractDataToEvent() {
        // Get input data from form

        String subject = subjectView.getText().toString();
        String description = descriptionView.getText().toString();

        String location;
        if(locationNumberView.getVisibility() == View.VISIBLE)
            location = locationNumberView.getText().toString();
        else{
            RadioButton locationKindButton = findViewById(locationRadioGroup.getCheckedRadioButtonId());
            location = locationKindButton.getText().toString();
        }


        // Get date and time
        String dateAndTime = selectedDateLabelView.getText().toString() + " " + selectedTimeView.getText().toString();
        Date selectedDate = parseDate(dateAndTime);


        // Get the kind of event
        RadioButton eventKindButton = findViewById(eventKindRadioGroup.getCheckedRadioButtonId());
        String kindText = eventKindButton.getText().toString();
        int kindNumber = -1;

        if(kindText.equals(getResources().getString(R.string.presentation_label)))
            kindNumber = 2;
        else if (kindText.equals(getResources().getString(R.string.exam_label)))
            kindNumber = 1;
        else if (kindText.equals(getResources().getString(R.string.schoolwork_label)))
            kindNumber = 0;

        return new Event(0,subject,selectedDate,location,description,kindNumber);

    }


    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_button:
                fragment = new DatePickerFragment();
                fragment.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.time_button:
                fragment = new TimePickerFragment();
                fragment.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.add_button:
                addBtn.setEnabled(false);
                Event newEvent = extractDataToEvent();
                Intent i = getIntent();
                i.putExtra(NEW_EVENT_EXTRA, newEvent);
                setResult(RESULT_OK, i);
                finish();
                break;
            case R.id.cancel_button:
                finish();
                break;
        }

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        selectedDateView.setText(String.format("%02d/%02d/%d", dayOfMonth, month + 1, year));

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        selectedTimeView.setText(String.format("%02d:%02d", hourOfDay, minute));
    }
}



