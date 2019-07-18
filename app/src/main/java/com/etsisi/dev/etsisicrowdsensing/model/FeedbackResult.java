package com.etsisi.dev.etsisicrowdsensing.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FeedbackResult {
    @SerializedName("internalId")
    private String internalId;
    @SerializedName("idAsignatura")
    private int subjectId;
    @SerializedName("duracion")
    private int duration;
    @SerializedName("aula")
    private int room;
    @SerializedName("fecha")
    private Date date;
    @SerializedName("votaciones")
    private List<Voting> votingArray;

    public FeedbackResult(int subjectId, int duration, int room, Date date, List<Voting> votingArray){
        this.subjectId = subjectId;
        this.duration = duration;
        this.room = room;
        this.date = date;
        this.votingArray = votingArray;
    }


    public List<Voting> getVotingArray() {
        return votingArray;
    }

    public void setVotingArray(List<Voting> votingArray) {
        this.votingArray = votingArray;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getRoom() {
        return room;
    }

    public void setRoom(int room) {
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
