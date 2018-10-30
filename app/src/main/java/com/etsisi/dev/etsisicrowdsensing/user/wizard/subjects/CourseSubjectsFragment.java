package com.etsisi.dev.etsisicrowdsensing.user.wizard.subjects;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link CourseSubjectsRecyclerViewAdapter.OnPositionClickListener}
 * interface.
 */
public class CourseSubjectsFragment extends Fragment implements CourseSubjectsRecyclerViewAdapter.OnPositionClickListener {

    private static final String ARG_FRAGMENT_COURSE = "fragment-course";
    private static final String ARG_SUBJECTS_ARRAY = "subjects-array";
    private static final String ARG_STATUS_ARRAY = "status-array";

    private int fragmentCourse;
    private ArrayList<Subject> subjects;

    private OnChildFragmentInteractionListener mParentListener;

    // Array of booleans to store switches status
    public boolean[] statusArray;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CourseSubjectsFragment() {
    }

    @SuppressWarnings("unused")
    public static CourseSubjectsFragment newInstance(int fragmentCourse, ArrayList<Subject> subjects) {
        CourseSubjectsFragment fragment = new CourseSubjectsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FRAGMENT_COURSE,fragmentCourse);
        args.putParcelableArrayList(ARG_SUBJECTS_ARRAY, subjects);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            fragmentCourse = getArguments().getInt(ARG_FRAGMENT_COURSE);
            subjects = getArguments().getParcelableArrayList(ARG_SUBJECTS_ARRAY);
        }

        if(savedInstanceState !=  null){
            statusArray = savedInstanceState.getBooleanArray(ARG_STATUS_ARRAY);
        }
        else{
            statusArray = new boolean[subjects.size()];
            Arrays.fill(statusArray, Boolean.FALSE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coursesubjects_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));


            recyclerView.setAdapter(new CourseSubjectsRecyclerViewAdapter(subjects, this, statusArray));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBooleanArray(ARG_STATUS_ARRAY, statusArray);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() instanceof OnChildFragmentInteractionListener) {
            mParentListener = (OnChildFragmentInteractionListener) getParentFragment();
        } else {
            throw new RuntimeException(getParentFragment().getContext().toString()
                    + " must implement OnChildFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mParentListener = null;
    }

    @Override
    public void itemClicked(int position, boolean isChecked) {
        statusArray[position] = isChecked;

        //Toast.makeText(getContext(), "Position is -> " + position + " and isChecked is -> " + isChecked + " course is -> " + fragmentCourse, Toast.LENGTH_SHORT).show();

        mParentListener.onSelectedSubjectsChange(fragmentCourse, isChecked, subjects.get(position));
    }

    public boolean[] getStatus(){
        return statusArray;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnChildFragmentInteractionListener {
        void onSelectedSubjectsChange(int fragmentCourse, boolean checked, Subject subject);
    }



}

