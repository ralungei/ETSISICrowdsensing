package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

public class FeedbackNotification extends Notification{

    private String subject;

    public FeedbackNotification(int image, String title, String description, String time, String subject) {
        super(image, title, description, time);
        this.subject = subject;
    }
}
