package com.etsisi.dev.etsisicrowdsensing.user.wizard;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.etsisi.dev.etsisicrowdsensing.NonSwipeableViewPager;
import com.etsisi.dev.etsisicrowdsensing.R;

import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubjectWizardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubjectWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectWizardFragment extends Fragment {

    private static final String ARG_PARAM1 = "plans";

    private ArrayList<String> plans;

    private OnFragmentInteractionListener mListener;

    public SubjectWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment SubjectWizardFragment.
     */
    public static SubjectWizardFragment newInstance(ArrayList<String> param1) {
        SubjectWizardFragment fragment = new SubjectWizardFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            plans = getArguments().getStringArrayList(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subject_wizard, container, false);

        // Setting ViewPager for each Tabs
        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) view.findViewById(R.id.pager);

        mScreenSlidePagerAdapter mPagerAdapter = new mScreenSlidePagerAdapter(getFragmentManager());

        viewPager.setAdapter(mPagerAdapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        //tabs.setupWithViewPager(viewPager);


        // Inflate the layout for this fragment
        return view;
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class mScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public mScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /*
            HashMap<String,String>  asignaturas = new HashMap<String, String>();
            switch (position) {
                case 0:
                    return YearSubjectsFragment.newInstance(asignaturas["primero"]);
                case 1:
                    return YearSubjectsFragment.newInstance(asignaturas["segundo"]);
                case 2:
                    return YearSubjectsFragment.newInstance(asignaturas["tercero"]);
                case 3:
                    return YearSubjectsFragment.newInstance(asignaturas["cuarto"]);
                default:
                    return null;
            }
            */

            switch (position) {
                case 0:
                    return YearSubjectsFragment.newInstance(1);
                case 1:
                    return YearSubjectsFragment.newInstance(2);
                case 2:
                    return YearSubjectsFragment.newInstance(3);
                case 3:
                    return YearSubjectsFragment.newInstance(4);
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
