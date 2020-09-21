package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.Global.debugMode;
import static com.rekuchn.Global.level;
import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderTwistyTunnel {

    public static World create(){

        //setAllTilesTo(Tile.FLOOR);
        //border();
        //fancyWalls();
        setAllTilesTo(Tile.WALL);
        setTileAToTileB(Tile.FLOOR, Tile.WALL);

        xStart = 1; yStart = 37;
        tiles[xStart][yStart] = Tile.START_FLOOR;


        mainPath(xStart, yStart, 139, 1);

        int rooms = 50;
        for(int i=2; i<rooms; i++) {

            Cords c = findPointAdjacentToRoom(1);
            subPath(c.x, c.y, i);
        }





        int itemPoints = 12;
        if(level == 2 && debugMode){ itemPoints = 600; }
        while(itemPoints > 0){
            itemPoints --;
            putItemInRoom(ItemList.get(random),random.nextInt(rooms - 1) + 2);
        }


        int monsterPoints = 20;
        String monsterType = pickBreed();
        while(monsterPoints >= monsterCost){
            if(level == 2){ monsterType = "Bat"; }
            monsterPoints -= monsterCost;
            putCreatureAnywhere(monsterType);
            monsterType = pickBreed();
        }

        for(int i=0; i<20; i++) {
            //putCreatureAnywhere("Wolf");
            //putCreatureAnywhere("Bat");
        }
        //for(int i=0; i<35; i++) { putCreatureAnywhere("Fungus"); }

        for(int a = 1; a < 10; a++){
            for(int b = 34; b <= 37; b++){
                tiles[a][b] = Tile.FLOOR;
            }
        }


        tiles[xStart-1][yStart] = Tile.LAVA_BLOCK;
        tiles[xStart][yStart] = Tile.START_FLOOR;
        tiles[139][1] = Tile.STAIRS;


        //setTileAToTileB(Tile.WALL, Tile.BLOCK);


        events.add(new EventTrigger("Lava Flow", -1, -1, -1, -1));


        return new World(tiles, events, items);
    }

    static void subPath(int x1, int y1, int n){
        int size = random.nextInt(40) + 20;
        int x = x1; int y = y1;

        int px = 0; int py = 0;
        if(roomNumbers[x-1][y] == 1){ px = 1; }
        if(roomNumbers[x+1][y] == 1){ px = -1; }
        if(roomNumbers[x][y-1] == 1){ py = 1; }
        if(roomNumbers[x][y+1] == 1){ py = -1; }

        while(size > 0){
            size --;
            if(x < 1 || y < 1 || x >= width - 1 || y >= height - 1){ continue; }

            if(roomNumbers[x][y] == 0) {
                tiles[x][y] = Tile.FLOOR;
                roomNumbers[x][y] = n;
            }

            int lx = x; int ly = y;
            while(lx == x && ly == y) {

                if (random.nextBoolean()) {
                    x = lx + random.nextInt(3) - 1;
                    y = ly;
                } else {
                    x = lx;
                    y = ly + random.nextInt(3) - 1;
                }

                if(random.nextInt(3) == 0 ){
                    if(random.nextBoolean()) {
                        x = lx + px;
                        y = ly;
                    } else {
                        x = lx;
                        y = ly + py;
                    }
                }
            }

        }

    }

    static int xm; static int ym;
    static void mainPath(int x1, int y1, int x2, int y2){
        xm = 0; ym = 0;
        int x = x1; int y = y1;
        pickDirection(x, y, x2, y2);

        while(x != x2 || y != y2){

            if(x + xm >= 1 && x + xm < width - 1){ x += xm; }
            if(y + ym >= 1 && y + ym < height - 1){ y += ym; }

            if(tiles[x][y] == Tile.WALL){
                tiles[x][y] = Tile.FLOOR;
                roomNumbers[x][y] = 1;
            }
            pickDirection(x, y, x2, y2);
        }
    }

    static void pickDirection(int x, int y, int x2, int y2){
        boolean newDirection = false;
        if(random.nextInt(6) == 0){ newDirection = true; }

        if(!newDirection){ return; }

        int dx = 0;
        if(x < x2){ dx = 1; }
        if(x > x2){ dx = -1; }
        int dy = 0;
        if(y < y2){ dy = 1; }
        if(y > y2){ dy = -1; }

        int r = random.nextInt(8) + 1;
        if(r == 1){ xm = 0; ym = -1; }
        if(r == 2){ xm = 1; ym = 0; }
        if(r == 3){ xm = 0; ym = 1; }
        if(r == 4){ xm = -1; ym = 0; }
        if(r >= 5 && r <= 6){ xm = dx; ym = 0; }
        if(r >= 7 && r <= 8){ xm = 0; ym = dy; }
    }


}
