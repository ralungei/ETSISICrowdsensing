package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.model.FeedbackForm;
import com.etsisi.dev.etsisicrowdsensing.model.Notification;
import com.etsisi.dev.etsisicrowdsensing.model.Subject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_FEEDBACK = 0;
    public static final int ITEM_TYPE_TRANSPORT = 1;

    private final NotificationItemClickListener notificationItemClickListener;
    private ArrayList<Object> mDataset;

    public NotificationsAdapter(NotificationItemClickListener notificationItemClickListener, ArrayList<Object> mDataset) {
        this.notificationItemClickListener = notificationItemClickListener;
        this.mDataset = mDataset;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ITEM_TYPE_FEEDBACK) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.feedback_notification_row, parent, false);
            return new FeedbackNotificationViewHolder(view);
        } else if (viewType == ITEM_TYPE_TRANSPORT) {
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.transport_notification_row, parent, false);
            return new TransportNotificationViewHolder(view);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final int itemType = getItemViewType(position);

        /*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationItemClickListener.onNotificationItemClick(holder.getAdapterPosition(), new Notification(1, "b", "22:00", "b"));
            }
        });
        */

        if (itemType == ITEM_TYPE_FEEDBACK) {
            ((FeedbackNotificationViewHolder) holder).bindData((FeedbackForm)mDataset.get(position));
            Button btn = ((FeedbackNotificationViewHolder) holder).getBtn();
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationItemClickListener.onFeedbackNotificationItemClick(holder.getAdapterPosition(), (FeedbackForm) mDataset.get(position));
                }
            });
        } else if (itemType == ITEM_TYPE_TRANSPORT) {
            //((TransportNotificationViewHolder) holder).bindData((MyModel) myData[position]);

        }
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        /*
        final Notification notification = mDataset.get(position);

        holder.notificationIcon.setImageResource(notification.getImage());
        holder.notificationTitle.setText(notification.getTitle());
        holder.notificationDescription.setText(notification.getDescription());
        holder.notificationTime.setText(notification.getTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notificationItemClickListener.onNotificationItemClick(holder.getAdapterPosition(), notification);
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mDataset.get(position) instanceof FeedbackForm) {
            return ITEM_TYPE_FEEDBACK;
        } else {
            return ITEM_TYPE_TRANSPORT;
        }
    }
}


class FeedbackNotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView descriptionTextView;
    private TextView timeTextView;
    private Button fillFormBtn;

    public FeedbackNotificationViewHolder(View view) {
        super(view);
        descriptionTextView = (TextView) view.findViewById(R.id.notification_description);
        timeTextView = (TextView) view.findViewById(R.id.notification_time);
        fillFormBtn = (Button) view.findViewById(R.id.button);

    }

    public void bindData(FeedbackForm model) {
        descriptionTextView.setText("Por favor, cuéntanos tu experiencia en la clase " + model.getSubject().getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = model.getDate();
        int duration = model.getDuration();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR, duration);
        Date endDate = calendar.getTime();
        String startTime = dateFormat.format(startDate.getTime());
        String endTime = dateFormat.format(endDate.getTime());

        timeTextView.setText(startTime + " - " + endTime);
    }

    public Button getBtn(){
        return fillFormBtn;
    }

}


class TransportNotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView titleLabel;
    private TextView descriptionLabel;

    public TransportNotificationViewHolder(View view) {
        super(view);

        //titleLabel = (TextView) view.findViewById(R.id.titleLabel);
        //descriptionLabel = (TextView) view.findViewById(R.id.descriptionLabel);
    }

    public void bindData(Subject model) {
        //titleLabel.setText(model.getTitle());
        //descriptionLabel.setText(model.getDescription());
    }
}



