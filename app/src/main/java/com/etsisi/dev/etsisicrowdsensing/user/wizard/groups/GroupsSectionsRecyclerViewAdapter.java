package com.etsisi.dev.etsisicrowdsensing.user.wizard.groups;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;
import com.etsisi.dev.etsisicrowdsensing.model.UserSubject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * {@link RecyclerView.Adapter} that can display a Section and makes a call to the
 * specified {@link OnSectionFragmentInteractionListener}.
 */
public class GroupsSectionsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsSectionsRecyclerViewAdapter.SectionsViewHolder> implements GroupsSubjectsRecyclerViewAdapter.OnPositionClickListener {

    /**
     * As RecyclerView works iterating data sequentially
     * and the data we receive in a HashMap is not
     * sequential. We must get the HashMap positions sequentially
     * {@link #selectedSubjectsHashMapPositions}
     * {@link #setSelectedSubjectsHashMapPositions()} ()}
     */

    private Context context;

    private final ArrayList<String> coursesTitles;

    private HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap;

    private ArrayList<Integer> selectedSubjectsHashMapPositions;

    private OnSectionFragmentInteractionListener mListener;

    public GroupsSectionsRecyclerViewAdapter(Context context, ArrayList<String> coursesTitles, HashMap<Integer, ArrayList<Subject>> selectedSubjectsPerCourseHashMap, OnSectionFragmentInteractionListener listener) {
        this.context = context;
        this.coursesTitles = coursesTitles;
        this.selectedSubjectsPerCourseHashMap = selectedSubjectsPerCourseHashMap;
        this.mListener = listener;
        this.selectedSubjectsHashMapPositions = new ArrayList<>();
        setSelectedSubjectsHashMapPositions();

    }

    @Override
    public SectionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groups_section_row, parent, false);
        return new SectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SectionsViewHolder holder, int position) {
        int hashMapPosition = selectedSubjectsHashMapPositions.get(position);

        final ArrayList<Subject> courseSubjectsArray = selectedSubjectsPerCourseHashMap.get(hashMapPosition);

        holder.sectionLabel.setText(coursesTitles.get(selectedSubjectsHashMapPositions.get(position)));

        GroupsSubjectsRecyclerViewAdapter groupsSubjectsRecyclerViewAdapter = new GroupsSubjectsRecyclerViewAdapter(context, hashMapPosition, courseSubjectsArray, this);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        holder.subjectsRecyclerView.addItemDecoration(itemDecor);
        holder.subjectsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.subjectsRecyclerView.setAdapter(groupsSubjectsRecyclerViewAdapter);
    }

    @Override
    public int getItemCount() {
        return selectedSubjectsPerCourseHashMap.keySet().size();
    }


    public void setSelectedSubjectsHashMapPositions() {
        for (Integer key : selectedSubjectsPerCourseHashMap.keySet()) {
            selectedSubjectsHashMapPositions.add(key);
        }

    }


    @Override
    public void itemClicked(int course, int subjectPosition, int groupPosition) {
        Subject subject = selectedSubjectsPerCourseHashMap.get(course).get(subjectPosition);
        int subjectId = subject.getId();
        String group = subject.getGroups().get(groupPosition);
        //Toast.makeText(context, "For subject ->" + subject.getTitle() + " group is -> " + group, Toast.LENGTH_SHORT).show();
        mListener.onSectionFragmentInteraction(subjectId, group);

    }

    public class SectionsViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionLabel;
        private RecyclerView subjectsRecyclerView;


        public SectionsViewHolder(View view) {
            super(view);
            sectionLabel = (TextView) view.findViewById(R.id.section_label);
            subjectsRecyclerView = (RecyclerView) view.findViewById(R.id.subjects_recycler_view);
        }

    }


    /**
     * This interface must be implemented to respond to recycler view
     * item clicks from fragment
     */
    public interface OnSectionFragmentInteractionListener {
        void onSectionFragmentInteraction(int subjectId, String group);
    }
}
