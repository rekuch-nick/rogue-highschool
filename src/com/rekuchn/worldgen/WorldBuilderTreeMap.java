package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.Item;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;


import static com.rekuchn.Global.level;
import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderTreeMap {

    public static World create(Tile[][] tiles, int[][] roomNumbers){

        maxRooms = 14;
        keys = 0;

        if(level % 3 == 0) {
            vault = 11; ////
        }

        setAllTilesTo(Tile.WALL);
        setQuarterTilesTo(Tile.NODE);

        while(tiles[xStart][yStart] != Tile.NODE){
            xStart += random.nextInt(3) - 1;
            yStart += random.nextInt(3) - 1;
            if(xStart < 0){ xStart = 0; }
            if(yStart < 0){ yStart = 0; }
            if(xStart >= width){ xStart = width - 1; }
            if(yStart >= height){ yStart = height - 1; }
        }

        //do {
        //    xStart = random.nextInt(120) + 10;
        //    yStart = random.nextInt(29) + 5;
        //} while(tiles[xStart][yStart] != Tile.NODE);

        roomNumber = 1;
        tiles[xStart][yStart] = Tile.START_FLOOR;
        roomNumbers[xStart][yStart] = 1;
        makeRoomAt(xStart, yStart, random.nextInt(15) + 10);


        roomNumber = 2;
        while(roomNumber <= maxRooms){

            int x = (random.nextInt(width / 2) * 2) + 1;
            int y = (random.nextInt(height / 2) * 2) + 1;


            if(tiles[x][y] != Tile.NODE){ continue; }
            if(enforceNodes(x, y, 15)){ continue; }
            if(!nodeAdjacentLesserRoom(x, y, roomNumber)){ continue; }

            tiles[x][y] = Tile.FLOOR;
            roomNumbers[x][y] = roomNumber;

            makeRoomAt(x, y, random.nextInt(20) + 20);

            //setTileAToTileB(Tile.DOOR, Tile.LOCKED_DOOR);

            //printRoomCompToConsole();

            roomNumber ++;
        }





        putTileInRoom(Tile.STAIRS, roomNumber - 1);

        if(vault > -1){ putTileInRoom(Tile.SHRINE, vault); }








        removeWallsInRooms();
        setTileAToTileB(Tile.NODE, Tile.WALL);
        fancyWalls();


        for(int x=0; x<width; x++){ for(int y=0; y<height; y++){

            if(items[x][y] != null && items[x][y].name().equals("Key")){
                Tile t = putBlockInRange(Tile.STATUE, x, y, 12);
                if(t != null){
                    events.add(new EventTrigger("Grabbed Key", x, y, lastX, lastY));
                    continue;
                }
            }

            if(tiles[x][y] == Tile.LEVER_UP){
                int r = random.nextInt(100) + 1;
                String note = "Bomb"; int ox = x; int oy = y;
                if(r>=96 && r <= 100){ if(vault > -1 && vaultSolution.equals("")){
                    vaultSolution = "Lever";
                    note = "Shrine";
                }}
                else if(r >= 45 && r <= 95){
                    Tile t = putBlockInRange(Tile.STATUE, x, y, 12);
                    if(t != null){ note = "Item"; ox = lastX; oy = lastY; }
                }


                events.add(new EventTrigger("Pull Lever", x, y, ox, oy, note));
            }


        }}

        //Kannushi
        if(vault > -1 && vaultSolution.equals("")){
            putCreatureInRoom("Kannushi", random.nextInt(vault - 2) + 2);
        }


        int ws = random.nextInt(10) - 5;
        for(int i=0; i<ws; i++) { putWallSwitch(); }



        if(random.nextBoolean()){
            for(int i=0; i<random.nextInt(3) + 4; i++) { randomPatch(Tile.VINE); }
        }


        //maybeSetTileAToTileB(Tile.FLOOR, Tile.WALL_SWITCH, 30);

        //randomizeTiles();
        //smooth(2);
        //for(int x=0; x<141; x++){ tiles[x][22] = Tile.WATER; }

        return new World(tiles, events, items);
    }

    private static void makeRoomAt(int xPoint, int yPoint, int size){


        /// hallways
        if(roomNumber > 1 && random.nextBoolean()){
            int xDir = 0; int yDir = 0;

            if(xEntry < xPoint){ xDir = 2; }
            if(yEntry < yPoint){ yDir = 2; }
            if(xEntry > xPoint){ xDir = -2; }
            if(yEntry > yPoint){ yDir = -2; }

            while(true){

                if(xPoint < 2 || xPoint >= width - 2 || yPoint < 2 || yPoint >= height - 2){ break; }
                if(tiles[xPoint+xDir][yPoint+yDir] != Tile.NODE){ break; }
                if(random.nextInt(14) == 0){ break; }


                tiles[xPoint+(xDir/2)][yPoint+(yDir/2)] = Tile.FLOOR; roomNumbers[xPoint+(xDir/2)][yPoint+(yDir/2)] = roomNumber;
                xPoint += xDir; yPoint += yDir;
                tiles[xPoint][yPoint] = Tile.FLOOR; roomNumbers[xPoint][yPoint] = roomNumber;

            }
        }


        int monsterPoints = random.nextInt(4) + random.nextInt(4) + random.nextInt(4);
        if(roomNumber == 1){ monsterPoints = 0; }
        int swapChance = random.nextInt(30) + 1;
        String monsterType = pickBreed();

        int itemPoints = random.nextInt(5);
        //itemPoints = 100; ///////////////////////////////
        if(roomNumber == vault){
            itemPoints = 0;
            monsterPoints = 0;
            size = Math.min(size, 6);
        }




        int tries = 0;
        int sizeRange = (int)(size / 2);
        while(size > 0 && tries < 100000){
            tries ++;

            //int x = random.nextInt(width); int y = random.nextInt(height);
            int x = xPoint + random.nextInt(sizeRange * 2) - sizeRange;
            int y = yPoint + random.nextInt(sizeRange * 2) - sizeRange;

            if(x < 0 || y < 0 || x >= width || y >= height){ continue; }
            if(tiles[x][y] != Tile.NODE){ continue; }
            if(roomNumbers[x][y] != 0){ continue; }
            if(!nodeAdjacentRoom(x, y, roomNumber)){ continue; }

            tiles[x][y] = Tile.FLOOR;
            roomNumbers[x][y] = roomNumber;
            size --;


            if(random.nextInt(100) == 1 && roomNumber != vault){
                tiles[x][y] = Tile.LEVER_UP;
                continue;
            }




            if(random.nextInt(3) == 0){
                if(monsterPoints >= monsterCost){
                    monsterPoints -= monsterCost;
                    creatures[x][y] = monsterType;
                    if(monsterPoints < monsterCost && random.nextBoolean()){ monsterPoints = 0; }
                }
                if(random.nextInt(100) >= swapChance || monsterPoints < monsterCost){ monsterType = pickBreed(); }
            }
            int itemScarcity = 3;
            if(random.nextInt(itemScarcity) == 0){
                if(itemPoints > 0) {
                    itemPoints --;
                    if(random.nextBoolean() && random.nextBoolean()){
                        items[x][y] = ItemList.get("Key");
                        keys = 1;
                    } else {
                        items[x][y] = ItemList.get(random);
                        if(items[x][y].name() != null && items[x][y].name().equals("Potion of Disintegrate")){
                            vaultSolution = "Potion of Disintegrate";
                        }
                    }

                }

            }

        }


    }
    static void removeWallsInRooms(){

        for (int x = 1; x < width-1; x++) {
            for (int y = 1; y < height-1; y++) {
                if(tiles[x][y] == Tile.WALL){

                    boolean okay = false;
                    int rn = 0;

                    if(roomNumbers[x-1][y] == roomNumbers[x+1][y]){ okay = true; if(rn == 0){ rn = roomNumbers[x-1][y]; }}
                    if(roomNumbers[x][y-1] == roomNumbers[x][y+1]){ okay = true; if(rn == 0){ rn = roomNumbers[x][y-1]; }}
                    if(     roomNumbers[x-1][y-1] == roomNumbers[x+1][y+1] &&
                            roomNumbers[x-1][y-1] == roomNumbers[x-1][y+1] &&
                            roomNumbers[x-1][y-1] == roomNumbers[x+1][y-1]){ okay = true; if(rn == 0){ rn = roomNumbers[x-1][y-1]; }}
                    if(rn == 0){ okay = false; }

                    for(Cords cord : Cords.adjacent(x, y)){
                        if(tiles[cord.x][cord.y].isDoor()){ okay = false; }
                    }

                    if(okay){
                        tiles[x][y] = Tile.FLOOR;
                        roomNumbers[x][y] = rn;
                    }
                }
            }
        }

    }

    static boolean nodeAdjacentRoom(int x, int y, int n){
        if(x > 1 && roomNumbers[x-2][y] == n){ return true; }
        if(x < width-2 && roomNumbers[x+2][y] == n){ return true; }
        if(y > 1 && roomNumbers[x][y-2] == n){ return true; }
        if(y < height-2 && roomNumbers[x][y+2] == n){ return true; }

        return false;
    }

    static boolean nodeAdjacentLesserRoom(int x, int y, int n){
        Tile door = Tile.DOOR;
        if(keys > 0 && random.nextInt(100) >= 70){ door = Tile.LOCKED_DOOR; }
        if(keys > 0 && roomNumber == maxRooms){ door = Tile.LOCKED_DOOR; }
        if(roomNumber == vault){ door = Tile.EVENT_DOOR; }

        if(nodeCanAttach(x-2, y, x-1, y, n, door)){ return true; }
        if(nodeCanAttach(x+2, y, x+1, y, n, door)){ return true; }
        if(nodeCanAttach(x, y-2, x, y-1, n, door)){ return true; }
        if(nodeCanAttach(x, y+2, x, y+1, n, door)){ return true; }

        //if(y < height-2 && roomNumbers[x][y+2] < n && roomNumbers[x][y+2] > 0){
        //    xEntry = x; yEntry = y+1;
        //    tiles[x][y+1] = door; roomNumbers[x][y+1] = roomNumber;
        //    if(door == Tile.LOCKED_DOOR){ keys --; }
        //    return true;
        //}

        return false;
    }

    public static boolean nodeCanAttach(int x, int y, int xEntry, int yEntry, int n, Tile door){

        if(x < 0 || y < 0 || x >= width || y >= height){ return false; }
        if(roomNumbers[x][y] >= n){ return false; }
        if(roomNumbers[x][y] == vault){ return false; }
        if(roomNumbers[x][y] == 0){ return false; }

        WorldBuilder.xEntry = xEntry; WorldBuilder.yEntry = yEntry;
        tiles[xEntry][yEntry] = door; roomNumbers[xEntry][yEntry] = roomNumber;
        if(door == Tile.LOCKED_DOOR){ keys --; }

        return true;
    }

    static boolean enforceNodes(int x, int y, int n){
        count = 0;
        floodCountNodes(x, y);

        setTileAToTileB(Tile.COUNTED_NODE, Tile.NODE);
        lastCount = count;
        return count < n;
    }

    static void floodCountNodes(int x, int y){


        if(x < 0 || y < 0 || x >= width || y >= height){ return; }
        if(tiles[x][y] != Tile.NODE){ return; }
        if(roomNumbers[x][y] != 0){ return; }


        tiles[x][y] = Tile.COUNTED_NODE;

        floodCountNodes(x - 2, y);
        floodCountNodes(x + 2, y);
        floodCountNodes(x, y - 2);
        floodCountNodes(x, y + 2);

        count ++;

    }


}
