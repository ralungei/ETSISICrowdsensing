package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Event;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private Context mContext;
    private final EventItemClickListener eventItemClickListener;
    private ArrayList<Event> mDataset;

    public EventsAdapter(Context context, EventItemClickListener eventItemClickListener, ArrayList<Event> myDataset) {
        this.eventItemClickListener = eventItemClickListener;
        this.mDataset = myDataset;
        this.mContext = context;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);

        return new EventsViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Event eventItem = mDataset.get(position);

        //holder.dateView.setText(eventItem);
        //holder.timeView.setText(eventItem.getTime());
        holder.subjectView.setText(eventItem.getSubject());
        holder.locationView.setText(eventItem.getLocation());

        holder.kindView.setText(eventItem.getKind());

        switch(eventItem.getKind()){
            case "EXAMEN":
                holder.kindView.setTextColor(mContext.getResources().getColor(R.color.red));
                break;
            case "ENTREGA":
                holder.kindView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventItemClickListener.onEventItemClick(holder.getAdapterPosition(), eventItem);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);

    }

    public void restoreItem(Event event, int position) {
        mDataset.add(position, event);
        // notify item added by position
        notifyItemInserted(position);
    }


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class EventsViewHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        TextView timeView;
        TextView subjectView;
        TextView locationView;
        TextView kindView;

        public EventsViewHolder(View v) {
            super(v);

            dateView = (TextView) v.findViewById(R.id.dateView);
            timeView = (TextView) v.findViewById(R.id.timeView);
            subjectView = (TextView) v.findViewById(R.id.subjectView);
            locationView = (TextView) v.findViewById(R.id.descriptionView);
            kindView = (TextView) v.findViewById(R.id.kindView);

        }

    }
}




