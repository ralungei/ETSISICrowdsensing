package com.etsisi.dev.etsisicrowdsensing.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

/**
 * {@link #agenda} indicates the event description
 * {@link #kind} indicates the kind of event (deliver|exam|presentation|event)
 *
 * kind values
 * 0 - Schoolwork
 * 1 - Exam
 * 2 - Presentation
 */

@Entity(tableName = "events")
public class Event implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String subject;
    @NonNull
    private Date time;
    @NonNull
    private String location;
    private String agenda;
    @NonNull
    private int kind;


    public Event(int id, String subject, Date time, String location, String agenda, int kind) {
        this.id = id;
        this.subject = subject;
        this.time = time;
        this.location = location;
        this.agenda = agenda;
        this.kind = kind;
    }

    protected Event(Parcel in) {
        id = in.readInt();
        subject = in.readString();
        long tmpDate = in.readLong();
        time = tmpDate == -1 ? null : new Date(tmpDate);
        location = in.readString();
        agenda = in.readString();
        kind = in.readInt();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

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

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(id);
        dest.writeString(subject);
        dest.writeLong(time != null ? time.getTime() : -1);
        dest.writeString(location);
        dest.writeString(agenda);
        dest.writeInt(kind);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
