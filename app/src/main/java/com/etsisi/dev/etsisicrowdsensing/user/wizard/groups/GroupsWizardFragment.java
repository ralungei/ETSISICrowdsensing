package com.etsisi.dev.etsisicrowdsensing.user.wizard.groups;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;
import com.etsisi.dev.etsisicrowdsensing.UserScheduleActivity;
import com.etsisi.dev.etsisicrowdsensing.model.UserSubject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupsWizardFragment.OnGroupsSubmittedListener} interface
 * to handle interaction events.
 * Use the {@link GroupsWizardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupsWizardFragment extends Fragment implements GroupsSectionsRecyclerViewAdapter.OnSectionFragmentInteractionListener {

    private static String TAG = "GroupsWizardFragment";

    private static final String ARG_COURSES_TITLES = "courses-titles";

    private ArrayList<String> coursesTitles;

    /**
     * The HashMap with the user selected subjects of each course.
     */
    private HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap = new HashMap<>();

    /**
     * The SparseArray with the selected subjects ids and the selected course for each subject
     */
    private SparseArray<String>  userSelectedSubjectsAndGroupsSparseArray = new SparseArray<>();

    /**
     * The {@link UserSubject} ArrayList formed from {@link #userSelectedSubjectsAndGroupsSparseArray}
     * to be sent to Activity
     */
    private ArrayList<UserSubject> userSubjectsArrayList = new ArrayList<>();

    private GroupsSectionsRecyclerViewAdapter adapter;

    private OnGroupsSubmittedListener mListener;

    private RecyclerView recyclerView;

    public GroupsWizardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GroupsWizardFragment.
     */

    public static GroupsWizardFragment newInstance(ArrayList<String> coursesTitles) {
        GroupsWizardFragment fragment = new GroupsWizardFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_COURSES_TITLES, coursesTitles);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coursesTitles = getArguments().getStringArrayList(ARG_COURSES_TITLES);
        }
        loadUserSelectedSubjectsAndGroupsSparseArray();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_groups_wizard, container, false);

        Context context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        adapter = new GroupsSectionsRecyclerViewAdapter(getContext(), coursesTitles, selectedSubjectsPerCourseHashMap, this);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button nextButton = getView().findViewById(R.id.endButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserObjects(userSelectedSubjectsAndGroupsSparseArray);
                mListener.onGroupsSubmitted(userSubjectsArrayList);
                ((UserScheduleActivity) getActivity()).onFinishPressed();
            }
        });

        Button backButton = getView().findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Map.Entry<Integer, ArrayList<Subject>> entry : selectedSubjectsPerCourseHashMap.entrySet()) {
                    String subjects = "";
                    for(Subject s : entry.getValue()){
                        subjects += s.getTitle() + ",";
                    }

                    Log.d(TAG, "Course " + entry.getKey() + " subjects are -> " + subjects);
                }

                ((UserScheduleActivity) getActivity()).onBackPressed();
            }
        });

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnGroupsSubmittedListener) {
            mListener = (OnGroupsSubmittedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSectionFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setSelectedSubjectsPerCourseHashMap(HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap) {
        this.selectedSubjectsPerCourseHashMap = selectedSubjectsPerCourseHashMap;

        adapter = null;
        adapter = new GroupsSectionsRecyclerViewAdapter(getContext(), coursesTitles, selectedSubjectsPerCourseHashMap, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSectionFragmentInteraction(int subjectId, String group) {
        userSelectedSubjectsAndGroupsSparseArray.put(subjectId, group);
    }

    /**
     * Load the selected subjects and selected course
     */
    public void loadUserSelectedSubjectsAndGroupsSparseArray(){
        for(ArrayList<Subject> subjects: selectedSubjectsPerCourseHashMap.values())
            for(Subject subject: subjects)
                userSelectedSubjectsAndGroupsSparseArray.put(subject.getId(), subject.getGroups().get(0));
    }

    /**
     * Create {@link UserSubject} objects and fill {link #userSubjectsArrayList}
     * from {@link #userSelectedSubjectsAndGroupsSparseArray}
     */
    public void createUserObjects(SparseArray<String> subjectsAndGroupsSparseArray){
        for(int i = 0; i < subjectsAndGroupsSparseArray.size(); i++) {
            int key = subjectsAndGroupsSparseArray.keyAt(i);
            String course = subjectsAndGroupsSparseArray.get(key);
            UserSubject userSubject = new UserSubject(key, course);
            userSubjectsArrayList.add(userSubject);
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
    public interface OnGroupsSubmittedListener {
        void onGroupsSubmitted(ArrayList<UserSubject> userSubjectsArrayList);
    }
}
