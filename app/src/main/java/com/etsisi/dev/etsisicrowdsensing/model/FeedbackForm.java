package com.etsisi.dev.etsisicrowdsensing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.etsisi.dev.etsisicrowdsensing.model.Notification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class FeedbackForm implements Parcelable {

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

    protected FeedbackForm(Parcel in) {
        subject = in.readParcelable(Subject.class.getClassLoader());
        duration = in.readInt();
        room = in.readInt();
        long tmpDate = in.readLong();
        date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<FeedbackForm> CREATOR = new Creator<FeedbackForm>() {
        @Override
        public FeedbackForm createFromParcel(Parcel in) {
            return new FeedbackForm(in);
        }

        @Override
        public FeedbackForm[] newArray(int size) {
            return new FeedbackForm[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(subject, i);
        parcel.writeInt(duration);
        parcel.writeInt(room);
        parcel.writeLong(date != null ? date.getTime() : -1);
    }
}
