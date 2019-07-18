package com.etsisi.dev.etsisicrowdsensing;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications.NotificationsFragment;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.profile.ProfileFragment;
import com.etsisi.dev.etsisicrowdsensing.model.Event;
import com.etsisi.dev.etsisicrowdsensing.model.Incidence;
import com.etsisi.dev.etsisicrowdsensing.model.Notification;
import com.etsisi.dev.etsisicrowdsensing.utils.NonSwipeableViewPager;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity
    implements  CampusFragment.OnCampusFragmentInteractionListener,
                NotificationsFragment.OnFragmentInteractionListener,
                ProfileFragment.OnFragmentInteractionListener{

    private static final String TAG = "MainActivity";

    // SQLite Mobile Database
    private EventViewModel mEventViewModel;

    private AHBottomNavigation bottomNavigation;
    private boolean notificationVisible = false;

    /**
     * TODO Connect to DB
     * Incidences array
     */
    private ArrayList<Incidence> incidencesDataset;
    private ArrayList<Event> eventsDataset;

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data from SQLite Database containing events is loaded in the Campus Fragment
        incidencesDataset = new ArrayList<>();

        //FirebaseAuth mAuth = FirebaseAuth.getInstance();


        bottomNavigation = findViewById(R.id.bottom_navigation);
        addBottomNavigationItems();
        setupBottomNavStyle();

        // Fake notification (Delete line below)
        createFakeNotification("1", 5000);
        createFakeNotification("2", 10000);


        bottomNavigation.setBehaviorTranslationEnabled(false);
        // Instantiate a ViewPager and a PagerAdapter.
        NonSwipeableViewPager mPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);

        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());

        mPager.setAdapter(mPagerAdapter);


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // remove notification badge
                if (notificationVisible && position == 1)
                    bottomNavigation.setNotification(new AHNotification(), 1);

                mPager.setCurrentItem(position);

                return true;
            }
        });


        // Load campus fragment by default
        //loadFragment(new CampusFragment());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);
    }

    public String getAuthenticatedUserId(){
        return mAuth.getCurrentUser().getUid();
    }

    /*
    private void loadFragment(Fragment fragment) {
        // Create new fragment and transaction
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragment_container, fragment);
        //transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }



    /*
    private void loadIncidences(){
        incidencesDataset = new ArrayList<Incidence>();
    }



    private void loadIncidencesSample(){
        incidencesDataset = new ArrayList<Incidence>();
        Incidence in1 = new Incidence("Material", "Pizarra", 3402, new Date());
        Incidence in2 = new Incidence("Ambiente", "Frío", 3, new Date());
        Incidence in3 = new Incidence("Instalaciones", "Máquinas expendedoras", 3, new Date());
        incidencesDataset.add(in1);
        incidencesDataset.add(in2);
        incidencesDataset.add(in3);
    }
    */

    /*
    private void loadEvents(){
        eventsDataset = new ArrayList<Event>();
        loadEventsSample();
    }

    private void loadEventsSample(){
        eventsDataset = new ArrayList<Event>();
        Event ev1 = new Event("Estadística", parseDate("13/11/2018 12:00"), "Definición axiomática de probabilidad", "Temas 1,2", 0);
        Event ev2 = new Event("Aspectos éticos y sociales", parseDate("15/12/2018 12:00"), "Aula 5201","Tema 7", 1);
        Event ev3 = new Event("Fundamentos de Ingeniería del Software", parseDate("14/12/2018 12:00"), "Bloque IX", "Tema 2", 2);

        eventsDataset.add(ev1);
        eventsDataset.add(ev2);
        eventsDataset.add(ev3);
    }
    */

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(date);
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            return null;
        }
    }

    @Override
    public boolean addIncidence(Incidence incidence){
        // TODO Update database
        return incidencesDataset.add(incidence);
    }

    @Override
    public boolean deleteIncidence(Incidence incidence){
        // TODO Update database
        return incidencesDataset.remove(incidence);
    }

    /*
    @Override
    public boolean addEvent(Event event) {
        return eventsDataset.add(event);
    }

    @Override
    public boolean deleteEvent(Event event) {
        return eventsDataset.remove(event);
    }
    */

    public ArrayList<Incidence> getIncidencesDataset() {
        return incidencesDataset;
    }

    public ArrayList<Event> getEventsDataset() {
        return eventsDataset;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * Adds styling properties to {@link AHBottomNavigation}
     */
    private void setupBottomNavStyle() {
        // Change colors
        // Set background color
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setAccentColor(Color.parseColor("#2196F3"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));
        //  Enables Reveal effect
        bottomNavigation.setColored(false);

        // Set current item programmatically
        bottomNavigation.setCurrentItem(0);

        // Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#F63D2B"));

        //  Displays item Title always (for selected and non-selected items)
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
    }

    /**
     * Adds (items) {@link AHBottomNavigationItem} to {@link AHBottomNavigation}
     * Also assigns a distinct color to each Bottom Navigation item, used for the color ripple.
     */
    private void addBottomNavigationItems() {
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.title_campus, R.drawable.ic_campus,R.color.white);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.title_notifications, R.drawable.ic_notifications, R.color.white);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.title_profile, R.drawable.ic_profile, R.color.white);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
    }

    private void createFakeNotification(String number, long delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText(number)
                        .setBackgroundColor(Color.RED)
                        .setTextColor(Color.WHITE)
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, 1);
                notificationVisible = true;
            }
        }, delay);
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return CampusFragment.newInstance();
                case 1:
                    return NotificationsFragment.newInstance();
                case 2:
                    return ProfileFragment.newInstance("a");
                default:
                    return null;
            }

        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            instantiatedFragments.put(position, new WeakReference<>(fragment));
            return fragment;
        }

        @Override
        public void destroyItem(final ViewGroup container, final int position, final Object object) {
            instantiatedFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        @Nullable
        public Fragment getFragment(final int position) {
            final WeakReference<Fragment> wr = instantiatedFragments.get(position);
            if (wr != null) {
                return wr.get();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
