package com.etsisi.dev.etsisicrowdsensing.model;

public class Notification {
    private final String title;
    private final String description;
    private final String time;

    public Notification(int image, String title, String description, String time){
        this.title = title;
        this.description = description;
        this.time = time;
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
