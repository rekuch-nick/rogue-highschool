package com.rekuchn;

import com.rekuchn.creature.Creature;
import com.rekuchn.model.*;
import com.rekuchn.service.Cords;
import com.rekuchn.service.EffectManager;
import com.rekuchn.service.Line;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private Tile[][] tiles;
    private char[][] memoryTiles;
    private int width;
    public int width() { return width; }
    public Random random = new Random();
    private int height;
    public int height() { return height; }
    public List<EventTrigger> events;

    public void setTile(int x, int y, Tile t){
        tiles[x][y] = t;
    }

    public List<Creature> creatures;
    public Creature creature(int x, int y){
        for(Creature c : creatures){
            if(c.x == x && c.y == y){ return c; }
        }
        return null;
    }

    public Creature player(){
        for(Creature c : creatures){
            if(c.isPlayer){ return c; }
        }
        return null;
    }

    public Item[][] items;
    public Item item(int x, int y){
        return items[x][y];
    }

    private List<Effect> effects;
    public Effect effect(int x, int y){
        for(Effect e : effects) {
            if(e.x == x && e.y == y){ return e; }
        }
        return null;
    }
    public List<Effect> effects(){ return effects; }
    public void remove(Effect e){
        effects.remove(e);
    }

    public boolean levelUp;

    public World(Tile[][] tiles, List<EventTrigger> events, Item[][] items){
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.creatures = new ArrayList<>();
        this.memoryTiles = new char[width][height];
        for(int x = 0; x<width; x++){ for(int y = 0; y<height; y++){
            memoryTiles[x][y] = ' ';
        }}
        this.items = items;
        effects = new ArrayList<>();
        this.events = events;
        levelUp = false;
    }

    public Tile tile(int x, int y){
        if (x < 0 || x >= width || y < 0 || y >= height)
            return Tile.BOUNDS;
        else
            return tiles[x][y];
    }
    public Cords getTileCords(Tile t) {
        for(int x = 0; x < width; x++) { for(int y = 0; y < height; y++) {
            if (t == tiles[x][y]) {
                return new Cords(x, y);
            }
        }}
        return null;
    }


    public char glyph(int x, int y){
        return tile(x, y).glyph();
    }

    public Color color(int x, int y){
        return tile(x, y).color();
    }

    public char[][] memoryTiles() {
        return memoryTiles;
    }

    public Cords getEmptyLocation(){
        int x, y;
        int tries = 0;
        do {
            tries ++;
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
            if(tries > 1000000){ break; }
        }
        while (!tile(x,y).canHaveItem() || creature(x, y) != null || item(x, y) != null);

        return new Cords(x, y);
    }

    public void addAtEmptyLocation(Creature creature){
        int x, y;

        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        }
        while (!tile(x,y).canHaveItem() || creature(x, y) != null);

        creature.x = x;
        creature.y = y;
        creatures.add(creature);
    }

    public void addAtEmptyLocation(Item item){
        int x, y;

        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        }
        while (items[x][y] != null || !tile(x, y).canHaveItem());

        items[x][y] = item;
    }


    public void addAtTile(Creature creature, Tile t){
        int x=0; int y=0;
        int tries = 0;

        do {
            tries ++;
            if(tries > 100000){ break; }
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        }
        while (tile(x,y) != t || creature(x, y) != null);

        creature.x = x;
        creature.y = y;
    }

    public Cords creatureFreeTileNear(Tile t, int tx, int ty){
        int x=0; int y=0;
        int tries = 0;
        boolean tooFar = false;

        do {
            tries ++;
            tooFar = false;
            if(tries > 100000){ return null; }
            x = (int)(Math.random() * width-2)+1;
            y = (int)(Math.random() * height-2)+1;
            int d = Math.abs(x - tx) + Math.abs(y - ty);
            if(d > 5){ tooFar = true; }
        }
        while (tile(x,y) != t || creature(x, y) != null || tooFar);


        return new Cords(x, y);
    }

    public boolean addAtEmptySpace(Item item, int xc, int yc){
        for(int range=0; range<2; range++){

            for(int x=xc-range; x<=xc+range; x++) {
                for (int y=yc-range; y<=yc+range; y++) {
                    if(!Cords.inBounds(x, y)){ continue; }
                    if(tile(x, y).canHaveItem() && item(x, y) == null){
                        items[x][y] = item;
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public Cords spotForItemBy(int x, int y){
        for(int tx=x-1; tx<=x+2; tx++){
            for(int ty=y-1; ty<=y+1; ty ++){
                if(tile(tx, ty).canHaveItem() && item(tx, ty) == null){
                    return new Cords(tx, ty);
                }
            }
        }
        return null;
    }



    public void openDoor(int x, int y){
        tiles[x][y] = Tile.OPEN_DOOR;
    }

    public void remove(Creature other){
        creatures.remove(other);
    }

    public void remove(int x, int y) {
        items[x][y] = null;
    }

    public void update(){

        for(EventTrigger e : events){
            if(e.trigger.equals("Lava Flow")){ e.resolve(this, player()); }
            if(e.trigger.equals("Fire Tile")){ e.resolve(this, player()); }
            if(e.trigger.equals("Wolf Call")){ e.resolve(this, player()); }

        }

        List<Creature> toUpdate = new ArrayList<>(creatures);
        for (Creature creature : toUpdate){
            if(player() == null){ break; }
            creature.update();
        }

        for(Creature c : creatures){ c.justSwapped = false; }


    }

    public void swapCreatures(Creature c1, Creature c2){
        int tx = c1.x; int ty = c1.y;
        c1.x = c2.x; c1.y = c2.y;
        c2.x = tx; c2.y = ty;
    }


    public boolean lineBetween(int x1, int y1, int x2, int y2) {

        for(Cords p : new Line(x1, y1, x2, y2)){
            if(p.x == x1 && p.y == y1){ continue; }
            if(p.x == x2 && p.y == y2){ continue; }
            if(!tile(p.x, p.y).canSeeThrough()){ return false; }
            if(creature(p.x, p.y) != null){ return false; }
        }
        return true;
    }
    public boolean lineBetweenIgnoreCreatures(int x1, int y1, int x2, int y2) {

        for(Cords p : new Line(x1, y1, x2, y2)){
            if(p.x == x1 && p.y == y1){ continue; }
            if(p.x == x2 && p.y == y2){ continue; }
            if(!tile(p.x, p.y).canSeeThrough()){ return false; }
        }
        return true;
    }


    public Line getLineBetween(int x1, int y1, int x2, int y2){
        Line line = new Line(x1, y1, x2, y2);
        List<Cords> goodPoints = new ArrayList<>();
        for(Cords c : line){
            if(lineBetween(x1, y1, c.x, c.y)){
                goodPoints.add(c);
            }
        }
        return new Line(goodPoints);
    }



    public void eventTest(String trigger, int x, int y, Creature player){
        List<EventTrigger> temp = new ArrayList<>(events);
        for(EventTrigger e : temp){
            if(e.trigger.equals(trigger) && x == e.xPoint && y == e.yPoint){
                e.resolve(this, player);
                events.remove(e);
            }

        }
    }

    public void unsealShrine(){
        Cords c = findRandomTile(Tile.EVENT_DOOR);
        if(c != null){
            player().doAction(AsciiPanel.brightYellow, "hear the shrine unseal");
            tiles[c.x][c.y] = Tile.OPEN_DOOR;
            EffectManager.splash(c.x, c.y, this);

        }
    }

    public Cords findRandomTile(Tile t){
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        int sx = x; int sy = y;

        do{
            if(tile(x, y) == t){ return new Cords(x, y); }

            x ++;
            if(x >= width){
                x = 0; y ++;
            }
            if(y >= height){ y = 0; }
        } while(sx != x || sy != y);

        return null;
    }


    public void burnTile(int x, int y){
        if(tiles[x][y].canBurn()){
            setTile(x, y, Tile.FLOOR);
        }
    }


    public Creature putCreatureInRangeWide(Creature c, int sx, int sy, int r){

        int tries = 0;
        while(tries < 10000){
            tries ++;

            int x = sx + random.nextInt(r * 2) - r;
            int y = sy + random.nextInt(r * 2) - r;
            if(Math.abs(sx - x) + Math.abs(sy - y) > r){ continue; }
            if(x < 0 || y < 0 || x >= width || y >= height){ continue; }
            if(!tiles[x][y].canEnter()){ continue; }
            if(items[x][y] != null && !items[x][y].equals("")){ continue; }
            if(creature(x, y) != null){ continue; }

            int adjacentBlocks = 0;
            for(int xx=x-1; xx<=x+1; xx++){
                for(int yy=y-1; yy<=y+1; yy++){
                    if(!tiles[xx][yy].canEnter()){
                        adjacentBlocks ++;
                    }
                }
            }

            if(adjacentBlocks == 0){
                creatures.add(c);
                c.x = x; c.y = y;
                return c;
            }
        }
        return null;
    }


}