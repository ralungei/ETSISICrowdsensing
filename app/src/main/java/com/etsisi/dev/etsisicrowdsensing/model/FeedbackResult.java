package com.etsisi.dev.etsisicrowdsensing.model;

import java.util.ArrayList;

public class FeedbackResult {
    private int feedbackFormId;
    private ArrayList<Voting> votingArray;

    public FeedbackResult(int feedbackFormId, ArrayList<Voting> votingArray){
        this.feedbackFormId = feedbackFormId;
        this.votingArray = votingArray;
    }


    public int getFeedbackFormId() {
        return feedbackFormId;
    }

    public void setFeedbackFormId(int feedbackFormId) {
        this.feedbackFormId = feedbackFormId;
    }

    public ArrayList<Voting> getVotingArray() {
        return votingArray;
    }

    public void setVotingArray(ArrayList<Voting> votingArray) {
        this.votingArray = votingArray;
    }
}
