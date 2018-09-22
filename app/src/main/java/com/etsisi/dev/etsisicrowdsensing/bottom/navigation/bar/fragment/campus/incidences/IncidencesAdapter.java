package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;

import java.util.ArrayList;

public class IncidencesAdapter extends RecyclerView.Adapter<IncidencesViewHolder> {

    private final IncidenceItemClickListener incidenceItemClickListener;
    private ArrayList<Incidence> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public IncidencesAdapter(IncidenceItemClickListener incidenceItemClickListener, ArrayList<Incidence> myDataset) {
        this.incidenceItemClickListener = incidenceItemClickListener;
        this.mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public IncidencesViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.incidence_row, parent, false);

        IncidencesViewHolder vh = new IncidencesViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(IncidencesViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Incidence incidenceItem = mDataset.get(position);

        holder.bubbleTitle.setText(incidenceItem.getText());
        holder.bubbleImageView.setImageResource(incidenceItem.getImage());

        //ViewCompat.setTransitionName(holder.bubbleImageView, );


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incidenceItemClickListener.onIncidenceItemClick(holder.getAdapterPosition(), incidenceItem, holder.bubbleImageView);
            }
        });

            /*
            holder.bubbleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext, IncidenceDetailActivity.class);
                    mIntent.putExtra("title", holder.bubbleTitle.getText().toString());
                    mContext.startActivity(mIntent);
                }
            });
            */
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

// Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
class IncidencesViewHolder extends RecyclerView.ViewHolder {
    TextView bubbleTitle;
    ImageView bubbleImageView;
    RelativeLayout bubbleLayout;


    public IncidencesViewHolder(View v) {
        super(v);
        bubbleTitle = (TextView) v.findViewById(R.id.bubbleTitle);
        bubbleImageView = (ImageView) v.findViewById(R.id.bubbleImageView);
        bubbleLayout = (RelativeLayout) v.findViewById(R.id.layout);
    }

}


