package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderRogue {


    public static World create(){

        border();

        setTileAToTileB(Tile.FLOOR, Tile.WALL);

        nineRooms();
        connectAToB(1, 2); connectAToB(2, 3);
        connectAToB(4, 5); connectAToB(5, 6);
        connectAToB(7, 8); connectAToB(8, 9);

        connectAToB(1, 4); connectAToB(2, 5); connectAToB(3, 6);
        connectAToB(4, 7); connectAToB(5, 8); connectAToB(6, 9);

        while(!pointAdjacentToTile(xStart, yStart, Tile.FLOOR)){
            xStart += random.nextInt(3) - 1;
            yStart += random.nextInt(3) - 1;
        }
        tiles[xStart][yStart] = Tile.START_FLOOR;


        int sx = xStart / (width / 3);
        int sy = yStart / (height / 3);
        int startRoom = (sx + 1) + (sy * 3);

        int lastRoom = startRoom;
        while(lastRoom == startRoom){
            lastRoom = random.nextInt(9) + 1;
        }

        putTileInRoom(Tile.STAIRS, lastRoom);

        spawnRogueMonsters(startRoom, lastRoom);
        spawnRogueTreasure();







        return new World(tiles, events, items);
    }

    static void spawnRogueTreasure(){
        for(int i=1; i<10; i++){
            int itemPoints = random.nextInt(4);
            while(itemPoints > 0){
                itemPoints --;
                putItemInRoom(ItemList.get(random), i);
            }
        }
    }

    static void spawnRogueMonsters(int startRoom, int lastRoom){
        for(int i=1; i<10; i++){
            int monsterPoints = random.nextInt(6) + random.nextInt(6) + random.nextInt(6) + 3;
            if(i == startRoom){ monsterPoints = monsterPoints / 2; }
            if(i == lastRoom){ monsterPoints += monsterPoints / 2; }
            int swapChance = random.nextInt(30) + 41;
            String monsterType = pickBreed();

            while (monsterPoints > 0){
                putCreatureInRoom(monsterType, i);
                monsterPoints -= monsterCost;
                if(random.nextInt(100) >= swapChance){ monsterType = pickBreed(); }
            }
        }
    }



    static void connectAToB(int r1, int r2){
        Cords c1 = WorldBuilder.pickSpotInRoom(r1);
        int x = c1.x; int y = c1.y;

        Cords c2 = WorldBuilder.pickSpotInRoom(r2);
        int x2 = c2.x; int y2 = c2.y;

        while(roomNumbers[x][y] != r2){
            if(x != x2){
                x += x < x2 ? 1 : -1;
            } else {
                y += y < y2 ? 1 : -1;
            }

            tiles[x][y] = Tile.FLOOR;
        }
    }


    static void nineRooms(){
        for(int n=0; n<9; n++){

            int xp = n * 45;
            int yp = 0;
            while(xp >= 135){

                xp -= 135;
                yp += 12;
            }

            int xOff = random.nextInt(5);
            int yOff = random.nextInt(2);
            //xOff = 0; yOff = 0;

            int x1 = 8 + xp + xOff;
            int y1 = 3 + yp + yOff;

            int w = random.nextInt(22) + 11 - xOff;
            int h = random.nextInt(4) + 5 - yOff;
            //w = 32; h = 8;

            int x2 = x1 + w;
            int y2 = y1 + h;

            squareRoom(x1, y1, x2, y2, n+1);
        }
    }

    static void squareRoom(int x1, int y1, int x2, int y2, int n){
        for(int x=x1; x<=x2; x++){
            for(int y=y1; y<y2; y++){
                tiles[x][y] = Tile.FLOOR;
                roomNumbers[x][y] = n;
            }
        }
    }
}
