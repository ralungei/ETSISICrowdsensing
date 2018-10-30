package com.etsisi.dev.etsisicrowdsensing.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Plan implements Parcelable{

    private int id;
    private String title;
    private int year;

    public Plan(int id, String title, int año){
        this.id = id;
        this.title = title;
        this.year = año;
    }

    protected Plan(Parcel in) {
        id = in.readInt();
        title = in.readString();
        year = in.readInt();
    }

    public static final Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeInt(year);
    }
}
