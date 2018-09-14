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

import java.util.List;


public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>{
    private Context mContext;
    private List<Notification> notificationsData;

    public NotificationsAdapter(Context context, List<Notification> notificationsData){
        this.mContext = context;
        this.notificationsData = notificationsData;
    }


    @NonNull
    @Override
    public NotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row,
                parent, false);
        return new NotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsViewHolder holder, int position) {
        holder.notificationIcon.setImageResource(notificationsData.get(position).getImage());
        holder.notificationTitle.setText(notificationsData.get(position).getTitle());
        holder.notificationDescription.setText(notificationsData.get(position).getDescription());
        holder.notificationTime.setText(notificationsData.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return notificationsData.size();
    }

    public static class NotificationsViewHolder extends RecyclerView.ViewHolder {

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
}


