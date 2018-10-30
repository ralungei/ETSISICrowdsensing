package com.etsisi.dev.etsisicrowdsensing.user.wizard.groups;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Subject} and makes a call to the
 * specified {@link OnPositionClickListener}.
 */
public class GroupsSubjectsRecyclerViewAdapter extends RecyclerView.Adapter<GroupsSubjectsRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Subject> selectedSubjects;

    private int subjectsCourse;

    private final OnPositionClickListener mListener;

    private Context context;

    public GroupsSubjectsRecyclerViewAdapter(Context context, int subjectsCourse, ArrayList<Subject> selectedSubjects, OnPositionClickListener listener) {
        this.context = context;
        this.subjectsCourse = subjectsCourse;
        this.selectedSubjects = selectedSubjects;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_groups_subject_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.subjectName.setText(selectedSubjects.get(position).getTitle());
        ArrayList<String> mArray = selectedSubjects.get(position).getGroups();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mArray);

        holder.spinner.setAdapter(adapter);

        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(context, "Subject position is ->" + holder.getAdapterPosition() + " and group position is -> " + position, Toast.LENGTH_SHORT).show();

                mListener.itemClicked(subjectsCourse, holder.getAdapterPosition(), position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return selectedSubjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView subjectName;
        public final Spinner spinner;

        public ViewHolder(View view) {
            super(view);
            subjectName = (TextView) view.findViewById(R.id.subjectName);
            spinner = (Spinner) view.findViewById(R.id.spinner);


        }
    }

    /**
     * This interface must be implemented to respond to recycler view
     * item clicks from fragment
     */
    public interface OnPositionClickListener {
        void itemClicked(int course, int subjectPosition, int groupPosition);
    }
}
