package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.events;

import java.util.Date;

public class Event {
    private String subject;
    private Date time;
    private String location;
    private int weighing;
    private String agenda;
    private String kind;

    public Event(String subject, Date time, String location, int weighing, String agenda, String kind) {
        this.subject = subject;
        this.time = time;
        this.location = location;
        this.weighing = weighing;
        this.agenda = agenda;
        this.kind = kind;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getWeighing() {
        return weighing;
    }

    public void setWeighing(int weighing) {
        this.weighing = weighing;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
