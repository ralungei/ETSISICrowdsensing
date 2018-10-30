package com.etsisi.dev.etsisicrowdsensing;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.android.gms.maps.model.Circle;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class CalendarActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



       MaterialCalendarView calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);


       Calendar calendar = Calendar.getInstance();
       calendarView.setDateSelected(calendar, true);

       CalendarDay currentDay = CalendarDay.from(new Date());
       calendarView.addDecorators(new EventDecorator(ContextCompat.getColor(this, R.color.colorPrimary)
               , currentDay));



    }

}

class EventDecorator implements DayViewDecorator {

    private final int color;
    private final CalendarDay date;

    public EventDecorator(int color, CalendarDay date) {
        this.color = color;
        this.date = date;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return day.equals(this.date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, color));
    }
}
