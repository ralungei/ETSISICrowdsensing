package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsisi.dev.etsisicrowdsensing.R;
import com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events.Event;

import java.util.ArrayList;
import java.util.List;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsViewHolder>{

    private final NotificationItemClickListener notificationItemClickListener;
    private ArrayList<Notification> mDataset;

    public NotificationsAdapter(NotificationItemClickListener notificationItemClickListener, ArrayList<Notification> mDataset){
        this.notificationItemClickListener = notificationItemClickListener;
        this.mDataset = mDataset;
    }


    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
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
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}

class NotificationsViewHolder extends RecyclerView.ViewHolder {

    ImageView notificationIcon;
    TextView notificationTitle;
    TextView notificationDescription;
    TextView notificationTime;

    public NotificationsViewHolder(View itemView) {
        super(itemView);

        // Create references for each individual view created in the XML layout file
        notificationIcon = (ImageView) itemView.findViewById(R.id.notification_icon);
        notificationTitle = (TextView)itemView.findViewById(R.id.notification_title);
        notificationDescription = (TextView)itemView.findViewById(R.id.notification_description);
        notificationTime = (TextView) itemView.findViewById(R.id.notification_time);
    }
}
