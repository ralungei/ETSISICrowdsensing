package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private Context mContext;
    private final EventItemClickListener eventItemClickListener;
    private List<Event> mDataset;

    /**
     * Kind of event values
     * Entrega
     * Examen
     * Presentacion
     */
    private final int ENTREGA_KIND = 0;
    private final int EXAMEN_KIND = 1;
    private final int PRESENTACION_KIND = 2;

    public EventsAdapter(Context context, EventItemClickListener eventItemClickListener) {
        this.eventItemClickListener = eventItemClickListener;
        this.mContext = context;
    }


    public void setEvents(List<Event> events) {
        mDataset = events;
        notifyDataSetChanged();
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

        if(mDataset != null) {

            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            final Event eventItem = mDataset.get(position);

            //holder.dateView.setText(eventItem);
            //holder.timeView.setText(eventItem.getTime());
            holder.subjectView.setText(eventItem.getSubject());
            holder.locationView.setText(eventItem.getLocation());

            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String formattedDate = formatter.format(eventItem.getTime());
            String[] dateParts = formattedDate.split(" ");
            if (dateParts.length == 2) {
                holder.dateView.setText(dateParts[0]);
                holder.timeView.setText(dateParts[1]);
            }

            Calendar cal = Calendar.getInstance();
            Date currentDate = cal.getTime();
            if (DateUtils.isToday(eventItem.getTime().getTime())) {
                holder.dateView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_rectangle));
                holder.dateView.setTextColor(mContext.getResources().getColor(R.color.white));
                holder.timeView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            switch (eventItem.getKind()) {
                case EXAMEN_KIND:
                    holder.kindView.setText(R.string.exam_label);
                    holder.separatorView.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                    break;
                case ENTREGA_KIND:
                    holder.kindView.setText(R.string.schoolwork_label);
                    holder.separatorView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                    break;
                case PRESENTACION_KIND:
                    holder.kindView.setText(R.string.presentation_label);
                    holder.separatorView.setBackgroundColor(mContext.getResources().getColor(R.color.orange));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eventItemClickListener.onEventItemClick(holder.getAdapterPosition(), eventItem);
                }
            });
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // mDataset has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else return 0;
    }

    public Event getEvent(int position) {
        return mDataset.get(position);
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
        View separatorView;

        public EventsViewHolder(View v) {
            super(v);

            dateView = (TextView) v.findViewById(R.id.dateView);
            timeView = (TextView) v.findViewById(R.id.timeTextView);
            subjectView = (TextView) v.findViewById(R.id.subjectView);
            locationView = (TextView) v.findViewById(R.id.subjectTextView);
            kindView = (TextView) v.findViewById(R.id.kindView);
            separatorView = (View) v.findViewById(R.id.separator_left);

        }

    }
}




