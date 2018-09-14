package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.notifications;

public class Notification {
    private final int image;
    private final String title;
    private final String description;
    private final String time;

    public Notification(int image, String title, String description, String time){
        this.image = image;
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public int getImage(){
        return image;
    }

    public String getDescription(){
        return description;
    }

    public String getTime(){
        return time;
    }

    public String getTitle() {
        return title;
    }
}
