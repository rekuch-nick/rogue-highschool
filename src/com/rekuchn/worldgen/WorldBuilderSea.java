package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.ItemList;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import static com.rekuchn.Global.level;
import static com.rekuchn.worldgen.WorldBuilder.*;
import static com.rekuchn.worldgen.WorldBuilderSchoolEntry.treeBorder;

public class WorldBuilderSea {

    public static World create(){

        border();
        randomizeTiles(Tile.GRASS, Tile.WATER);
        smooth(8, Tile.GRASS, Tile.WATER);

        afterBorder();


        if(tiles[xStart][yStart] == Tile.WATER){
            if(xStart < 70){
                while(tiles[xStart][yStart] == Tile.WATER){
                    xStart ++;
                    if(xStart >= width-1){ break; }
                }
            } else {
                while(tiles[xStart][yStart] == Tile.WATER){
                    xStart --;
                    if(xStart <= 1){ break; }
                }
            }
        }



        // random wandering monsters
        int monsterPoints = 34 + (level * 2);
        String monsterType = pickBreed();
        while(monsterPoints >= monsterCost){
            monsterPoints -= monsterCost;
            putCreatureAnywhere(monsterType);
            monsterType = pickBreed();
        }








        maybeSetTileAToTileB(Tile.GRASS, Tile.GRASS2, 33);
        maybeSetTileAToTileB(Tile.GRASS, Tile.GRASS3, 33);
        maybeSetTileAToTileB(Tile.GRASS, Tile.GRASS4, 33);


        tiles[xStart][yStart] = Tile.START_FLOOR;
        putTileAnywhereBut(Tile.STAIRS, Tile.WATER, xStart, yStart);




        //putTileInRoom(Tile.STAIRS, lastRoom);
        //events.add(new EventTrigger("Wolf Call", 0, 0, 0, 0));

        events.add(new EventTrigger("Water Step", -1, -1, -1, -1));

        return new World(tiles, events, items);
    }


    static void fillHouse(int room){

            int monsterPoints = 10;
            String monsterType = pickBreed();
            while(monsterPoints >= monsterCost){
                monsterPoints -= monsterCost;
                putCreatureInRoom(monsterType, room);
            }


            int itemPoints = random.nextInt(6);
            while(itemPoints > 0){
                itemPoints --;
                putItemInRoom(ItemList.get(random), room);
            }


    }


}
