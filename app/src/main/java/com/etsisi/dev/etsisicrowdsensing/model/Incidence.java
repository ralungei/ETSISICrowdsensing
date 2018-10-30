package com.etsisi.dev.etsisicrowdsensing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Incidence implements Parcelable{

    /**
     * {@link #state}
     * SENT STATE       0
     * PROCESSED STATE  1
     */

    public static final Creator<Incidence> CREATOR = new Creator<Incidence>() {
        @Override
        public Incidence createFromParcel(Parcel in) {
            return new Incidence(in);
        }

        @Override
        public Incidence[] newArray(int size) {
            return new Incidence[size];
        }
    };

    private String category;
    private String problemRoot;
    private int location;
    private Date date;
    private int state = 0;

    public Incidence(String category, String problemRoot, int location, Date date){
        this.category = category;
        this.problemRoot = problemRoot;
        this.location = location;
        this.date = date;
    }

    protected Incidence(Parcel in) {
        category = in.readString();
        problemRoot = in.readString();
        location = in.readInt();
        long tmpDate = in.readLong();
        date = tmpDate == -1 ? null : new Date(tmpDate);
        state = in.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(problemRoot);
        dest.writeInt(location);
        dest.writeLong(date != null ? date.getTime() : -1);
        dest.writeInt(state);
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public String getProblemRoot() {
        return problemRoot;
    }

    public void setProblemRoot(String problemRoot) {
        this.problemRoot = problemRoot;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getState(){
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "category:" + category + " problemRoot:" + problemRoot + " location:" + location + " date:" + date.toString() + " state:" + state;
    }
}
