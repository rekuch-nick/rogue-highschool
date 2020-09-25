package com.rekuchn.worldgen;

import com.rekuchn.Global;
import com.rekuchn.creature.CreatureList;
import com.rekuchn.creature.CreatureString;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.Item;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.World;
import com.rekuchn.service.Cords;
import com.rekuchn.service.PowerManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.rekuchn.Global.*;


public class WorldBuilder {
    static Random random = new Random();
    public static int width = 141;
    public static int height = 39;
    static Tile[][] tiles;
    public static Item[][] items;
    public static String[][] creatures;
    static int roomNumber;
    public static int[][] roomNumbers;
    static int maxRooms;
    static int xEntry, yEntry;
    static boolean havePlacedStairs = false;
    static List<EventTrigger> events;
    public static int xStart, yStart;
    static int keys = 0;
    static int vault = -1;
    static String vaultSolution = "";
    static int monsterCost; static boolean singleMonster;
    static int lastCount, count;

    public static World create(int xInit, int yInit){

        events = new ArrayList<>();
        level ++;

        int buildSeed = seed;// + (level * 1000000);
        random.setSeed(buildSeed);
        for(int i=0; i<level * 120; i++){
            int r = random.nextInt(100);
        }

        if(powerList == null){ powerList = PowerManager.getRandomPowerList(random); }

        CreatureList.setRandomCreatureList(random);

        tiles = new Tile[width][height];
        items = new Item[width][height];
        creatures = new String[width][height];
        roomNumbers = new int[width][height];
        havePlacedStairs = false;
        xStart = xInit; yStart = yInit;

        vaultSolution = "";



        messages = new LinkedList<>();
        if(currentMessage != null){ currentMessage.setText("."); }
        messageCD = 1;


        //if(true){ return WorldBuilderTwistyTunnel.create(); }
        //if(true){ return WorldBuilderSea.create(); }
        //if(true){ return WorldBuilderTreeMap.create(tiles, roomNumbers); }



        if(level == 1){
            Global.addMessage("It's hard to believe I was transferred to the all girl's highschool.");
            Global.addMessage("I'm going to work twice as hard to prove my value as a student!");
            return WorldBuilderSchoolEntry.create(); } //

        if(level == 2){
            Global.addMessage("The early warning bell is ringing.");
            Global.addMessage("That means I have just five minutes to get to class.");
            Global.addMessage("How embarrassing it would be to be late on the very first day!");
            return WorldBuilderTwistyTunnel.create(); } //

        if(level % 3 == 0){
            Global.addMessage("There is a shrine " + (char)241 + " somewhere on this level, I should try to find it.");
            return WorldBuilderTreeMap.create(tiles, roomNumbers); }


        if(level >= 11 && level <= 16){ return WorldBuilderSea.create(); }

        if(level % 10 == 0){ return WorldBuilderTwistyTunnel.create(); }
        if(level % 8 == 0){ return WorldBuilderForest.create(); }
        if(level % 7 == 0){ return WorldBuilderSquareRooms.create(); }
        if(level % 5 == 0){ return WorldBuilderRuins.create(); }
        if(level % 4 == 0){ return WorldBuilderRogue.create(); }



        //return WorldBuilderMaze.create();


        //int r = random.nextInt(5);
        //if(r == 1){ return WorldBuilderTwistyTunnel.create(); }
        //if(r == 2){ return WorldBuilderRogue.create(); }
        //if(r == 3){ return WorldBuilderRuins.create(); }
        //if(r == 4){ return WorldBuilderSquareRooms.create(); }


        return WorldBuilderTreeMap.create(tiles, roomNumbers);
    }


    static String pickBreed(){
        monsterCost = 1; singleMonster = false;
        CreatureString cs = CreatureList.randomCreatureString(random);
        monsterCost = cs.cr;
        return cs.name;
    }


    static void putTileInRoom(Tile t, int room){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canHaveItem() == false){ continue; }

                tiles[x][y] = t;
                break;
            }
        }
    }

    static void putTileInsideRoom(Tile t, int room){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if(pointOnEdgeOfRoom(x, y)){ continue; }

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canHaveItem() == false){ continue; }

                tiles[x][y] = t;
                break;
            }
        }
    }

    static Cords pickSpotInRoom(int room){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if(roomNumbers[x][y] == room){
                return new Cords(x, y);
            }
        }
    }

    static Cords pickSpotInsideRoom(int room){
        while(true){
            int x = random.nextInt(width-4)+2;
            int y = random.nextInt(height-4)+2;

            if(roomNumbers[x-1][y] != room || roomNumbers[x+1][y] != room || roomNumbers[x][y-1] != room || roomNumbers[x][y+1] != room){ continue; }
            //if(roomNumbers[x-2][y] != room && roomNumbers[x+2][y] != room && roomNumbers[x][y-2] != room && roomNumbers[x][y+2] != room){ continue; }

            if(roomNumbers[x][y] == room){
                return new Cords(x, y);
            }
        }
    }



    static void putCreatureInRoom(String c, int room){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canEnter() == false){ continue; }
                if(creatures[x][y] != null && !creatures[x][y].equals("")){ continue; }

                creatures[x][y] = c;
                break;
            }
        }
    }

    static void putCreatureInsideRoom(String c, int room){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if(pointOnEdgeOfRoom(x, y)){ continue; }

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canEnter() == false){ continue; }
                if(creatures[x][y] != null && !creatures[x][y].equals("")){ continue; }

                creatures[x][y] = c;
                break;
            }
        }
    }

    public static void putItemInRoom(Item item, int room){
        int tries = 0;
        while(true){
            tries ++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canHaveItem() == false){ continue; }
                if(items[x][y] != null){ continue; }

                items[x][y] = item;
                break;
            }
            if(tries > 100000){ break; }
        }
    }

    public static void putItemInsideRoom(Item item, int room){
        int tries = 0;
        while(true){
            tries ++;
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            if(pointOnEdgeOfRoom(x, y)){ continue; }

            if(roomNumbers[x][y] == room){
                if(tiles[x][y].canHaveItem() == false){ continue; }
                if(items[x][y] != null && !items[x][y].equals("")){ continue; }

                items[x][y] = item;
                break;
            }
            if(tries > 100000){ break; }
        }
    }








    static void randomizeTiles(Tile a, Tile b) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = random.nextBoolean() ? a : b;
            }
        }
    }

    static void setAllTilesTo(Tile t){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = t;
            }
        }
    }

    public static void setTileAToTileB(Tile a, Tile b){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(tiles[x][y] == a){ tiles[x][y] = b; }
            }
        }
    }

    public static void maybeSetTileAToTileB(Tile a, Tile b, int chance){
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(tiles[x][y] == a && random.nextInt(100) < chance){ tiles[x][y] = b; }
            }
        }
    }

    static void setQuarterTilesTo(Tile t){
        for (int x = 1; x < width-1; x+=2) {
            for (int y = 1; y < height-1; y+=2) {
                tiles[x][y] = t;
            }
        }
    }

    static void fancyWalls(){

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                if(tiles[x][y] == Tile.WALL){
                    boolean d1 = false; boolean d2 = false; boolean d3 = false; boolean d4 = false;

                    if(x > 0 && tiles[x-1][y].isWall()){ d4 = true; }
                    if(x < width-1 && tiles[x+1][y].isWall()){ d2 = true; }
                    if(y > 0 && tiles[x][y-1].isWall()){ d1 = true; }
                    if(y < height-1 && tiles[x][y+1].isWall()){ d3 = true; }

                    if(d1){ tiles[x][y] = Tile.W1; }
                    if(d2){ tiles[x][y] = Tile.W2; }
                    if(d3){ tiles[x][y] = Tile.W3; }
                    if(d4){ tiles[x][y] = Tile.W4; }

                    if(d1 && d2){ tiles[x][y] = Tile.W12; }
                    if(d2 && d3){ tiles[x][y] = Tile.W23; }
                    if(d3 && d4){ tiles[x][y] = Tile.W34; }
                    if(d4 && d1){ tiles[x][y] = Tile.W41; }
                    if(d1 && d3){ tiles[x][y] = Tile.W13; }
                    if(d2 && d4){ tiles[x][y] = Tile.W24; }

                    if(d1 && d2 && d3){ tiles[x][y] = Tile.W123; }
                    if(d2 && d3 && d4){ tiles[x][y] = Tile.W234; }
                    if(d3 && d4 && d1){ tiles[x][y] = Tile.W341; }
                    if(d4 && d1 && d2){ tiles[x][y] = Tile.W412; }

                    if(d1 && d2 && d3 && d4){ tiles[x][y] = Tile.W1234; }
                }
            }
        }

    }



    static void smooth(int times, Tile a, Tile b) {
        Tile[][] tiles2 = new Tile[width][height];
        for (int time = 0; time < times; time++) {

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    int floors = 0;
                    int rocks = 0;

                    for (int ox = -1; ox < 2; ox++) {
                        for (int oy = -1; oy < 2; oy++) {
                            if (x + ox < 0 || x + ox >= width || y + oy < 0
                                    || y + oy >= height)
                                continue;

                            if (tiles[x + ox][y + oy] == a)
                                floors++;
                            else
                                rocks++;
                        }
                    }
                    tiles2[x][y] = floors >= rocks ? a : b;
                }
            }
            tiles = tiles2;
        }
    }



    static void putCreatureAnywhere(String t){
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            if(tiles[x][y].canEnter() && tiles[x][y] != Tile.START_FLOOR){
                if(tiles[x][y] == Tile.WATER){ continue; }
                if(tiles[x][y] == Tile.LAVA){ continue; }
                if(creatures[x][y] == null || creatures[x][y].equals("")){
                    creatures[x][y] = t;
                    return;
                }
            }
        }
    }

    static void putCreatureAnywhereWide(String t){
        int tries = 0;
        while(tries < 50000){
            tries ++;
            int x = random.nextInt(width - 3) + 1;
            int y = random.nextInt(height - 3) + 1;

            int ok = 0;
             if(tiles[x][y] == Tile.START_FLOOR){ continue; }

            for(int a=x-1; a<=x+1; a++){ for(int b=y-1; b<=y+1; b++){
                if(tiles[a][b].canEnter()){
                    if(creatures[a][b] == null || creatures[a][b].equals("")){
                        ok ++;
                    }
                }
            }}
            if(ok == 9){
                creatures[x][y] = t;
                return;
            }
        }
    }

    static void putTileAnywhereWide(Tile t){
        int tries = 0;
        while(tries < 50000){
            tries ++;
            int x = random.nextInt(width - 3) + 1;
            int y = random.nextInt(height - 3) + 1;

            int ok = 0;
            if(tiles[x][y] == Tile.START_FLOOR){ continue; }

            for(int a=x-1; a<=x+1; a++){ for(int b=y-1; b<=y+1; b++){
                if(tiles[a][b].canOverWrite()){
                    if(creatures[a][b] == null || creatures[a][b].equals("")){
                        ok ++;
                    }
                }
            }}
            if(ok == 9){
                tiles[x][y] = t;
                return;
            }
        }
    }

    static void putQuadTileAnywhereWide(Tile t, int chance){
        int tries = 0;
        while(tries < 50000){
            tries ++;
            int x = random.nextInt(width - 4) + 1;
            int y = random.nextInt(height - 4) + 1;

            int ok = 0;

            for(int a=x-1; a<=x+2; a++){ for(int b=y-1; b<=y+2; b++){
                if(tiles[a][b].canOverWrite() && tiles[a][b] != Tile.START_FLOOR){
                    if(creatures[x][y] == null || creatures[x][y].equals("")){
                        ok ++;
                    }
                }
            }}
            if(ok == 16){
                if(random.nextInt(100) < chance){ tiles[x][y] = t; }
                if(random.nextInt(100) < chance){ tiles[x+1][y] = t; }
                if(random.nextInt(100) < chance){ tiles[x][y+1] = t; }
                if(random.nextInt(100) < chance){ tiles[x+1][y+1] = t; }

                return;
            }
        }
    }

    static void putTileAnywhereBut(Tile t, int xPoint, int yPoint){
        putTileAnywhereBut(t, null, xPoint, yPoint);
    }

    static void putTileAnywhereBut(Tile t, Tile no, int xPoint, int yPoint){
        int tries = 0;
        int minDis = (width + height) / 2;
        while(true){
            tries ++;
            if(tries > 50000 || minDis > 2){ tries = 0; minDis --; }

            int x = random.nextInt(width - 3) + 1;
            int y = random.nextInt(height - 3) + 1;

            if(tiles[x][y] == Tile.START_FLOOR){ continue; }
            if(tiles[x][y] == no){ continue; }
            if(!tiles[x][y].canEnter()){ continue; }
            if(creatures[x][y] != null){ continue; }

            int d = Math.abs(x - xPoint) + Math.abs(y - yPoint);
            if(d < minDis){ continue; }

            tiles[x][y] = t;
            break;
        }
    }



    static int lastX, lastY;
    static Tile putBlockInRange(Tile t, int sx, int sy, int r){

        int tries = 0;
        while(tries < 10000){
            tries ++;

            int x = sx + random.nextInt(r * 2) - r;
            int y = sy + random.nextInt(r * 2) - r;
            if(Math.abs(sx - x) + Math.abs(sy - y) > r){ continue; }
            if(x < 0 || y < 0 || x >= width || y >= height){ continue; }
            if(!tiles[x][y].canEnter()){ continue; }
            if(tiles[x][y] == Tile.START_FLOOR){ continue; }
            if(items[x][y] != null && !items[x][y].equals("")){ continue; }
            if(creatures[x][y] != null && !creatures[x][y].equals("")){ continue; }
            if(roomNumbers[x][y] >= roomNumbers[sx][sy]){ continue; }

            int adjacentBlocks = 0;
            for(int xx=x-1; xx<=x+1; xx++){
                for(int yy=y-1; yy<=y+1; yy++){
                    if(!tiles[xx][yy].canEnter()){
                        adjacentBlocks ++;
                    }
                }
            }

            if(adjacentBlocks == 0){
                lastX = x; lastY = y;
                tiles[x][y] = t;
                return tiles[x][y];
            }
        }
        return null;
    }



    static void printRoomCompToConsole(){
        for(int y=0; y<height; y++){
            for(int x=0; x<width; x++){
                String c = "";
                int n = roomNumbers[x][y];
                if(n < 10){ c = "" + n; } else {
                    c = "" + (char)(97 + 9 + n);
                }
                if(n == 0){ c = "."; }
                System.out.print(c);

            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }


    static void border(){
        setAllTilesTo(Tile.FLOOR);
        for(int x=0; x<width; x++){
            tiles[x][0] = Tile.WALL;
            tiles[x][height-1] = Tile.WALL;
        }
        for(int y=0; y<height; y++){
            tiles[0][y] = Tile.WALL;
            tiles[width-1][y] = Tile.WALL;
        }
        fancyWalls();
    }

    static void afterBorder(){
        for(int x=0; x<width; x++){
            tiles[x][0] = Tile.WALL;
            tiles[x][height-1] = Tile.WALL;
        }
        for(int y=0; y<height; y++){
            tiles[0][y] = Tile.WALL;
            tiles[width-1][y] = Tile.WALL;
        }
        fancyWalls();
    }

    static boolean pointAdjacentToTile(int x, int y, Tile t){
        if(x < 0 || y < 0 || x >= width || y >= height){ return false; }

        if(x > 1 && tiles[x-1][y] == t){ return true; }
        if(y > 1 && tiles[x][y-1] == t){ return true; }
        if(x < width-2 && tiles[x+1][y] == t){ return true; }
        if(y < height-2 && tiles[x][y+1] == t){ return true; }
        return false;
    }

    static int countTile(Tile t){
        int count = 0;
        for(int x=0; x<width; x++){ for(int y=0; y<height; y++){
            if(tiles[x][y] == t){ count ++; }
        }}
        return count;
    }

    static Cords findPointAdjacentToRoom(int room){
        while(true){
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;

            if(roomNumbers[x][y] == room){ continue; }
            if(roomNumbers[x+1][y] == room){ return new Cords(x, y); }
            if(roomNumbers[x-1][y] == room){ return new Cords(x, y); }
            if(roomNumbers[x][y+1] == room){ return new Cords(x, y); }
            if(roomNumbers[x][y-1] == room){ return new Cords(x, y); }
        }
    }

    static Cords findPointInRoom(int room){
        while(true){
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;
            if(roomNumbers[x][y] == room){ return new Cords(x, y); }
        }
    }

    static boolean pointOnEdgeOfRoom(int x, int y){
        int r = roomNumbers[x][y];
        if(x < 1 || roomNumbers[x-1][y] != r){ return true; }
        if(x >= width-1 || roomNumbers[x+1][y] != r){ return true; }
        if(y < 1 || roomNumbers[x][y-1] != r){ return true; }
        if(y >= height-1 || roomNumbers[x][y+1] != r){ return true; }
        return false;
    }


    static void putWallSwitch(){
        int tries = 0;
        while(tries < 100000){
            tries ++;
            int x = random.nextInt(width - 4) + 2;
            int y = random.nextInt(height - 4) + 2;
            int mx = 0; int my = 0;

            int rn = roomNumbers[x][y];
            if(rn == 0){ continue; }
            if(items[x][y] != null){ continue; }
            if(!tiles[x][y].canOverWrite()){ continue; }

            int dir = random.nextInt(4) + 1;
            if(dir == 1){ my = -2; }
            if(dir == 2){ mx = 2; }
            if(dir == 3){ my = 2; }
            if(dir == 4){ mx = -2; }

            int tx = x + mx;
            int ty = y + my;

            if(roomNumbers[x + (mx/2)][y + (my/2)] != 0){ continue; }

            if(rn <= roomNumbers[tx][ty]){ continue; }
            if(roomNumbers[tx][ty] == 0){ continue; }


            tiles[x][y] = Tile.WALL_SWITCH;
            return;
        }

    }

    static void randomPatch(Tile t){
        int tries = 0;
        while(tries < 100000){
            tries ++;
            int x = random.nextInt(width - 2) + 1;
            int y = random.nextInt(height - 2) + 1;

            if(!tiles[x][y].canOverWrite()){ continue; }

            patchOf(x, y, t, random.nextInt(4) + 3, 50, random.nextBoolean());
            break;
        }
    }


    static void patchOf(int x, int y, Tile t, int range, int chance, boolean circle){

        for(int a=x-range; a<=x+range; a++){
            for(int b=y-range; b<=y+range; b++){
                if(a < 1 || b < 1 || a >= width - 1 || b >= height - 1){ continue; }

                if(circle){
                    int d = Math.abs(x - a) + Math.abs(y - b);
                    if(d > range){ continue; }
                }

                int roll = random.nextInt(100);
                if(roll < chance){
                    if(!tiles[a][b].canOverWrite()){ continue; }
                    tiles[a][b] = t;
                }
            }
        }


    }


}
