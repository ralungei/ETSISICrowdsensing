package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.etsisi.dev.etsisicrowdsensing.MainActivity;
import com.etsisi.dev.etsisicrowdsensing.alarms.AlarmReceiver;
import com.etsisi.dev.etsisicrowdsensing.EventViewModel;
import com.etsisi.dev.etsisicrowdsensing.ParcelableUtil;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.NewEventActivity;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences.IncidentsAdapter;
import com.etsisi.dev.etsisicrowdsensing.utils.SwipeHelper;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences.NewIncidenceActivity;
import com.etsisi.dev.etsisicrowdsensing.model.Event;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.EventItemClickListener;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.EventsAdapter;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences.IncidenceDetailActivity;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences.IncidenceItemClickListener;
import com.etsisi.dev.etsisicrowdsensing.activities.FoodActivity;
import com.etsisi.dev.etsisicrowdsensing.activities.MapsActivity;
import com.etsisi.dev.etsisicrowdsensing.activities.NewsActivity;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.activities.TransportActivity;
import com.etsisi.dev.etsisicrowdsensing.web.api.network.RetrofitInstance;
import com.etsisi.dev.etsisicrowdsensing.web.api.network.RetrofitUPMInstance;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CampusFragment.OnCampusFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CampusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CampusFragment extends Fragment implements View.OnClickListener,
        IncidenceItemClickListener,
        EventItemClickListener {

    private static final String TAG = "CampusFragment";

    static final int NEW_INCIDENCE_REQUEST = 1;
    static final int INCIDENCE_DELETE_REQUEST = 2;
    public static String NEW_INCIDENCE_EXTRA = "new_incidence";
    public static String DELETE_INCIDENCE_ID_EXTRA = "delete_incidence";

    static final int NEW_EVENT_REQUEST = 3;
    private String NEW_EVENT_EXTRA = "new_event";
    public static final String EVENT_EXTRA = "event";
    public static final String ALARM_KIND_EXTRA = "alarm_kind";

    // SQLite Local Database
    // Connection with the data
    private EventViewModel mEventViewModel;

    // Alarm request code
    // TODO Not sure how does it works
    public static final int REQUEST_CODE = 0;


    private OnCampusFragmentInteractionListener mListener;

    private static final String ARG_INCIDENCES_ARRAY = "incidences-array";
    private static final String ARG_EVENTS_ARRAY = "events-array";

    private ProgressBar incidencesProgressView;


    private RecyclerView incidencesRecyclerView;
    private RecyclerView.LayoutManager incidencesLayoutManager;
    private RecyclerView.Adapter incidencesAdapter;
    private ArrayList<Incidence> incidencesDataset;

    private RecyclerView eventsRecyclerView;
    private RecyclerView.LayoutManager eventsLayoutManager;
    private EventsAdapter eventsAdapter;
    //private ArrayList<Event> eventsDataset;

    private SwipeHelper swipeHelper;

    private View emptyIncidencesView;

    private Incidence selectedIncidenceForDetail;

    private RetrofitInstance restService;
    private RetrofitUPMInstance restUPMService;


    public CampusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CampusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CampusFragment newInstance() {
        CampusFragment fragment = new CampusFragment();
        return fragment;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Fragment was stopped");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // WebAPI Rest Interface Service
        restService = new RetrofitInstance();
        restUPMService = new RetrofitUPMInstance();


        /**
         * Add button bubble incidence
         * Must be added before recovering incidences from DB
         */
        incidencesDataset = new ArrayList<>();
        addIncidenceBubble();

        /*
        eventsDataset.addAll(((MainActivity)getActivity()).getEventsDataset());
        Collections.sort(eventsDataset, new SortByDate());
        */


        // Get a new or existing ViewModel from the ViewModelProvider.
        mEventViewModel = ViewModelProviders.of(this).get(EventViewModel.class);
        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        mEventViewModel.getAllEvents().observe(this, events -> {
            // Update the cached copy of the words in the adapter.
            Calendar c = Calendar.getInstance();

            for(Event event: events){
                long time = event.getTime().getTime();
                if(DateUtils.isToday(time))
                    Log.d(TAG, "IS TODAY");
                if(c.getTime().getTime() > time)
                    Log.d(TAG, "TIME IS BIGGER");

                if(DateUtils.isToday(time) &&  System.currentTimeMillis() > time) {
                    mEventViewModel.remove(event);
                    deleteEventAlarm(event);
                }


            }
            eventsAdapter.setEvents(events);
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            incidencesDataset = (ArrayList<Incidence>) savedInstanceState.getSerializable(ARG_INCIDENCES_ARRAY);
        }

        actionBarConfig();

        incidencesProgressView = getActivity().findViewById(R.id.incidencesProgressBar);

        emptyIncidencesView = getActivity().findViewById(R.id.empty_events_view);

        cardListeners();
        cardImages();


        incidencesRecyclerViewConfig();

        if(incidencesDataset.size() == 1) {
            showProgress(true);
            loadIncidencesWebAPI();
        }

        // todo start scheduler
        scheduleHTTPIncidenceRetrieve();



        eventsRecyclerViewConfig();

        // TODO Enable swipe for delete option on events recycler view
        editDeleteRows(eventsRecyclerView);

        RelativeLayout newEventViewBtn = getActivity().findViewById(R.id.new_event);
        newEventViewBtn.setOnClickListener(this);

        try {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firebase id login", "Refreshed token: " + refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList(ARG_INCIDENCES_ARRAY, incidencesDataset);
        //outState.putSerializable(ARG_SELECTED_SUBJECTS, selectedSubjectsPerCourseHashMap);

        outState.putSerializable(ARG_INCIDENCES_ARRAY, incidencesDataset);
        // outState.putString("key","value");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void addIncidenceBubble() {
        Incidence newIncidenceBubble = new Incidence("0", "","Añadir", "Añadir", 0, new Date());
        incidencesDataset.add(newIncidenceBubble);
    }

    public void reloadIncidencesData() {
        incidencesDataset.clear();
        addIncidenceBubble();
        //incidencesDataset.addAll(((MainActivity) getActivity()).getIncidencesDataset());
        incidencesAdapter.notifyDataSetChanged();
    }

    private void scheduleHTTPIncidenceRetrieve(){
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate(this::loadIncidencesWebAPI, 10, 5, TimeUnit.SECONDS);
    }

    private void loadIncidencesWebAPI(){
        //Call to server to grab list of incidences records. this is a asyn
        // Check if there is only the add button event
        //if(incidencesDataset.size() == 1) {
        Call<ArrayList<Incidence>> call = restService.getIncidencesService().getIncidencias(((MainActivity)getActivity()).getAuthenticatedUserId());
            call.enqueue(new Callback<ArrayList<Incidence>>() {
                @Override
                public void onResponse(Call<ArrayList<Incidence>> call, Response<ArrayList<Incidence>> response) {
                    if (response.isSuccessful()) {
                        //Toast.makeText(getActivity(), "Call was successful", Toast.LENGTH_LONG).show();

                        // Check if there are new incidences by lenght check
                        //if (response.body().size() != incidencesDataset.size() - 1) {
                            incidencesDataset.clear();
                            addIncidenceBubble();
                            incidencesDataset.addAll(response.body());
                            incidencesAdapter.notifyDataSetChanged();
                        //}
                            showProgress(false);
                    } else {
                        Log.e(TAG, "Response from Retrofit incidences wasn't successful");
                        if(incidencesProgressView.getVisibility() == View.VISIBLE)
                            showProgress(false);
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Incidence>> call, Throwable t) {
                    if(incidencesProgressView.getVisibility() == View.VISIBLE)
                        showProgress(false);
                    Log.e(TAG, t.getMessage().toString());
                    //Toast.makeText(getActivity(), t.getMessage().toString(), Toast.LENGTH_LONG).show();
                    // TODO Show a retry button that launches this method
                }
            });
        //}
    }


    // Load Logo ImageView using Glide Library
    public void loadImageUsingGlide(int drawableId, ImageView imageView) {
        Glide
                .with(getContext())
                .load(drawableId)
                .into(imageView);
    }


    /**
     * Shows the progress UI and hides the incidences recycler
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if(getContext() != null) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            incidencesRecyclerView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
            incidencesRecyclerView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    incidencesRecyclerView.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
                }
            });

            Log.d(TAG, "Visibility is " + incidencesProgressView);

            incidencesProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
            incidencesProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    incidencesProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }


    public void actionBarConfig() {
        // Set action bar
        Toolbar myToolbar = (Toolbar) getView().findViewById(R.id.my_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
        ActionBar myActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        myActionBar.setDisplayShowTitleEnabled(false);
        myActionBar.setDisplayShowHomeEnabled(true);
        loadLogoImage();
    }

    public void incidencesRecyclerViewConfig() {
        // Empty recycler view message
        // TextView emptyView = getActivity().findViewById(R.id.empty_view);

        // Incidences data sample
        incidencesRecyclerView = (RecyclerView) getView().findViewById(R.id.incidences_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        incidencesRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        incidencesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        incidencesRecyclerView.setLayoutManager(incidencesLayoutManager);

        // specify an adapter (see also next example)
        incidencesAdapter = new IncidentsAdapter(getContext(), this, incidencesDataset);
        incidencesRecyclerView.setAdapter(incidencesAdapter);

    }

    public void eventsRecyclerViewConfig() {
        eventsRecyclerView = (RecyclerView) getView().findViewById(R.id.events_recycler_view);

        //TextView emptyView = getView().findViewById(R.id.empty_view);
        //eventsRecyclerView.setEmptyView(emptyView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        // use a linear layout manager
        eventsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        eventsRecyclerView.setLayoutManager(eventsLayoutManager);

        // specify an adapter (see also next example)
        eventsAdapter = new EventsAdapter(getContext(), this);
        eventsRecyclerView.setAdapter(eventsAdapter);

        eventsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(eventsAdapter.getItemCount() == 0)
                    showEmptyIncidencesView(true);
                else
                    showEmptyIncidencesView(false);
            }
        });
    }

    public void showEmptyIncidencesView(boolean show){
        if(show)
            emptyIncidencesView.setVisibility(View.VISIBLE);
        else
            emptyIncidencesView.setVisibility(View.GONE);
    }

    public void cardImages() {
        ImageView transportImage = (ImageView) getView().findViewById(R.id.iconTransport);
        ImageView foodImage = (ImageView) getView().findViewById(R.id.iconFood);
        ImageView mapImage = (ImageView) getView().findViewById(R.id.iconMap);
        ImageView newsImage = (ImageView) getView().findViewById(R.id.iconNews);

        loadImageUsingGlide(R.drawable.icon_transport, transportImage);
        loadImageUsingGlide(R.drawable.icon_food, foodImage);
        loadImageUsingGlide(R.drawable.icon_map, mapImage);
        loadImageUsingGlide(R.drawable.icon_news, newsImage);
    }

    public void cardListeners() {
        // Cards listeners
        CardView transportCard = (CardView) getView().findViewById(R.id.transportCard);
        CardView foodCard = (CardView) getView().findViewById(R.id.foodCard);
        CardView mapCard = (CardView) getView().findViewById(R.id.mapCard);
        CardView newsCard = (CardView) getView().findViewById(R.id.newsCard);

        transportCard.setOnClickListener(this);
        foodCard.setOnClickListener(this);
        mapCard.setOnClickListener(this);
        newsCard.setOnClickListener(this);
    }

    // Load Logo ImageView using Glide Library
    public void loadLogoImage() {
        int logoResourceId = R.drawable.campus_logo;
        ImageView imageView = (ImageView) getView().findViewById(R.id.action_bar_logo);
        loadImageUsingGlide(logoResourceId, imageView);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCampusFragmentInteractionListener) {
            mListener = (OnCampusFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.mapCard:
                i = new Intent(getActivity(), MapsActivity.class);
                startActivity(i);
                break;
            case R.id.newsCard:
                i = new Intent(getActivity(), NewsActivity.class);
                startActivity(i);
                break;
            case R.id.transportCard:
                i = new Intent(getActivity(), TransportActivity.class);
                startActivity(i);
                break;
            case R.id.foodCard:
                i = new Intent(getActivity(), FoodActivity.class);
                startActivity(i);
                break;
            case R.id.new_event:
                i = new Intent(getActivity(), NewEventActivity.class);
                startActivityForResult(i, NEW_EVENT_REQUEST);
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onIncidenceItemClick(int pos, Incidence incidence, ImageView sharedImageView) {
        Intent intent;

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                getActivity(),
                sharedImageView,
                "bubbleImage");


        if (incidence.getCategory().equals("Añadir")) {
            intent = new Intent(getContext(), NewIncidenceActivity.class);
            startActivityForResult(intent, NEW_INCIDENCE_REQUEST);
        } else {
            //this.selectedIncidenceForDetail = incidence;
            intent = new Intent(getContext(), IncidenceDetailActivity.class);
            intent.putExtra("incidence", incidence);
            startActivityForResult(intent, INCIDENCE_DELETE_REQUEST, options.toBundle());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_INCIDENCE_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                // Add incidence
                Incidence newIncidence = data.getParcelableExtra(NEW_INCIDENCE_EXTRA);

                Call<Void> call = restService.getIncidencesService().postIncidencia(newIncidence);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "Response received");
                        if(response.isSuccessful())
                            loadIncidencesWebAPI();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, t.getMessage().toString());
                    }
                });



                // Add to UPM Database
                /*Call<Void> callUPM = restUPMService.getIncidencesService().postIncidencia(newIncidence);
                callUPM.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d(TAG, "UPM response received " + response.message());
                        if(response.isSuccessful())
                            Log.d(TAG, "UPM response is " + response.message());
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, t.getMessage().toString());
                    }
                });*/
            }
        } else if (requestCode == INCIDENCE_DELETE_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                String deleteIncidenciaId = data.getStringExtra(DELETE_INCIDENCE_ID_EXTRA);
                // Call Retrofit to delete
                Call<Void> call = restService.getIncidencesService().deleteIncidenciaById(deleteIncidenciaId);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.isSuccessful())
                            loadIncidencesWebAPI();
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.d(TAG, t.getMessage().toString());
                    }
                });
                // TODO Once deleted reload incidences


            }
        } else if (requestCode == NEW_EVENT_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {

                Event newEvent = data.getParcelableExtra(NewEventActivity.NEW_EVENT_EXTRA);

                mEventViewModel.insert(newEvent);
                // Add event alarm
                scheduleEventAlarm(newEvent);

                /*
                if(mListener.addEvent(newEvent)){
                    Log.d(TAG, "Event was successfully added.");
                    reloadEventsData();
                    // Add event alarm
                    scheduleEventAlarm(newEvent);
                }
                else
                    Log.e(TAG, "Event couldn't be added.");
                */
            }
        }
    }


    private void scheduleEventAlarm(Event event) {
        // BEGIN_INCLUDE (receiver enabled)
        // Once you enable the receiver this way, it will stay enabled, even if the user reboots
        // the device. In other words, programmatically enabling the receiver overrides the
        // manifest setting, even across reboots. The receiver will stay enabled until your app
        // disables it.
        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
        PackageManager pm = getActivity().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        // END_INCLUDE (receiver enabled)

        // BEGIN_INCLUDE (configure_alarm_manager)
        // There are two clock types for alarms, ELAPSED_REALTIME and RTC.
        // ELAPSED_REALTIME uses time since system boot as a reference, and RTC uses UTC (wall
        // clock) time.  This means ELAPSED_REALTIME is suited to setting an alarm according to
        // passage of time (every 15 seconds, 15 minutes, etc), since it isn't affected by
        // timezone/locale.  RTC is better suited for alarms that should be dependant on current
        // locale.

        // Both types have a WAKEUP version, which says to wake up the device if the screen is
        // off.  This is useful for situations such as alarm clocks.  Abuse of this flag is an
        // efficient way to skyrocket the uninstall rate of an application, so use with care.
        // For most situations, ELAPSED_REALTIME will suffice.
        int alarmType = AlarmManager.ELAPSED_REALTIME;
        final int FIFTEEN_SEC_MILLIS = 2000;

        // The AlarmManager, like most system services, isn't created by application code, but
        // requested from the system.
        AlarmManager alarmManager = (AlarmManager)
                getActivity().getSystemService(getActivity().ALARM_SERVICE);

        // setRepeating takes a start delay and period between alarms as arguments.
        // The below code fires after 15 seconds, and repeats every 15 seconds.  This is very
        // useful for demonstration purposes, but horrendous for production.  Don't be that dev.
        //alarmManager.setRepeating(alarmType, SystemClock.elapsedRealtime() + FIFTEEN_SEC_MILLIS,FIFTEEN_SEC_MILLIS, pendingIntent);

        // alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIFTEEN_SEC_MILLIS, createAlarmPendingIntent(event, AlarmReceiver.REMINDER_KIND));


        Calendar calendar = Calendar.getInstance();
        calendar.setTime(event.getTime());
        // unccomment line below
        //calendar.add(Calendar.MINUTE, 30);
        // Set alarm at specified time to delete event from database as it has already occurred
        // RTC—Fires the pending intent at the specified time but does not wake up the device


        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.getTime().getTime(),
                createAlarmPendingIntent(event, AlarmReceiver.COMPLETED_KIND));



        // Alarm reminder is set 30 minutes earlier
        //RTC_WAKEUP—Wakes up the device to fire the pending intent at the specified time.
        calendar.add(Calendar.MINUTE, -30);
        //calendar.add(Calendar.MINUTE, -1);
        alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.getTime().getTime(),
                createAlarmPendingIntent(event,AlarmReceiver.REMINDER_KIND));

        // END_INCLUDE (configure_alarm_manager);


        Log.i(TAG, "Alarm reminder and completed for event set");
    }


    private void deleteEventAlarm(Event event) {
        // TODO Maybe it is not necessary here to enable the receiver
        // Enable reciever
        /*
        ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
        PackageManager pm = getActivity().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        */

        // The AlarmManager, like most system services, isn't created by application code, but
        // requested from the system.
        AlarmManager alarmManager = (AlarmManager)
                getActivity().getSystemService(getActivity().ALARM_SERVICE);

        // Cancel the alarm matching the intent
        alarmManager.cancel(createAlarmPendingIntent(event, AlarmReceiver.COMPLETED_KIND));
        alarmManager.cancel(createAlarmPendingIntent(event, AlarmReceiver.REMINDER_KIND));

        // TODO Check if there are any pending alarms. If NOT disable receiver
        if (false) {
            // BEGIN_INCLUDE (receiver enabled)
            // Once you enable the receiver this way, it will stay enabled, even if the user reboots
            // the device. In other words, programmatically enabling the receiver overrides the
            // manifest setting, even across reboots. The receiver will stay enabled until your app
            // disables it.
            ComponentName receiver = new ComponentName(getContext(), AlarmReceiver.class);
            PackageManager pm = getActivity().getPackageManager();

            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP);
            // END_INCLUDE (receiver enabled)

        }


        // END_INCLUDE (configure_alarm_manager);
        Log.i(TAG, "Alarm canceled.");
    }

    private PendingIntent createAlarmPendingIntent(Event event, int kind) {
        // BEGIN_INCLUDE (intent_fired_by_alarm)
        // First create an intent for the alarm to activate.
        // This code simply starts an Activity, or brings it to the front if it has already
        // been created.
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
//        intent.putExtra(ALARM_KIND_EXTRA, kind);

        byte[] eventBytes = ParcelableUtil.marshall(event);
        intent.putExtra(ALARM_KIND_EXTRA, kind);

        intent.putExtra(EVENT_EXTRA, eventBytes);
        // END_INCLUDE (intent_fired_by_alarm)

        // BEGIN_INCLUDE (pending_intent_for_alarm)
        // Because the intent must be fired by a system service from outside the application,
        // it's necessary to wrap it in a PendingIntent.  Providing a different process with
        // a PendingIntent gives that other process permission to fire the intent that this
        // application has created.
        // Also, this code creates a PendingIntent to start an Activity.  To create a
        // BroadcastIntent instead, simply call getBroadcast instead of getIntent.
        int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), _id,
                intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        // END_INCLUDE (pending_intent_for_alarm)

        return pendingIntent;
    }

    @Override
    public void onEventItemClick(int pos, Event event) {
        Intent intent;
        // TODO Decide if on touch must be included in the Event Recycler View items
        //swipeHelper.attachSwipe();
        //Toast.makeText(getContext(), "Event in pos " + pos + " clicked  ",Toast.LENGTH_SHORT).show();

        //intent = new Intent(getContext(), EventDetailActivity.class);
        //startActivity(intent);
    }


    /**
     * Event row deletion
     */
    public void editDeleteRows(RecyclerView recyclerView) {
        swipeHelper = new SwipeHelper(getContext(), recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "ELIMINAR",
                        0,
                        getResources().getColor(R.color.red),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                AlertDialog confirmationDialog = eventDeletionConfirmationDialog(pos);
                                confirmationDialog.show();
                                confirmationDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.red));


                            }
                        }
                ));
            }
        };
    }

    public AlertDialog eventDeletionConfirmationDialog(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.event_deletion_confirmation);
        // Add the buttons
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /*
                Event deletedEvent = eventsDataset.get(pos);
                mListener.deleteEvent(deletedEvent);
                eventsAdapter.removeItem(pos);
                */
                // Handle SQLite Database addition
                Event deletedEvent = eventsAdapter.getEvent(pos);
                mEventViewModel.remove(deletedEvent);
                eventsAdapter.notifyItemRangeChanged(pos, eventsAdapter.getItemCount());
                // Delete event alarm
                deleteEventAlarm(deletedEvent);

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


    class SortByDate implements Comparator<Event> {
        // Used for sorting in ascending order of
        // date
        public int compare(Event a, Event b) {
            if (a.getTime().after(b.getTime()))
                return 1;
            else if (a.getTime().before(b.getTime()))
                return -1;
            else
                return 0;
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnCampusFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        /**
         * Incidences data interaction with activity
         */
        boolean addIncidence(Incidence incidence);

        boolean deleteIncidence(Incidence incidence);

    }

}
