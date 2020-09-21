package com.rekuchn.creature;

import com.rekuchn.spell.Action;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;
import com.rekuchn.service.Line;
import com.rekuchn.service.Pathing;

import java.awt.*;
import java.util.List;


public class CreatureAi {
    protected Creature creature;



    public CreatureAi(Creature creature){
        this.creature = creature;
        this.creature.setCreatureAi(this);
    }

    public void onUpdate(){

    }

    public void onEnter(int x, int y, Tile tile) {
        onEnter(x, y, tile, false);
    }
    public void onEnter(int x, int y, Tile tile, boolean pushed) {



        if( (!pushed && tile.canEnter(creature)) ||  (pushed && tile.canEnter()) ){
            creature.x = x;
            creature.y = y;


        } else {
            if(pushed && tile.isWall()){
                creature.doAction("crashes into the wall");
                creature.takeDamage(2, "Bump");
            }
        }

        creature.triggerTile(creature.x, creature.y, creature.world().tile(creature.x, creature.y));
    }



    public void quickWander(){
        int mx = creature.world().random.nextInt(3) - 1;
        int my = creature.world().random.nextInt(3) - 1;
        creature.moveBy(mx, my);
    }

    public void wander(){
        int mx = 0; int my = 0;
        int adjust = creature.world().random.nextBoolean() ? 1 : -1;
        if(creature.world().random.nextBoolean()){
            mx += adjust;
        } else {
            my += adjust;
        }
        creature.moveBy(mx, my);
    }

    public void backStep() {
        int mx = 0; int my = 0;
        if(creature.world().random.nextBoolean()) {
            if (creature.x < creature.world().player().x) { mx = -1; }
            if (creature.x > creature.world().player().x) { mx = 1; }
        } else {
            if (creature.y < creature.world().player().y) { my = -1; }
            if (creature.y > creature.world().player().y) { my = 1; }
        }
        creature.moveBy(mx, my);
    }

    public void pathTo(int x, int y){

        Pathing.avoidCreatures = true;
        List<Cords> path = Pathing.find(creature.x, creature.y, x, y, creature.world());
        if(path == null) {
            Pathing.avoidCreatures = false;
            path = Pathing.find(creature.x, creature.y, x, y, creature.world());
        }

        if (path != null && path.size() > 0) {
            creature.moveBy(Cords.xDir(creature.x, path.get(0).x), Cords.yDir(creature.y, path.get(0).y));
        } else {
            //creature.triggerTile(creature.x, creature.y, creature.world().tile(creature.x, creature.y));
            creature.moveBy(0, 0);
        }


    }

    public String getDescription(String name) {
        return "The " + name + " is ready to attack.";
    }

    public boolean canSee(int wx, int wy) {
        if(creature.x == wx && creature.y == wy){ return true; }

        int r = creature.visionRadius;
        if(creature.hasTrait("Blind")){ r = Math.max(1, r / 4); }

        if(!creature.isPlayer && creature.hostile){
            if(creature.world().creature(wx, wy) != null && creature.world().creature(wx, wy).hasTrait("Invisible") ){
                r = 2;
            }
        }

        if ((creature.x-wx)*(creature.x-wx) + (creature.y-wy)*(creature.y-wy) > r*r)
            return false;

        for (Cords p : new Line(creature.x, creature.y, wx, wy)){

            if (creature.tile(p.x, p.y).canSeeThrough() || (p.x == wx && p.y == wy) || (p.x == creature.x && p.y == creature.y ) )
                continue;

            return false;
        }
        return true;
    }






    public void onNotify(Color c, String s) {}
    public void detailInventory(){}
    public Action useItem(){ return null; }

    public Creature adjacentFoe(){

        for(int i=1; i<5; i++) {
            int x = creature.x; int y = creature.y;
            if(i == 1){y --; }
            if(i == 2){x ++; }
            if(i == 3){y ++; }
            if(i == 4){x --; }
            Creature c = creature.world().creature(x, y);
            if(c != null && c.hostile != creature.hostile && !c.isWaifu){
                return c;
            }
        }

        return null;
    }

    public void deathRattle(){}
}