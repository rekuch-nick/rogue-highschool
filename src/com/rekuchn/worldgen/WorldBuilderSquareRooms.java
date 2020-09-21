package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderSquareRooms {

    static boolean[] reachable;
    static int memX, memY;
    static int lastRoom;

    public static World create(){


        setAllTilesTo(Tile.FLOOR);
        border();
        fancyWalls();
        setTileAToTileB(Tile.FLOOR, Tile.WALL);




        makeGrid();

        reachable = new boolean[22];
        for(int i=1; i<=21; i++){ reachable[i] = false; }

        while(xStart < 4){ xStart ++; }
        while(xStart > width - 6){ xStart --; }
        while(yStart < 2){ yStart ++; }
        while(yStart > height - 3){ yStart --; }

        tiles[xStart][yStart] = Tile.START_FLOOR;
        int inRoom = roomNumbers[xStart][yStart];

        reachable[inRoom] = true;

        while (anyRoomUnreachable()){
            int room = getRandomReachable();
            int adj = getAdjacentUnreachable(room);
            if(adj != -1){
                tiles[memX][memY] = Tile.DOOR;
                reachable[adj] = true;

            }

        }



        putTileInRoom(Tile.STAIRS, lastRoom);

        fancyWalls();

        for(int i=1; i<=21; i++){
            if(i == inRoom || i == lastRoom){ continue; }
            fillRoom(i);
        }




        return new World(tiles, events, items);

    }


    static void fillRoom(int room){

        int r = random.nextInt(3);
        Tile t = Tile.BUSH;
        if(r == 1){ t = Tile.VINE; }
        if(r == 2){ t = Tile.BLOCK; }

        int m = random.nextInt(10) + 10;
        for(int i=0; i<m; i++){
            putTileInsideRoom(t, room);
        }


        int monsterPoints = 10;
        String monsterType = pickBreed();
        while(monsterPoints >= monsterCost){
            monsterPoints -= monsterCost;
            putCreatureInsideRoom(monsterType, room);
        }


        int itemPoints = random.nextInt(3);
        while(itemPoints > 0){
            itemPoints --;
            putItemInsideRoom(ItemList.get(random), room);
        }

    }







    static int getAdjacentUnreachable(int room){
        Cords c1 = findPointInRoom(room);
        Cords c2 = findPointInRoom(room);

        Cords c = new Cords((c1.x + c2.x) / 2, (c1.y + c2.y) / 2);

        boolean d1 = false; boolean d2 = false; boolean d3 = false; boolean d4 = false;

        while(!d1 || !d2 || !d3 || !d4){
            int r = random.nextInt(4) + 1;
            int xm = 0; int ym = 0;
            if(r == 1){ ym = -1; d1 = true; }
            if(r == 2){ xm = 1; d2 = true; }
            if(r == 3){ ym = 1; d3 = true; }
            if(r == 4){ xm = -1; d4 = true; }

            int x = c.x; int y = c.y;


            while(x > 0 && y > 0 && x < width-1 && y < height-1){
                x += xm; y += ym;


                if(roomNumbers[x][y] == 0){ memX = x; memY = y; }
                if(roomNumbers[x][y] != room && roomNumbers[x][y] != 0){
                    int rf = roomNumbers[x][y];
                    if(!reachable[rf]){
                        return rf;
                    } else {
                        break;
                    }
                }
            }

        }

        return -1;
    }

    static int getRandomReachable(){
        while(true){
            int r = random.nextInt(reachable.length);
            if(reachable[r]){ return r; }
        }
    }

    static boolean anyRoomUnreachable(){
        for(int i=1; i<=21; i++){
            if(!reachable[i]){
                lastRoom = i;
                return true; }
        }
        return false;
    }

    static void makeGrid(){
        int rn = 0;
        for(int xRoom = 0; xRoom < 7; xRoom ++){
            for(int yRoom = 0; yRoom < 3; yRoom ++){
                rn ++;

                int xr = xRoom;
                while(xr >= 140){ xr -= 140; }

                int x1 = 4 + (xr * 19);
                int y1 = 2 + (yRoom * 12);

                int w = 18;
                int h = 11;

                for(int x=x1; x<x1+w; x++){
                    for(int y=y1; y<y1+h; y++){
                        tiles[x][y] = Tile.FLOOR;
                        roomNumbers[x][y] = rn;
                    }
                }

            }
        }
    }
}
