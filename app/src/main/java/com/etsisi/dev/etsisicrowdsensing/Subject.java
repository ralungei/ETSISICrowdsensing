package com.etsisi.dev.etsisicrowdsensing;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Subject {

    private String name;
    private int curso;
    private ArrayList<String> grupos;

    public Subject() {}

    public Subject(String name, int curso, ArrayList<String> grupos) {
        this.name = name;
        this.curso = curso;
        this.grupos = grupos;
    }


    public String getName() {
        return name;
    }

    public int getCurso() {
        return curso;
    }

    public ArrayList<String> getGrupos() {
        return grupos;
    }
}
