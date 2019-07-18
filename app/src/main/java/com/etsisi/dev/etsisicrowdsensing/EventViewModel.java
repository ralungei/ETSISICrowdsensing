package com.etsisi.dev.etsisicrowdsensing;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.etsisi.dev.etsisicrowdsensing.model.Event;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private EventRepository mRepository;
    private LiveData<List<Event>> mAllEvents;


    public EventViewModel(@NonNull Application application) {
        super(application);
        mRepository = new EventRepository(application);
        mAllEvents = mRepository.getAllEvents();
    }

    public LiveData<List<Event>> getAllEvents() { return mAllEvents; }
    public void insert(Event event) { mRepository.insert(event); }
    public void remove(Event event) { mRepository.delete(event); }


}
