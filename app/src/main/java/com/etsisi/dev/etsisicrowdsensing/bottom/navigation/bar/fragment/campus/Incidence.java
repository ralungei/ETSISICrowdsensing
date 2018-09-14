package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar.fragment.campus;

import android.widget.ImageView;
import android.widget.TextView;

public class Incidence {
    private int image;
    private String text;
    private String date;
    private String description;

    public Incidence(int image, String text){
        this.image = image;
        this.text = text;
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
}
