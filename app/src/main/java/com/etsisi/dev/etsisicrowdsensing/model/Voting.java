package com.etsisi.dev.etsisicrowdsensing.model;

import com.google.gson.annotations.SerializedName;

public class Voting {
    @SerializedName("categoria")
    private String category;
    @SerializedName("puntuacion")
    private int score;
    @SerializedName("razon")
    private String reason;

    public Voting(String category){
        this.category = category;
        this.score = -1;
        this.reason = "";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reasons) {
        this.reason = reasons;
    }
}
