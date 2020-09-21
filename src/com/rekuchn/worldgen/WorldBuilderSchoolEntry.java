package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.Tile;

import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderSchoolEntry {

    public static World create(){

        randomizeTiles(Tile.GRASS, Tile.GRASS3);
        smooth(8, Tile.GRASS, Tile.GRASS3);

        treeBorder();


        for(int x=0 + 30; x < width - 30; x ++){
            for(int y=6; y < 16; y ++){
                tiles[x][y] = Tile.WALL;
            }
        }
        for(int x=0 + 31; x < width - 31; x ++){
            for(int y=7; y < 15; y ++){
                tiles[x][y] = Tile.FLOOR;
            }
        }
        tiles[40][15] = Tile.DOOR;
        tiles[50][15] = Tile.DOOR;
        tiles[60][15] = Tile.DOOR;
        tiles[70][15] = Tile.DOOR;
        tiles[80][15] = Tile.DOOR;
        tiles[90][15] = Tile.DOOR;
        tiles[100][15] = Tile.DOOR;


        fancyWalls();


        tiles[70][33] = Tile.START_FLOOR;

        for(int i=0; i<12; i++){
            putCreatureAnywhereWide("Waifu");
        }

        tiles[70][7] = Tile.STAIRS;
        //tiles[31][14] = Tile.STAIRS;
        //tiles[109][7] = Tile.STAIRS;
        //tiles[109][14] = Tile.STAIRS;

        return new World(tiles, events, items);
    }

    static void treeBorder(){
        for(int x=0; x<width; x++){
            tiles[x][0] = Tile.BUSH;
            if(random.nextBoolean()){ tiles[x][1] = Tile.BUSH; }
            //if(random.nextBoolean()){ tiles[x][2] = Tile.BUSH; }
            //if(random.nextBoolean()){ tiles[x][height - 3] = Tile.BUSH; }
            if(random.nextBoolean()){ tiles[x][height - 2] = Tile.BUSH; }
            tiles[x][height - 1] = Tile.BUSH;
        }
        for(int y=0; y<height; y++){
            tiles[0][y] = Tile.BUSH;
            if(random.nextBoolean()){ tiles[1][y] = Tile.BUSH; }
            if(random.nextBoolean()){ tiles[2][y] = Tile.BUSH; }
            //if(random.nextBoolean()){ tiles[3][y] = Tile.BUSH; }
            //if(random.nextBoolean()){ tiles[4][y] = Tile.BUSH; }

            //if(random.nextBoolean()){ tiles[width - 4][y] = Tile.BUSH; }
            if(random.nextBoolean()){ tiles[width - 3][y] = Tile.BUSH; }
            if(random.nextBoolean()){ tiles[width - 2][y] = Tile.BUSH; }
            tiles[width - 1][y] = Tile.BUSH;
        }

    }

}
