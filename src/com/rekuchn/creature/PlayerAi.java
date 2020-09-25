package com.rekuchn.creature;


import com.rekuchn.AsciiPanel;
import com.rekuchn.model.*;
import com.rekuchn.service.EffectManager;
import com.rekuchn.spell.Action;

import java.awt.*;
import java.util.Random;

import static com.rekuchn.Global.*;

public class PlayerAi extends CreatureAi {





    public PlayerAi(Creature creature) {

        super(creature);
        creature.hostile = false;
        creature.isPlayer = true;
        creature.visionRadius = 20;
        creature.swimChance = 101;
        creature.hearts = 9;
        creature.cash = 0;

    }

    public void onEnter(int x, int y, Tile tile){ onEnter(x, y, tile, false); }

    public void onEnter(int x, int y, Tile tile, boolean pushed){

        if(tile == Tile.WALL && x > 1 && y > 1 && x < creature.world().width() - 1 && y < creature.world().height() - 1 ){
            creature.digTime ++;
            if(creature.digTime >= 6 || creature.hasTrait("Mole Hands")){
                creature.digTime = 0;
                //creature.takeDamage(1, "Digging");
                creature.doAction(AsciiPanel.orange, "manage to break the wall");
                creature.openDoor(x, y);
            } else {
                creature.doAction(AsciiPanel.brown, "chip away at the wall");
            }
        }

        if(tile == Tile.DOOR){
            creature.openDoor(x, y);
            return;
        }

        if(tile == Tile.LOCKED_DOOR){

            if(creature.inventory().hasItem("Key")) {
                creature.consumeItem(creature.inventory().getItem("Key"));
                creature.openDoor(x, y);
                return;
            }

            creature.attackDoor(tile, x, y);
            return;
        }

        if(tile == Tile.SHRINE){
            creature.world().setTile(x, y, Tile.USED_SHRINE);

            if(creature.powers.size() < 10) {
                creature.world().levelUp = true;
            } else {
                Random random = creature.world().random;
                //TODO: epic shrines
                //for(int i=0; i<12; i++) {
                //    int xx = x + creature.world().random.nextInt(5) - 2;
                //    int yy = y + creature.world().random.nextInt(5) - 2;
                //    String itemString = ItemList.randomItemString(random.nextInt(12), random.nextInt(100) + level * 3);
                //    Item item = ItemList.get(itemString);
                //    creature.world().addAtEmptySpace(item, xx, yy);
                //}

            }
        }

        if(tile == Tile.WATER) {
            for(EventTrigger e : creature.world().events){
                if(e.trigger.equals("Water Step")){ e.resolve(creature.world(), creature); }
            }
            //if(e.trigger.equals("Wolf Call")){ e.resolve(this, player()); }
            //creature.world().eventTest("Water Step", x, y, creature);
        }

        if(tile == Tile.WALL_SWITCH){
            for(int a=x-1; a<=x+1; a++){
                for(int b=y-1; b<=y+1; b++) {
                    creature.open(a, b);
                }
            }
            EffectManager.splash(x, y, creature.world());

        }

        if (tile.canEnter(creature) || debugMode){
            if(creature.world().items[x][y] != null && creature.world().items[x][y].name().equals("Yen")){
                int n = creature.world().items[x][y].charges;
                creature.doAction(AsciiPanel.brightWhite, "collect " + (char)157 + " %d", n);
                creature.cash += n;
                creature.world().items[x][y] = null;
            }

            if(creature.world().items[x][y] != null && creature.world().items[x][y].name().equals("Health Orb")){
                creature.hp = Math.min(creature.hp + (creature.mhp() / 20), creature.mhp());
                if(creature.hasTrait("Healthy Orbs")){
                    creature.hp = Math.min(creature.hp + (creature.mhp() / 20), creature.mhp());
                    creature.doAction("consume the HP orb x2");
                } else {
                    creature.doAction("consume the HP orb");
                }
                creature.world().items[x][y] = null;
            }

            if(creature.world().items[x][y] != null && creature.world().items[x][y].name().equals("Mana Orb")){
                creature.mp = Math.min(creature.mp + (creature.mmp() / 20), creature.mmp());
                creature.doAction("consume the MP orb");
                creature.world().items[x][y] = null;
            }





            creature.x = x;
            creature.y = y;

            boolean sprint = false;
            if(creature.hasTrait("Sprinter") && creature.world().random.nextInt(100) < 10){ sprint = true; }
            if(creature.hasTrait("Haste") && creature.world().random.nextInt(100) < 20){ sprint = true; }
            if(creature.hasTrait("Speedy") && creature.world().random.nextInt(100) < 10){ sprint = true; }
            if(creature.hasTrait("Fast") && creature.world().random.nextInt(100) < 10){ sprint = true; }
            if(sprint){
                for(Creature c : creature.world().creatures){
                    if(c.isPlayer){ continue; }
                    if(c.isFast){ continue; }
                    c.stunTime ++;
                }
            }


        }
        creature.triggerTile(creature.x, creature.y, creature.world().tile(creature.x, creature.y));
    }

    public void onNotify(Color c, String message){
        combatLog.add(new Message(message, c));
    }

    public void detailInventory(){
        combatLog.add(new Message("Inventory:", AsciiPanel.brightGreen));
        //for(Item i : creature.inventory().getItems()){
        for(int i=0; i<creature.inventory().getItems().length; i++){
            Item item = creature.inventory().get(i);
            if(item == null){ continue; }

            String m = item.name();
            m = "Slot " + i + ": " + m;
            combatLog.add(new Message(m, AsciiPanel.white));
            //combatLog.add(new Message("  " + item.detail(), AsciiPanel.brightBlack));
        }
    }

    public Action useItem(){
        int i = creature.readyItem;
        Item item = creature.inventory().get(i);
        if(item == null){ return null; }
        if(item.action == null){ return null; }



            Action act = item.action;
            return act;

    }
}
