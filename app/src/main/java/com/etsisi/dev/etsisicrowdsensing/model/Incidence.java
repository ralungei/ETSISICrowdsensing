package com.etsisi.dev.etsisicrowdsensing.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

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

    @SerializedName("internalId")
    private String internalId;
    @SerializedName("id")
    private String id;
    @SerializedName("userId")
    private String userId;
    @SerializedName("categoria")
    private String category;
    @SerializedName("problema")
    private String problemRoot;
    @SerializedName("lugar")
    private int location;
    @SerializedName("fecha")
    private Date date;
    @SerializedName("estado")
    private int state;

    public Incidence(String id, String userId, String category, String problemRoot, int location, Date date){
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.problemRoot = problemRoot;
        this.location = location;
        this.date = date;
    }

    protected Incidence(Parcel in) {
        id = in.readString();
        userId = in.readString();
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
        dest.writeString(id);
        dest.writeString(userId);
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

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        if(date == null)
            date = new Date();
        return "category:" + category + " problemRoot:" + problemRoot + " location:" + location + " date:" + date.toString() + " state:" + state;
    }

    public String getInternalId() {
        return internalId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
