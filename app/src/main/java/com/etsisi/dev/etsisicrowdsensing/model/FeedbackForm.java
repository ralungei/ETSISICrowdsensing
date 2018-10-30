package com.etsisi.dev.etsisicrowdsensing.model;

import com.etsisi.dev.etsisicrowdsensing.model.Notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class FeedbackForm {

    private Subject subject;
    private Date date;
    private int duration;
    private int room;

    public FeedbackForm(Subject subject, Date date, int duration, int room) {
        this.subject = subject;
        this.date = date;
        this.duration = duration;
        this.room = room;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
