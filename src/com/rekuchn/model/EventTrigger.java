package com.rekuchn.model;

import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.creature.CreatureList;
import com.rekuchn.service.Cords;
import com.rekuchn.service.EffectManager;
import com.rekuchn.worldgen.WorldBuilder;

import static com.rekuchn.worldgen.WorldBuilder.height;
import static com.rekuchn.worldgen.WorldBuilder.width;

public class EventTrigger {

    public String trigger;
    public int xPoint;
    public int yPoint;
    public int xObject;
    public int yObject;

    public int cd;
    public int cdMax;
    public String note;


    public EventTrigger(String trigger, int xPoint, int yPoint, int xObject, int yObject) {
        this.trigger = trigger;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
        this.xObject = xObject;
        this.yObject = yObject;
        cd = -1;
        this.note = "";
    }

    public EventTrigger(String trigger, int xPoint, int yPoint, int xObject, int yObject, String note) {
        this.trigger = trigger;
        this.xPoint = xPoint;
        this.yPoint = yPoint;
        this.xObject = xObject;
        this.yObject = yObject;
        cd = -1;
        this.note = note;
    }

    public void resolve(World world, Creature player){

        if(trigger.equals("Grabbed Key")){
            world.setTile(xObject, yObject, Tile.FLOOR);

            new ObjectFactory(world).newCreature(CreatureList.appropriateOgre(), xObject, yObject);
            player.notify(AsciiPanel.brightRed, "A statue rumbles and comes to life.");

            EffectManager.splash(xObject, yObject, world);
        }

        if(trigger.equals("Pull Lever")){

            if(note.equals("Bomb")){
                for(int x=xPoint-1; x<=xPoint+1; x++){ for(int y=yPoint-1; y<=yPoint+1; y++){
                    world.effects().add(new Effect(x, y, AsciiPanel.brightYellow, AsciiPanel.brightRed, 0, 0, 0, 0, 6));
                    if(world.creature(x, y) != null){
                        world.creature(x, y).takeDamage(6, "Fire");
                    }
                }}
                player.notify(AsciiPanel.red, "The lever sets off a bomb.");
            }

            if(note.equals("Item")){
                //Item item = ItemList.get( ItemList.itemStringByType("Potion", world.random.nextInt(100) + 1) );
                Item item = ItemList.get("Potion", world.random);
                world.setTile(xObject, yObject, Tile.FLOOR);
                if(world.addAtEmptySpace(item, xObject, yObject)){
                    for(int x=xObject-1; x<=xObject+1; x++){ for(int y=yObject-1; y<=yObject+1; y++){
                        world.effects().add(new Effect(x, y, AsciiPanel.brightWhite, AsciiPanel.brightBlue, 0, 0, 0, 0, 6));
                    }}
                    player.notify(AsciiPanel.brightYellow, "The lever reveals a hidden treasure.");
                }
            }

            if(note.equals("Shrine")){
                for(int x=0; x<width; x++){ for(int y=0; y<height; y++){
                    if(world.tile(x, y) == Tile.EVENT_DOOR){

                        world.setTile(x, y, Tile.FLOOR);
                        for(int xx=x-1; xx<=x+1; xx++){ for(int yy=y-1; yy<=y+1; yy++){
                            world.effects().add(new Effect(xx, yy, AsciiPanel.brightWhite, AsciiPanel.brightBlue, 0, 0, 0, 0, 6));
                        }}
                        player.notify(AsciiPanel.brightYellow, "The lever opens the sealed shrine.");

                    }
                }}

            }


            //world.setTile(xObject, yObject, Tile.FLOOR);

            //new ObjectFactory(world).newCreature("Ogre", xObject, yObject);
            //player.notify(AsciiPanel.brightRed, "A statue rumbles and comes to life.");

            //EffectManager.splash(xObject, yObject, world);
        }


        if(trigger.equals("Water Step")){
            int r = world.random.nextInt(100);
            int spawnChance = 20;
            if(r >= spawnChance){ return; }
            Cords c = world.creatureFreeTileNear(Tile.WATER, player.x, player.y);
            if(c != null){
                ObjectFactory cf = new ObjectFactory(world);
                Creature fish = cf.newCreature(CreatureList.appropriateFish(), c.x, c.y);
                fish.doAction(AsciiPanel.orange, "swim to the surface");
            }

        }



        if(trigger.equals("Wolf Call")){
            if(cd == -1){ cd = 16; cdMax = 17; }
            cd --;
            if(cd < 1) {
                cd = cdMax; cdMax += world.random.nextInt(2) + 1;
                player.notify(AsciiPanel.red, "A wolf howls in the distance.");
                new ObjectFactory(world).newCreatureAnywhereFar(CreatureList.randomLongWolf(world.random).name, player.x, player.y);
            }

        }

        if(trigger.equals("Fire Tile")){
            if(cd == -1){ cd = 4; }
            if(world.random.nextBoolean()){ cd --; }
            if(cd < 1){
                cd = 8;
                if(world.tile(xPoint, yPoint) == Tile.FIRE_1){ world.setTile(xPoint, yPoint, Tile.FLOOR); }
                if(world.tile(xPoint, yPoint) == Tile.FIRE_2){ world.setTile(xPoint, yPoint, Tile.FIRE_1); }
                if(world.tile(xPoint, yPoint) == Tile.FIRE_3){ world.setTile(xPoint, yPoint, Tile.FIRE_2); }
                if(world.tile(xPoint, yPoint) == Tile.FIRE_4){ world.setTile(xPoint, yPoint, Tile.FIRE_3); }
            }
        }

        if(trigger.equals("Lava Flow")){
            if(cd == -1){ cd = 6; }
            cd --;
            if(cd < 1){
                cd = 6;

                for(int x=1; x<world.width()-1; x++){
                    for(int y=1; y<world.height()-1; y++){
                        if(world.tile(x, y).canOverWrite()){


                            if(world.tile(x-1, y) == Tile.LAVA_BLOCK || world.tile(x-1, y) == Tile.LAVA ||
                                    world.tile(x+1, y) == Tile.LAVA_BLOCK || world.tile(x+1, y) == Tile.LAVA ||
                                    world.tile(x, y-1) == Tile.LAVA_BLOCK || world.tile(x, y-1) == Tile.LAVA ||
                                    world.tile(x, y+1) == Tile.LAVA_BLOCK || world.tile(x, y+1) == Tile.LAVA ){

                                world.setTile(x, y, Tile.GROW);
                                world.items[x][y] = null;
                            }


                        }
                    }
                }
                WorldBuilder.setTileAToTileB(Tile.GROW, Tile.LAVA);

            }

        }


    }





}
