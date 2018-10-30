package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;

import java.util.ArrayList;


public class FeedbackBlocksAdapter extends RecyclerView.Adapter<FeedbackBlocksViewHolder>{

    private ArrayList<String> mTopics;

    public FeedbackBlocksAdapter(ArrayList<String> mTopics){
        this.mTopics = mTopics;
    }


    @NonNull
    @Override
    public FeedbackBlocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.feedback_row, parent, false);

        return new FeedbackBlocksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackBlocksViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.topic.setText(mTopics.get(position));




        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(holder.ratingDisplayTextView.getVisibility() == View.GONE)
                    holder.ratingDisplayTextView.setVisibility(View.VISIBLE);

                switch(Math.round(rating)) {
                    case 1:
                        holder.ratingDisplayTextView.setText("Muy malo");
                        break;
                    case 2:
                        holder.ratingDisplayTextView.setText("Malo");
                        break;
                    case 3:
                        holder.ratingDisplayTextView.setText("Aceptable");
                        break;
                    case 4:
                        holder.ratingDisplayTextView.setText("Bueno");
                        break;
                    case 5:
                        holder.ratingDisplayTextView.setText("Excelente");
                        break;
                    default:
                        holder.ratingDisplayTextView.setText("Error");
                        break;
                }

            }
        });

        /*
        holder.happyIcon.setImageResource(R.drawable.smile_icon);
        holder.neutIcon.setImageResource(R.drawable.neutral_icon);
        holder.sadIcon.setImageResource(R.drawable.sad_icon);
        */

        /* DISCOMMENT
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setActivated(!v.isActivated());
            }
        };

        holder.happyIcon.setOnClickListener(listener);

        holder.neutIcon.setOnClickListener(listener);

        holder.sadIcon.setOnClickListener(listener);
    */
    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }


}

class FeedbackBlocksViewHolder extends RecyclerView.ViewHolder {

    TextView topic;

    RatingBar ratingBar;
    TextView ratingDisplayTextView;

    public FeedbackBlocksViewHolder(View itemView) {
        super(itemView);

        // Create references for each individual view created in the XML layout file
        topic = (TextView) itemView.findViewById(R.id.topic);
        //happyIcon = (ImageView) itemView.findViewById(R.id.happyIcon);
        //neutIcon = (ImageView)itemView.findViewById(R.id.neutIcon);
        //sadIcon = (ImageView) itemView.findViewById(R.id.sadIcon);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        ratingDisplayTextView = (TextView) itemView.findViewById(R.id.ratingDisplayTextView);
    }
}
