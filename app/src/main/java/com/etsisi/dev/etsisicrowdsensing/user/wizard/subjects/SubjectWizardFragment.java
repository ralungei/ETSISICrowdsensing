package com.etsisi.dev.etsisicrowdsensing.user.wizard.subjects;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;
import com.etsisi.dev.etsisicrowdsensing.UserScheduleActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubjectWizardFragment.OnSubjectsSubmittedListener} interface
 * to handle interaction events.
 * Use the {@link SubjectWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectWizardFragment extends Fragment implements CourseSubjectsFragment.OnChildFragmentInteractionListener {

    private static String TAG = "SubjectWizardFragment";

    private static final String ARG_SUBJECTS = "plan-subjects-per-course";
    private static final String ARG_SELECTED_SUBJECTS = "selected-subjects";
    private static final String ARG_COURSES_TITLES = "courses-titles";

    /**
     * The HashMap with the subject array for each course.
     * This is received from parent activity and is not modified.
     */
    private HashMap<Integer, ArrayList<Subject>> planSubjectsPerCourseHashMap;


    /**
     * The HashMap with the user selected subjects of each course.
     */
    private HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap = new HashMap<>();

    private OnSubjectsSubmittedListener mListener;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private mScreenSlidePagerAdapter mPagerAdapter;

    private ViewPager viewPager;

    private ArrayList<String> coursesTitles;


    private Button nextButton;

    public SubjectWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SubjectWizardFragment.
     */
    public static SubjectWizardFragment newInstance(ArrayList<String> coursesTitles, HashMap<Integer, ArrayList<Subject>> planSubjectsPerCourseHashMap) {
        SubjectWizardFragment fragment = new SubjectWizardFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_SUBJECTS, planSubjectsPerCourseHashMap);
        args.putStringArrayList(ARG_COURSES_TITLES, coursesTitles);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            planSubjectsPerCourseHashMap = (HashMap<Integer, ArrayList<Subject>>) getArguments().getSerializable(ARG_SUBJECTS);
            coursesTitles = getArguments().getStringArrayList(ARG_COURSES_TITLES);
        }

        // En lugar de inicializar el hashmap con valores vacios estoy intentando que solo se inicialicen aquellos cursos con asignaturas dentro
        //loadEmptySubjectsArraysOnHashMap(selectedSubjectsPerCourseHashMap);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_subject_wizard, container, false);

        // Setting ViewPager for each Tabs
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        mPagerAdapter = new mScreenSlidePagerAdapter(coursesTitles, getChildFragmentManager());

        viewPager.setAdapter(mPagerAdapter);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(viewPager);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextButton = getView().findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSubjectsSubmitted(selectedSubjectsPerCourseHashMap);
                ((UserScheduleActivity) getActivity()).onNextPressed();
            }
        });

        Button backButton = getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((UserScheduleActivity) getActivity()).onBackPressed();
            }
        });


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSubjectsSubmittedListener) {
            mListener = (OnSubjectsSubmittedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSubjectsSubmittedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSelectedSubjectsChange(int fragmentCourse, boolean isChecked, Subject subject) {
        if (isChecked) {
            // Check if the fragment course already has any subjects
            if (selectedSubjectsPerCourseHashMap.get(fragmentCourse) == null)
                selectedSubjectsPerCourseHashMap.put(fragmentCourse, new ArrayList<>());
            selectedSubjectsPerCourseHashMap.get(fragmentCourse).add(subject);

        } else {
            if (selectedSubjectsPerCourseHashMap.get(fragmentCourse) != null)
                selectedSubjectsPerCourseHashMap.get(fragmentCourse).remove(subject);
            if (selectedSubjectsPerCourseHashMap.get(fragmentCourse).isEmpty())
                selectedSubjectsPerCourseHashMap.remove(fragmentCourse);
        }
        nextButton.setEnabled(anySubjectSelected());
    }

    /*
    public void loadNullSelectedSubjectsHashMap(){
        for (Map.Entry<Integer, ArrayList<Subject>> entry: planSubjectsPerCourseHashMap.entrySet()) {
            Integer course = entry.getKey();
            ArrayList<Subject> subjectsArray = entry.getValue();
            boolean[] nullArray = new boolean[subjectsArray.size()];
            Arrays.fill(nullArray, Boolean.FALSE);
            selectedSubjectsHashMap.put(course, nullArray);
        }
    }
    */

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(ARG_SELECTED_SUBJECTS, selectedSubjectsPerCourseHashMap);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            selectedSubjectsPerCourseHashMap = (HashMap<Integer, ArrayList<Subject>>) savedInstanceState.getSerializable(ARG_SELECTED_SUBJECTS);
            // The state of the button in case the fragment is destroyed is recovered here
            // This is not best practice as the model part should be detached from the views
            nextButton.setEnabled(anySubjectSelected());
        }
    }

    public void loadEmptySubjectsArraysOnHashMap(HashMap<Integer, ArrayList<Subject>> hm) {
        for (int i = 0; i < coursesTitles.size(); i++)
            hm.put(i, new ArrayList<>());
    }

    private boolean anySubjectSelected() {
        for (ArrayList<Subject> subjects : selectedSubjectsPerCourseHashMap.values()) {
            if (!subjects.isEmpty())
                return true;
        }
        return false;
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
    public interface OnSubjectsSubmittedListener {
        public void onSubjectsSubmitted(HashMap<Integer, ArrayList<Subject>> selectedSubjects);
    }


    /**
     * A simple pager adapter that represents (number of courses) ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class mScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final SparseArray<Fragment> instantiatedFragments = new SparseArray<>();
        private List<String> courses;

        public mScreenSlidePagerAdapter(List<String> courses, FragmentManager fm) {
            super(fm);
            this.courses = courses;
        }

        @Override
        public Fragment getItem(int position) {
            return CourseSubjectsFragment.newInstance(position, planSubjectsPerCourseHashMap.get(position));
        }

        @Override
        public int getCount() {
            return planSubjectsPerCourseHashMap.keySet().size();
        }

        @Override
        public Object instantiateItem(final ViewGroup container, final int position) {
            final Fragment fragment = (Fragment) super.instantiateItem(container, position);
            instantiatedFragments.put(position, fragment);
            return fragment;
        }

        @Nullable
        public Fragment getFragment(final int position) {
            final Fragment f = instantiatedFragments.get(position);
            return f;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return courses.get(position);
        }
    }
}
