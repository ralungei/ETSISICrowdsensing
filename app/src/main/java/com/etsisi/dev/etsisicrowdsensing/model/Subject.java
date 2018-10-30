package com.etsisi.dev.etsisicrowdsensing.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Subject implements Parcelable{
    private int id;
    private int course;
    private String title;
    private int credits;
    private ArrayList<String> groups;

    public Subject(int id, String title, int course, int credits, ArrayList<String> groups) {
        this.id = id;
        this.title = title;
        this.course = course;
        this.credits = credits;
        this.groups = groups;
    }


    protected Subject(Parcel in) {
        id = in.readInt();
        title = in.readString();
        course = in.readInt();
        credits = in.readInt();
        groups = in.createStringArrayList();
    }

    public static final Creator<Subject> CREATOR = new Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel in) {
            return new Subject(in);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public int getCourse() {
        return course;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
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
        dest.writeInt(course);
        dest.writeInt(credits);
        dest.writeStringList(groups);
    }
}


