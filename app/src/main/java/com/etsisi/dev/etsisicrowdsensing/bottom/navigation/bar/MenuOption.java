package com.etsisi.dev.etsisicrowdsensing.bottom.navigation.bar;

public class MenuOption {
    private final int name;
    private final int imageResource;
    private final int color;

    public MenuOption(int name, int imageResource, int color) {
        this.name = name;
        this.imageResource = imageResource;
        this.color = color;
    }

    public int getName(){
        return name;
    }
    public int getImageResource(){
        return imageResource;
    }
    public int getColor(){
        return color;
    }


}
