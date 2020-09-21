package com.rekuchn.model;


import com.rekuchn.AsciiPanel;
import com.rekuchn.Global;
import com.rekuchn.creature.*;
import com.rekuchn.model.*;
import com.rekuchn.World;
import com.rekuchn.service.Cords;
import com.rekuchn.spell.*;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class ObjectFactory {
    private World world;
    public World getWorld() { return world; }
    public void setWorld(World world) { this.world = world; }


    public ObjectFactory(World world){
        this.world = world;
    }

    public Creature newCreature(String name){
        Cords c = world.getEmptyLocation();
        return newCreature(name, c.x, c.y);
    }

    public Creature newCreature(String name, int x, int y){

        if(name.equals("Fungus")){
            Creature creature = new Creature(name, world, 'f', AsciiPanel.green, 4, 0,  0, 0, 0);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new FungusAi(creature, this);
            return creature;
        }


        if(name.equals("Bat")){
            Creature creature = new Creature(name, world, 'b', AsciiPanel.brightBlue, 2,  0, 50, 0, 6);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new BatAi(creature);
            return creature;
        }

        if(name.equals("Wolf")){
            Creature creature = new Creature(name, world, 'd', AsciiPanel.red, 15,  0, 15, 0, 10);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new HunterAi(creature);
            return creature;
        }
        if(name.equals("Fenrir")){
            Creature creature = new Creature(name, world, 'd', AsciiPanel.brightWhite, 20,  0, 15, 0, 10, "Striking");
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new LongHunterAi(creature);
            return creature;
        }
        if(name.equals("Wolf Skeleton")){
            Creature creature = new Creature(name, world, 'd', Color.darkGray, 5,  0, 0, 3, 10);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new HunterAi(creature);
            return creature;
        }

        if(name.equals("Rat")){
            Creature creature = new Creature(name, world, 'r', AsciiPanel.brown, 4,  0, 15, 0, 12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new HunterAi(creature);
            return creature;
        }
        if(name.equals("Albino Rat")){
            Creature creature = new Creature(name, world, 'r', AsciiPanel.brightWhite, 9,  0, 15, 0, 12, "Striking");
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new LongHunterAi(creature);
            return creature;
        }
        if(name.equals("Rat Matron")){
            Creature creature = new Creature(name, world, 'r', AsciiPanel.orange, 14,  30, 15, 0, 4);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Chittering Call", new SummonMonster("Rat"), 10, creature) );
            spells.add( new Spell("Chittering Call", new SummonMonster("Rat"), 10, creature) );
            spells.add( new Spell("Chittering Call", new SummonMonster("Rat"), 10, creature) );
            spells.add( new Spell("Chittering Call", new SummonMonster("Albino Rat"), 30, creature) );
            new MageAi(creature, spells, 40);
            return creature;
        }



        if(name.equals("Lynx")){
            Creature creature = new Creature(name, world, 'l', AsciiPanel.red, 15,  0, 15, 0, 10, "Striking", "Rending");
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new HunterAi(creature);
            return creature;
        }


        if(name.equals("Grey Ogre")){
            Creature creature = new Creature(name, world, 'O', AsciiPanel.grey, 22,  0, 0, 0, 8);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            new BruteAi(creature);
            return creature;
        }
        if(name.equals("Ogre")){
            Creature creature = new Creature(name, world, 'O', Color.orange, 55,  0, 0, 0, 20);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            new BruteAi(creature);
            return creature;
        }
        if(name.equals("Ogre Mage")){
            Creature creature = new Creature(name, world, 'O', AsciiPanel.cyan, 55, 40,  0, 0, 20);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Animate Bones", new SummonMonster("Wolf Skeleton"), 10, creature) );
            spells.add( new Spell("Animate Bones", new SummonMonster("Wolf Skeleton"), 10, creature) );
            spells.add( new Spell("Web", new CreateWebs(), 10, creature) );
            new MageAi(creature, spells, 50);
            return creature;
        }
        if(name.equals("Salamander")){
            Creature creature = new Creature(name, world, 's', AsciiPanel.brightRed, 4,  20, 10, 0, 12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new SpitterAi(creature);
            return creature;
        }
        if(name.equals("Frog")){
            Creature creature = new Creature(name, world, 's', AsciiPanel.orange, 20,  0, 10, 0, 12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new FrogAi(creature);
            return creature;
        }
        if(name.equals("Snake")){
            Creature creature = new Creature(name, world, 's', AsciiPanel.brightGreen, 10,  0, 10, 0, 6, "Poison Strike");
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new BatAi(creature);
            return creature;
        }

        if(name.equals("Mole Man")){
            Creature creature = new Creature(name, world, 'm', AsciiPanel.orange, 5, 10, 10, 0,  8);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 2, 0);
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Needle", new RangedAttack(), 0, creature) );
            spells.add( new Spell("Needle", new RangedAttack(), 0, creature) );
            spells.add( new Spell("Needle", new RangedAttack("Blinding"), 0, creature) );
            spells.add( new Spell("Grease", new ThrowGrease(), 10, creature) );
            new BattleMageAi(creature, spells, 80);
            return creature;
        }




        if(name.equals("Random Gob")){
            int r = world.random.nextInt(6) + 1;
            name = "Goblin";
            if(r == 1 && Global.level > 10){ name = "Gob Drummer"; }
            if(r == 2 && Global.level > 10){ name = "Gob Pyro"; }
            if(r == 3 && Global.level > 10){ name = "Gob Striker"; }
        }


        if(name.equals("Stalker")){
            Creature creature = new Creature(name, world, 'S', AsciiPanel.brightBlue, 8,  0, 50, 0, 18);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            Item i = ItemList.get("Weapon", world.random); creature.inventory().add(i); creature.worn[1] = i;
            new GoblinAi(creature);
            return creature;
        }



        if(name.equals("Gob Drummer")){
            Creature creature = new Creature(name, world, 'g', AsciiPanel.yellow, 8, 60, 10, 0,  4);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            Item i = ItemList.get("Armor", world.random); creature.inventory().add(i); creature.worn[1] = i;
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Paradiddle", new AoEStatus("Mighty", AsciiPanel.red, 11, false, 10), 5, creature) );
            new MageAi(creature, spells, 40);
            return creature;
        }
        if(name.equals("Goblin")){
            Creature creature = new Creature(name, world, 'g', AsciiPanel.brightGreen, 8,  0, 10, 0, 12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            Item i = ItemList.get("Armor", world.random); creature.inventory().add(i); creature.worn[1] = i;
            new GoblinAi(creature);
            return creature;
        }
        if(name.equals("Gob Pyro")){
            Creature creature = new Creature(name, world, 'g', AsciiPanel.red, 8, 20, 10, 0,  12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            Item i = ItemList.get("Armor", world.random); creature.inventory().add(i); creature.worn[1] = i;
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Fire Bolt", new FireBolt(), 5, creature) );
            spells.add( new Spell("Inferno", new FirePatch(), 5, creature) );
            new BattleMageAi(creature, spells, 40);
            return creature;
        }
        if(name.equals("Gob Striker")){
            Creature creature = new Creature(name, world, 'g', AsciiPanel.brown, 8, 5, 10,  0, 12);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            holdsYen(creature, 3, -1);
            Item i = ItemList.get("Armor", world.random); creature.inventory().add(i); creature.worn[1] = i;
            List<Spell> spells = new ArrayList<>();
            spells.add( new Spell("Stoning", new RockThrow(), 5, creature) );
            new BattleMageAi(creature, spells, 60);
            return creature;
        }

        if(name.equals("Slime")){
            Creature creature = new Creature(name, world, 'o', AsciiPanel.cyanMid, 30, 0, 0,  0, 6);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new JellyAi(creature);
            return creature;
        }
        if(name.equals("Jelly")){
            Creature creature = new Creature(name, world, 'o', AsciiPanel.pink, 80, 0, 0,  0, 6);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new JellyAi(creature);
            return creature;
        }

        if(name.equals("Kannushi")){
            Creature creature = new Creature(name, world, 'K', AsciiPanel.brightCyan, 35, 0, 20,  2, 8);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new BatAi(creature);
            return creature;
        }


        if(name.equals("Carp")){
            Creature creature = new Creature(name, world, 'c', AsciiPanel.orange, 10,  0, 10, 0, 8);
            creature.x = x; creature.y = y; world.creatures.add(creature);
            new FishAi(creature);
            return creature;
        }





        if(name.equals("Waifu")){
            String waifuName = "";
            try {
                waifuName = Waifu.newWaifuName(world).getName();
            } catch (NullPointerException e){ return null; }

            Creature creature = new Creature(waifuName, world, (char)190, AsciiPanel.brightMagenta, 250, 200,  100, 10, 10);
            creature.x = x; creature.y = y;
            world.creatures.add(creature);
            new WaifuAi(creature);
            return creature;
        }

        return null;
    }


    public Creature newFungus(Creature parent, int hp){
        Creature creature = new Creature(parent.name, world, parent.glyph(), parent.color(), hp, parent.mmp(), parent.evade(), parent.defend(), parent.might());

        world.addAtEmptyLocation(creature);
        new FungusAi(creature, this);
        return creature;
    }

    public Creature newJelly(int hp, Creature par){
        Creature creature = new Creature(par.name, world, par.glyph(), par.color(), hp, par.mmp(), par.evade(), par.defend(), par.might());

        world.addAtEmptyLocation(creature);
        new JellyAi(creature);
        return creature;
    }



    public Creature newWaifu(){
        String name;
        try {
            name = Waifu.newWaifuName(world).getName();
        } catch (NullPointerException e){ return null; }
        Creature waifu = new Creature(name, world, (char)190, AsciiPanel.brightMagenta, 250, 250,  10, 10, 50);
        //world.addAtEmptyLocation(waifu);
        new WaifuAi(waifu);
        return waifu;
    }

    public Creature newPlayer(){
        Creature player = new Creature("Hero", world, (char)189, AsciiPanel.brightWhite,  100, 20, 30, 0, 6);
        world.addAtTile(player, Tile.START_FLOOR);
        new PlayerAi(player);
        world.creatures.add(player);
        return player;
    }

    public Creature newPetDragon(){
        //Creature creature = new Creature(par.name, world, (char)182, AsciiPanel.brightTeal, 100, 0, 30, 4, 4);
        //world.addAtEmptyLocation(creature);
        //new PetAi(creature);
        return null;
    }

    public Creature newPlayer(Creature player){

        player.setWorld(world);
        world.addAtTile(player, Tile.START_FLOOR);

        world.creatures.add(player);
        player.inventory().removeAll("Key");
        if(player.hasTrait("Sick")){

        } else {
            player.hp = player.mhp();
        }
        player.mp = player.mmp();
        return player;
    }


    public Creature newCreatureAt(String name, int x, int y){

        int range = 1;
        while(range < 10){
            int xPoint = x + world.random.nextInt((range * 2) + 1) - range - 2;
            int yPoint = y + world.random.nextInt((range * 2) + 1) - range - 2;
            if(world.random.nextInt(10) == 0){ range ++; }

            if(xPoint < 1 || yPoint < 1 || xPoint >= world.width() - 1 || yPoint >= world.height() - 1){
                continue;
            }

            if(world.creature(xPoint, yPoint) == null && world.tile(xPoint, yPoint).canEnter()){
                return newCreature(name, xPoint, yPoint);
            }
        }

        return null;
    }

    public Creature newCreatureAnywhereFar(String name, int xPoint, int yPoint){

        int xBest = -1; int yBest = -1;
        int best = Integer.MIN_VALUE;

        for(int i=0; i<50000; i++){
            int x = world.random.nextInt(world.width()-2)+1;
            int y = world.random.nextInt(world.height()-2)+1;

            if(world.tile(x, y).canEnter() && world.creature(x, y) == null){
                int d = Math.abs(x - xPoint) + Math.abs(y - yPoint);
                if(d > best){
                    best = d; xBest = x; yBest = y;
                }
            }
        }

        if(xBest == -1){ return null; }
        return newCreature(name, xBest, yBest);
    }







    public Item newItemAtRandom(Item item){
        Cords c = world.getEmptyLocation();
        world.items[c.x][c.y] = item;
        return item;
    }

    public Item newItemAt(Item item, int x, int y){
        if(world.item(x, y) != null || !world.tile(x, y).canHaveItem() ){
            Cords newSpot = world.spotForItemBy(x, y);
            if(newSpot == null){ return null; }
            x = newSpot.x; y = newSpot.y;
        }
        world.items[x][y] = item;
        return item;
    }



    private void holdsYen(Creature creature, int dice, int plus){
        Item i = ItemList.get("Yen");

        int r = world.random.nextInt(dice) + plus;
        for(int x=0; x<r; x++) {
            creature.inventory().add(i);
        }

    }







}
