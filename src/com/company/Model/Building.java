package com.company.Model;

/**
 * Created by Mauricio on 11/8/2015.
 */
public class Building {
    private final Integer x;
    private final Integer y;
    private final String name;

    public Building(String name, int x, int y) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
    public int getX(){
        return this.x;
    }
    public int getY(){
        return this.y;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
