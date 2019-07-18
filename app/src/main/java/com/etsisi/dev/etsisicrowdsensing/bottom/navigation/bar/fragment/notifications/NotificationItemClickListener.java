package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

import com.etsisi.dev.etsisicrowdsensing.model.FeedbackForm;

public interface NotificationItemClickListener {
    void onFeedbackNotificationItemClick(int pos, FeedbackForm notification);
}
