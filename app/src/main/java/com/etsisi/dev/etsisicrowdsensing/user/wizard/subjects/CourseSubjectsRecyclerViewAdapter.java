package com.etsisi.dev.etsisicrowdsensing.user.wizard.subjects;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a Subject and makes a call to the
 * specified {@link }.
 */
public class CourseSubjectsRecyclerViewAdapter extends RecyclerView.Adapter<CourseSubjectsRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Subject> subjectsArray;
    // Array of booleans to store switches status
    public boolean[] statusArray;
    public final OnPositionClickListener onPositionClickListener;

    public CourseSubjectsRecyclerViewAdapter(ArrayList<Subject> items, OnPositionClickListener onPositionClickListener, boolean[] statusArray) {
        subjectsArray = items;
        this.onPositionClickListener = onPositionClickListener;
        this.statusArray = statusArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_coursesubjects_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mSubject = subjectsArray.get(position);
        holder.mSubjectName.setText(subjectsArray.get(position).getTitle());
        holder.mSwitch.setChecked(statusArray[position]);

        holder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onPositionClickListener.itemClicked(position, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return subjectsArray.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSubjectName;
        public final Switch mSwitch;
        public Subject mSubject;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mSubjectName = (TextView) view.findViewById(R.id.subjectName);
            mSwitch = (Switch) view.findViewById(R.id.simpleSwitch);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSubjectName.getText() + "'";
        }
    }

    /**
     * This interface must be implemented to respond to recycler view
     * item clicks from fragment
     */
    public interface OnPositionClickListener {
        void itemClicked(int position, boolean isChecked);
    }

}
