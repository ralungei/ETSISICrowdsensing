package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus.incidences;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;

public class Incidence implements Parcelable{

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

    private int image;
    private String text;
    private String date;
    private String description;

    public Incidence(int image, String text){
        this.image = image;
        this.text = text;
        this.date = date;
        this.description = description;
    }

    protected Incidence(Parcel in) {
        image = in.readInt();
        text = in.readString();
        date = in.readString();
        description = in.readString();
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(image);
        dest.writeString(text);
        dest.writeString(date);
        dest.writeString(description);
    }
}
