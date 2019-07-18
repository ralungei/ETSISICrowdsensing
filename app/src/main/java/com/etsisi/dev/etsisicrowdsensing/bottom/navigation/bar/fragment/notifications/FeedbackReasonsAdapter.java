package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.EventItemClickListener;
import com.etsisi.dev.etsisicrowdsensing.model.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeedbackReasonsAdapter extends RecyclerView.Adapter<FeedbackReasonsAdapter.ReasonsViewHolder> {

    private static final String TAG = "FeedbackReasonsAdapter";
    private Context mContext;
    private final ReasonItemClickListener reasonItemClickListener;
    private List<String> reasonsArray;



    public FeedbackReasonsAdapter(Context mContext, ReasonItemClickListener reasonItemClickListener, List<String> reasonsArray) {
        this.mContext = mContext;
        this.reasonItemClickListener = reasonItemClickListener;
        this.reasonsArray = reasonsArray;
    }



    // Create new views (invoked by the layout manager)
    @Override
    public ReasonsViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.reasons_row_test, parent, false);

        return new ReasonsViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ReasonsViewHolder holder, int position) {

        if(reasonsArray != null) {

            String reasonTitle = reasonsArray.get(position);

            holder.reasonView.setText(reasonTitle);
            // TODO Link imageview

            reasonTitle = reasonTitle.toLowerCase();
            reasonTitle = reasonTitle.replaceAll("\\s", "");
            reasonTitle = reasonTitle.replaceAll("á", "a");
            reasonTitle = reasonTitle.replaceAll("é", "e");
            reasonTitle = reasonTitle.replaceAll("í", "i");
            reasonTitle = reasonTitle.replaceAll("ó", "o");
            reasonTitle = reasonTitle.replaceAll("ú", "u");

            reasonTitle += "_icon";

            Log.d(TAG, "Looking for drawable with name: " + reasonTitle);

            int drawableId = mContext.getResources().getIdentifier(reasonTitle, "drawable", mContext.getPackageName());

            Drawable imageDrawable;

            if(drawableId != 0) {
                //imageDrawable = mContext.getResources().getDrawable(drawableId);

                // TODO Call Glide
                loadImageUsingGlide(drawableId, holder.imageView);
                //holder.imageView.setImageDrawable(imageDrawable);
            }
            else
                Log.i(TAG, "Can't fin drawable with name: " + reasonTitle);



            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reasonItemClickListener.onReasonItemClick(position);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return reasonsArray.size();
    }

    public interface ReasonItemClickListener {
        void onReasonItemClick(int pos);

    }

    // Load Logo ImageView using Glide Library
    public void loadImageUsingGlide(int drawableId, ImageView imageView) {
        Glide
                .with(mContext)
                .load(drawableId)
                .into(imageView);
    }



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ReasonsViewHolder extends RecyclerView.ViewHolder {
        TextView reasonView;
        ImageView imageView;

        public ReasonsViewHolder(View v) {
            super(v);

            reasonView = (TextView) v.findViewById(R.id.reason_label);
            imageView = (ImageView) v.findViewById(R.id.imageView);

        }

    }
}




