package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

public class FeedbackForm extends Notification{

    private String subject;

    public FeedbackForm(int image, String title, String description, String time, String subject) {
        super(image, title, description, time);
        this.subject = subject;
    }
}
