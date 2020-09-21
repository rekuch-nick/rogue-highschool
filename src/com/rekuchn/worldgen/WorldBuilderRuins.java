package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.Global.level;
import static com.rekuchn.worldgen.WorldBuilder.*;
import static com.rekuchn.worldgen.WorldBuilderMaze.perfectMaze;

public class WorldBuilderRuins {

    public static World create(){

        randomizeTiles(Tile.FLOOR, Tile.WALL);
        smooth(8, Tile.FLOOR, Tile.FLOOR.WALL);
        Tile[][] bg = tiles.clone();

        tiles = new Tile[width][height];
        setAllTilesTo(Tile.WALL);
        setQuarterTilesTo(Tile.NODE);


        if(xStart % 2 == 0){ xStart ++; if(xStart >= width){ xStart -= 2; }}
        if(yStart % 2 == 0){ yStart ++; if(xStart >= height){ yStart -= 2; }}
        tiles[xStart][yStart] = Tile.START_FLOOR;

        perfectMaze(xStart, yStart);
        fancyWalls();

        removeByBG(bg);

        tiles[xStart][yStart] = Tile.START_FLOOR;

        int keysNeeded = 0;
        int towers = 6;
        for(int i=1; i<=towers; i++) {
            room(i);
            Cords c = pickSpotInsideRoom(i);
            doorFor(c);
            keysNeeded ++;
        }

        for(int i=0; i<keysNeeded; i++){
            putItemInRoom(ItemList.get("Key"), 0);
        }

        setTileAToTileB(Tile.WALL, Tile.BRICK_WALL);

        tiles[xStart][yStart] = Tile.START_FLOOR;
        putTileInRoom(Tile.STAIRS, 1);



        // random wandering monsters
        int monsterPoints = 34 + (level * 2);
        String monsterType = pickBreed();
        while(monsterPoints >= monsterCost){
            monsterPoints -= monsterCost;
            putCreatureInRoom(monsterType, 0);
            monsterType = pickBreed();
        }

        fillTowers(towers);



        return new World(tiles, events, items);

    }

    static void fillTowers(int towers){
        for(int i=0; i<=towers; i++){
            int monsterPoints = 10;
            String monsterType = pickBreed();
            while(monsterPoints >= monsterCost){
                monsterPoints -= monsterCost;
                putCreatureInRoom(monsterType, i);
            }


            int itemPoints = random.nextInt(6) + 1;
            while(itemPoints > 0){
                itemPoints --;
                putItemInRoom(ItemList.get(random), i);
            }

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

        tiles[x][y] = Tile.LOCKED_DOOR;
    }

    static void room(int n){

        while(true){
            int xSize = 12; int ySize = 8;
            int x1 = random.nextInt(width - xSize - 2) + 1;
            int y1 = random.nextInt(height - ySize - 2) + 1;
            int x2 = x1 + xSize;
            int y2 = y1 + ySize;

            boolean overlap = false;
            for(int x=x1; x<=x2; x++){ for(int y=y1; y<=y2; y++){
                if(roomNumbers[x][y] != 0){ overlap = true; }
                if(tiles[x][y] == Tile.START_FLOOR){ overlap = true; }
            }}
            if(overlap){ continue; }

            for(int x=x1; x<=x2; x++){ for(int y=y1; y<=y2; y++){
                tiles[x][y] = Tile.FLOOR;
            }}
            for(int x=x1+1; x<=x2-1; x++){ for(int y=y1+1; y<=y2-1; y++){
                tiles[x][y] = Tile.WALL;
                roomNumbers[x][y] = n;
            }}
            for(int x=x1+2; x<=x2-2; x++){ for(int y=y1+2; y<=y2-2; y++){
                tiles[x][y] = Tile.FLOOR;
            }}

            break;
        }

    }

    static void removeByBG(Tile[][] bg){
        for(int x=1; x<width-1; x++){  for(int y=1; y<height-1; y++){
            if(tiles[x][y] == Tile.START_FLOOR){ continue; }
            if(tiles[x][y] == Tile.STAIRS){ continue; }
            if(bg[x][y] == Tile.FLOOR){
                tiles[x][y] = Tile.FLOOR;
            }

        }}
    }

}
