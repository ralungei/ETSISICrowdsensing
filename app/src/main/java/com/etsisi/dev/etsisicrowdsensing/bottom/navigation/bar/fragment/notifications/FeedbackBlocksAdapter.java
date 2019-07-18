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
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.List;


public class FeedbackBlocksAdapter extends RecyclerView.Adapter<FeedbackBlocksViewHolder>{

    private List<String> mTopics;
    private final FeedbackStateClickListener feedbackStateClickListener;


    public FeedbackBlocksAdapter(List<String> mTopics, FeedbackStateClickListener feedbackStateClickListener){
        this.mTopics = mTopics;
        this.feedbackStateClickListener = feedbackStateClickListener;
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
        String topicTitle = mTopics.get(position);
        holder.topic.setText(topicTitle);



        holder.smileRatingBar.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                feedbackStateClickListener.onFeedbackStateItemClick(topicTitle, level);
            }
        });



    }

    @Override
    public int getItemCount() {
        return mTopics.size();
    }

    interface FeedbackStateClickListener {
        void onFeedbackStateItemClick(String topic, int level);

    }

}

class FeedbackBlocksViewHolder extends RecyclerView.ViewHolder {

    TextView topic;
    SmileRating smileRatingBar;

    public FeedbackBlocksViewHolder(View itemView) {
        super(itemView);

        // Create references for each individual view created in the XML layout file
        topic = (TextView) itemView.findViewById(R.id.topic);
        //happyIcon = (ImageView) itemView.findViewById(R.id.happyIcon);
        //neutIcon = (ImageView)itemView.findViewById(R.id.neutIcon);
        //sadIcon = (ImageView) itemView.findViewById(R.id.sadIcon);
        smileRatingBar = (SmileRating) itemView.findViewById(R.id.ratingBar);
    }
}



