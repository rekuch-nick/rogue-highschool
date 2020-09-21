package com.rekuchn.service;

import java.util.ArrayList;
import java.util.List;

public class Cords {
    public int x, y;

    public Cords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static int xDir(int x1, int x2){
        return x2 - x1;
    }

    public static int yDir(int y1, int y2){
        return y2 - y1;
    }

    public static List<Cords> adjacent(int x, int y){
        List<Cords> list = new ArrayList<>();
        if(inBounds(x-1, y)){ list.add(new Cords(x-1, y)); }
        if(inBounds(x+1, y)){ list.add(new Cords(x+1, y)); }
        if(inBounds(x, y-1)){ list.add(new Cords(x, y-1)); }
        if(inBounds(x, y+1)){ list.add(new Cords(x, y+1)); }

        return list;
    }

    public static boolean inBounds(int x, int y){
        if(x < 0 || y < 0 || x > 140 || y > 38){ return false; }
        return true;
    }

}
