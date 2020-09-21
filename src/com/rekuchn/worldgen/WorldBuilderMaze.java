package com.rekuchn.worldgen;

import com.rekuchn.World;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import java.util.ArrayList;
import java.util.List;

import static com.rekuchn.worldgen.WorldBuilder.*;

public class WorldBuilderMaze {

    public static World create(){

        setAllTilesTo(Tile.WALL);
        setQuarterTilesTo(Tile.NODE);


        if(xStart % 2 == 0){ xStart ++; if(xStart >= width){ xStart -= 2; }}
        if(yStart % 2 == 0){ yStart ++; if(xStart >= height){ yStart -= 2; }}
        tiles[xStart][yStart] = Tile.START_FLOOR;



        perfectMaze(xStart, yStart);

        setTileAToTileB(Tile.WALL, Tile.LAVA);

        fancyWalls();

        putCreatureInRoom("Hobgoblin", 0);
        putCreatureInRoom("Hobgoblin", 0);
        putCreatureInRoom("Hobgoblin", 0);
        putCreatureInRoom("Hobgoblin", 0);
        putCreatureInRoom("Hobgoblin", 0);





        return new World(tiles, events, items);
    }

    static void perfectMaze(int cx, int cy){
        int nodes = countTile(Tile.NODE);
        List<Cords> previous = new ArrayList<>();


        boolean d1 = false; boolean d2 = false; boolean d3 = false; boolean d4 = false;

        while(nodes > 0){

            int mx=0; int my=0;
            d1 = false; d2 = false; d3 = false; d4 = false;
            boolean foundDirection = false;
            while(!foundDirection){
                int dir = random.nextInt(4) + 1;
                mx = 0; my = 0;

                if(d1 && d2 && d3 && d4){ break; }

                if(dir == 1){ my = -2; d1 = true;}
                if(dir == 2){ mx =  2; d2 = true;}
                if(dir == 3){ my =  2; d3 = true;}
                if(dir == 4){ mx = -2; d4 = true;}

                if(cx+mx < 0 || cy+my < 0 || cx+mx >= width || cy+my >= height){ continue; }
                if(tiles[cx+mx][cy+my] != Tile.NODE){ continue; }

                foundDirection = true;
            }
            if(foundDirection){

                nodes --;
                tiles[cx+mx][cy+my] = Tile.FLOOR;
                tiles[cx+(mx/2)][cy+(my/2)] = Tile.FLOOR;
                previous.add(new Cords(cx, cy));
                cx = cx + mx; cy = cy + my;

            } else {

                if(previous.size() < 1){ return; }

                Cords c = previous.get(previous.size() - 1);
                previous.remove(c);
                cx = c.x; cy = c.y;
            }

        }
    }
}
