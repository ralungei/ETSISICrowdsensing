package com.etsisi.dev.etsisicrowdsensing;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.CampusFragment;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications.NotificationsFragment;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity
    implements  CampusFragment.OnFragmentInteractionListener,
                NotificationsFragment.OnFragmentInteractionListener,
                ProfileFragment.OnFragmentInteractionListener{


    private AHBottomNavigation bottomNavigation;
    private boolean notificationVisible = false;

           // OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
           // = new BottomNavigationView.OnNavigationItemSelectedListener() {

    /*
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_campus:
                    loadFragment(CampusFragment.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(NotificationsFragment.newInstance());
                    return true;
                case R.id.navigation_profile:
                    loadFragment(ProfileFragment.newInstance("Nombre"));
                    return true;
            }
            return false;
        }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        addBottomNavigationItems();
        setupBottomNavStyle();

        // Fake notification (Delete line below)
        createFakeNotification();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                // remove notification badge
                if (notificationVisible && position == 1)
                    bottomNavigation.setNotification(new AHNotification(), 1);

                switch (position) {
                    case 0:
                        loadFragment(CampusFragment.newInstance());
                        return true;
                    case 1:
                        loadFragment(NotificationsFragment.newInstance());
                        return true;
                    case 2:
                        loadFragment(ProfileFragment.newInstance("Nombre"));
                        return true;
                }
                return false;
            }
        });

        // Load campus fragment by default
        loadFragment(new CampusFragment());

    }

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

    private void createFakeNotification() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AHNotification notification = new AHNotification.Builder()
                        .setText("1")
                        .setBackgroundColor(Color.RED)
                        .setTextColor(Color.WHITE)
                        .build();
                // Adding notification to last item.
                bottomNavigation.setNotification(notification, 1);
                notificationVisible = true;
            }
        }, 5000);
    }
}
