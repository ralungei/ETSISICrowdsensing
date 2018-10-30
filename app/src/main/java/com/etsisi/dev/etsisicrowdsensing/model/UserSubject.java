package com.etsisi.dev.etsisicrowdsensing.model;

public class UserSubject {
    private int subjectId;
    private String course;

    public UserSubject(int subjectId, String course) {
        this.subjectId = subjectId;
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }
}
