package com.rekuchn.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Line implements Iterable<Cords> {
    private List<Cords> points;
    public List<Cords> getPoints() { return points; }

    public Line(int x0, int y0, int x1, int y1) {
        points = new ArrayList<Cords>();

        int dx = Math.abs(x1-x0);
        int dy = Math.abs(y1-y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx-dy;

        while (true){
            points.add(new Cords(x0, y0));

            if (x0==x1 && y0==y1)
                break;

            int e2 = err * 2;
            if (e2 > -dx) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx){
                err += dx;
                y0 += sy;
            }
        }
    }

    public Line(List<Cords> list){
        points = new ArrayList<Cords>();

        for(Cords c : list){
            points.add(c);
        }
    }

    public Line reverse(){
        int x0 = points.get(0).x;
        int y0 = points.get(0).y;
        int x1 = points.get(points.size() - 1).x;
        int y1 = points.get(points.size() - 1).y;
        return new Line(x1, y1, x0, y0);
    }

    @Override
    public Iterator<Cords> iterator() {
        return points.iterator();
    }



}
