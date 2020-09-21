package com.rekuchn.service;

import com.rekuchn.World;
import com.rekuchn.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class Pathing {

    public static boolean avoidCreatures = true;
    public static boolean ignoreDoors = false;
    public static boolean isPlayer = false;


    public static List<Cords> find(int x1, int y1, int x2, int y2, World world){

        if(world.creature(x1, y1) != null && world.creature(x1, y1).isPlayer){ isPlayer = true; }

        Status[][] mapStatus = new Status[world.width()][world.height()];
        Cords[][] parent = new Cords[world.width()][world.height()];
        int[][] knownDistance = new int[world.width()][world.height()];
        int[][] estimatedDistance = new int[world.width()][world.height()];

        String done = "";
        int bestX, bestY, bestDistance;
        int cx, cy, tx=0, ty=0;

        for(int x=0; x<world.width(); x++){ for(int y=0; y<world.height(); y++){
            mapStatus[x][y] = Status.UNTESTED;
            knownDistance[x][y] = 0;
            parent[x][y] = new Cords(-1, -1);
        }}

        mapStatus[x2][y2] = Status.OPEN;
        knownDistance[x2][y2] = 0;
        estimatedDistance[x2][y2] = estimateDistance(x1, y1, x2, y2);


        while(done.equals("")){

            bestDistance = Integer.MAX_VALUE;
            bestX = -1; bestY = -1;
            for(int x=0; x<world.width(); x++){ for(int y=0; y<world.height(); y++){
                if(mapStatus[x][y] != Status.OPEN){ continue; }
                int dis = knownDistance[x][y] + estimatedDistance[x][y];
                if(dis < bestDistance){
                    bestX = x; bestY = y; bestDistance = dis;
                }
            }}
            cx = bestX; cy = bestY;
            if(bestX == -1){ done = "no path"; }
            if(cx == x1 && cy == y1){ done = "found path"; }


            if(!done.equals("")){ break; }
            mapStatus[cx][cy] = Status.CLOSED;

            for(int dir=1; dir<5; dir ++) {
                if(dir == 1){ tx = cx; ty = cy - 1; }
                if(dir == 2){ tx = cx + 1; ty = cy; }
                if(dir == 3){ tx = cx; ty = cy + 1; }
                if(dir == 4){ tx = cx - 1; ty = cy; }


                if(tx < 0 || ty < 0 || tx >= world.width() || ty >= world.height()){ continue; }
                if(tx != x1 || ty != y1){ // you can always leave your starting tile
                    if(!world.tile(tx, ty).canEnter(world.creature(x1, y1))){ continue; }
                    if(world.tile(tx, ty) == Tile.DOOR && !ignoreDoors){ continue; }
                    if(isPlayer && world.tile(tx, ty) == Tile.LAVA){ continue; }
                    if(isPlayer && world.tile(tx, ty) == Tile.SPIKE){ continue; }
                }


                if(avoidCreatures && world.creature(tx, ty) != null){
                    if(isPlayer && !world.creature(tx, ty).hostile){
                        // allow player to change places with allies
                    } else {
                        if((x1 != tx || y1 != ty) && (x2 != tx || y2 != ty)){ continue; }
                    }
                }

                if(mapStatus[tx][ty] == Status.CLOSED){
                    if(knownDistance[tx][ty] > knownDistance[cx][cy] + 1){
                        knownDistance[tx][ty] = knownDistance[cx][cy] + 1;
                        parent[tx][ty].x = cx; parent[tx][ty].y = cy;
                        mapStatus[tx][ty] = Status.OPEN;
                    }
                } else {
                    mapStatus[tx][ty] = Status.OPEN;
                    knownDistance[tx][ty] = knownDistance[cx][cy] + 1;
                    estimatedDistance[tx][ty] = estimateDistance(tx, ty, x1, y1);

                    parent[tx][ty].x = cx; parent[tx][ty].y = cy;
                }


            }

        }

        avoidCreatures = false;
        ignoreDoors = false;
        isPlayer = false;


        List<Cords> path = new ArrayList<>();

        if(done.equals("found path")){
            int x = x1; int y = y1;

            while(x != x2 || y != y2){
                if(x < 0 || y < 0 || x >= world.width() || y >= world.width()){
                    break;
                }

                path.add(parent[x][y]);

                Cords c = new Cords(parent[x][y].x, parent[x][y].y);
                x = c.x;
                y = c.y;
            }

            return path;
        }







        return null;
    }


    private static int estimateDistance(int x1, int y1, int x2, int y2){
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }




}

enum Status {
    UNTESTED, OPEN, CLOSED
}