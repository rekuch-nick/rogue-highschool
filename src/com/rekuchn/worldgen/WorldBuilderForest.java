package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.worldgen.WorldBuilder.*;
import static com.rekuchn.worldgen.WorldBuilder.events;
import static com.rekuchn.worldgen.WorldBuilderSchoolEntry.treeBorder;

public class WorldBuilderForest {

    public static World create(){

        border();
        randomizeTiles(Tile.GRASS, Tile.GRASS3);
        smooth(8, Tile.GRASS, Tile.GRASS3);

        verticalStripe(Tile.WATER, random.nextInt(80) + 30, 9);

        treeBorder();






        tiles[xStart][yStart] = Tile.START_FLOOR;




        for(int i=0; i<20; i++){
            putQuadTileAnywhereWide(Tile.BUSH, 60);
        }

        for(int i=0; i<100; i++){
            putTileAnywhereWide(Tile.BUSH);
        }

        int lastRoom = 0;
        for(int i=1; i<8; i++){
            buildHouse(i);
            lastRoom = i;
            fillHouse(i);
        }


        putTileInRoom(Tile.STAIRS, lastRoom);

        events.add(new EventTrigger("Wolf Call", 0, 0, 0, 0));

        return new World(tiles, events, items);
    }

    private static void buildHouse(int room){
        while(true){

            int x = random.nextInt(width - 12) + 1; // 10
            int y = random.nextInt(height - 8) + 1; // 6

            int w = random.nextInt(6) + 10;
            int h = random.nextInt(4) + 6;

            boolean okay = true;
            for(int a=x; a<=x+w; a++){
                for(int b=y; b<=y+h; b++){
                    if(a < 2 || b < 2 || a >= width - 1 || b >= height - 1){ okay = false; continue; }
                    if(roomNumbers[a][b] != 0){ okay = false; }
                    if(tiles[a][b] == Tile.START_FLOOR){ okay = false; }
                }
            }
            if(!okay){ continue; }


            for(int a=x; a<=x+w; a++){
                for(int b=y; b<=y+h; b++){
                    tiles[a][b] = Tile.FLOOR;
                }
            }
            for(int a=x+1; a<=x+w-1; a++){
                for(int b=y+1; b<=y+h-1; b++){
                    tiles[a][b] = Tile.WALL;
                    roomNumbers[a][b] = room;
                }
            }
            for(int a=x+2; a<=x+w-2; a++){
                for(int b=y+2; b<=y+h-2; b++){
                    tiles[a][b] = Tile.FLOOR;
                }
            }




            Cords c = pickSpotInsideRoom(room);
            doorFor(c);


            break;
        }
    }


    static void doorFor(Cords c){
        int dir = random.nextInt(4) + 1;
        int mx = 0; int my = 0;
        if(dir == 1){ my = -1; }
        if(dir == 2){ mx = 1; }
        if(dir == 3){ my = 1; }
        if(dir == 4){ mx = -1; }

        int x = c.x; int y = c.y;

        while(tiles[x][y] != Tile.WALL){
            x += mx;
            y += my;
        }

        tiles[x][y] = Tile.DOOR;
    }

    static void verticalStripe(Tile t, int x, int w){


        for(int y=0; y<height; y++){

            while(x < 1){ x ++; }
            while(x + w >= width){ x --; }

            for(int i=0; i<w; i++){
                tiles[x + i][y] = t;
                roomNumbers[x + i][y] = 101;
            }

            x += random.nextInt(3) - 1;
            x += random.nextInt(3) - 1;
        }


    }

    static void fillHouse(int room){

            int monsterPoints = 10;
            String monsterType = pickBreed();
            while(monsterPoints >= monsterCost){
                monsterPoints -= monsterCost;
                putCreatureInRoom(monsterType, room);
            }


            int itemPoints = Math.max(random.nextInt(3) + 1, 0);
            while(itemPoints > 0){
                itemPoints --;
                putItemInRoom(ItemList.get(random), room);
            }


    }


}
