package com.etsisi.dev.etsisicrowdsensing.model;

public class Voting {
    private String category;
    private String result;
    private String[] reasons;

    public Voting(String category, String result, String[] reasons){
        this.category = category;
        this.result = result;
        this.reasons = reasons;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String[] getReasons() {
        return reasons;
    }

    public void setReasons(String[] reasons) {
        this.reasons = reasons;
    }
}
