package com.etsisi.dev.etsisicrowdsensing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.GroupsWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.NameWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.PlanWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.SubjectWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.WelcomeWizardFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.YearSubjectsFragment;
import com.etsisi.dev.etsisicrowdsensing.user.wizard.dummy.DummyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class UserScheduleActivity extends FragmentActivity
            implements WelcomeWizardFragment.OnFragmentInteractionListener,
        NameWizardFragment.OnFragmentInteractionListener,
        PlanWizardFragment.OnFragmentInteractionListener,
        SubjectWizardFragment.OnFragmentInteractionListener,
        YearSubjectsFragment.OnListFragmentInteractionListener,
        GroupsWizardFragment.OnFragmentInteractionListener {

    private static String TAG = "UserScheduleActivity";

    private View welcomeView;

    private View progressView;


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private NonSwipeableViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    /**
     * Firebase Database object
     */
    private FirebaseFirestore db;

    /**
     * The number of collections recovered from database
     */
    private static final int NUM_COLLECTIONS = 2;

    private volatile int sync = 0;

    /**
     * Progress bar
     */
    private View mProgressView;


    /**
     * Array with the plans recovered from the Firebase cloud database
     */
    private ArrayList<String> plansArray = new ArrayList<>();

    /**
     * Array with the subjects recovered from the Firebase cloud database
     */
    private ArrayList<Subject> subjectsArray = new ArrayList<Subject>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_schedule);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Progress bar from fragment
        mProgressView = findViewById(R.id.login_progress);


        // Instantiate database connection
        db = FirebaseFirestore.getInstance();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (NonSwipeableViewPager) findViewById(R.id.pager);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        // Show progress on welcome fragment
        WelcomeWizardFragment welcomeFragment = (WelcomeWizardFragment) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem());
        Log.d(TAG, welcomeFragment.getTag() + welcomeFragment.toString());

        welcomeFragment.showProgress(true);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void onNextPressed() {
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);

    }

    public void hideProgress() {
        WelcomeWizardFragment welcomeFragment = (WelcomeWizardFragment) mPagerAdapter.instantiateItem(mPager, mPager.getCurrentItem());
        welcomeFragment.showProgress(false);
    }
    
    public void dataCollectionFirestore() {

        db.collection("asignaturas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Subject subject = document.toObject(Subject.class);
                                subjectsArray.add(subject);
                            }
                            Log.d(TAG, "Documents in 'asignaturas' where recovered successfully from database");

                            //sync++;
                            //if(sync == NUM_COLLECTIONS)
                            // showProgressBar(false)
                        }
                        else{
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });



        // Collect plans array
        db.collection("planes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String planName = document.get("nombre").toString();

                                if (planName.length() > 26)
                                    planName = planName.substring(0, 26);

                                plansArray.add(planName);
                            }

                            Log.d(TAG, "Documents in 'planes where recovered successfully from database");

                            //sync++;
                            //if(sync == NUM_COLLECTIONS)
                            // showProgressBar(false)

                        }
                        else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();


        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return WelcomeWizardFragment.newInstance();
                case 1:
                    return NameWizardFragment.newInstance();
                case 2:
                    return PlanWizardFragment.newInstance(plansArray);
                case 3:
                    return SubjectWizardFragment.newInstance(plansArray);
                case 4:
                    return GroupsWizardFragment.newInstance();
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
            return NUM_PAGES;
        }
    }
}
