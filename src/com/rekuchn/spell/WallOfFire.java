package com.rekuchn.spell;

import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.Tile;

import javax.script.ScriptEngine;

public class WallOfFire implements Action {

    private World world;

    @Override
    public void resolve(Creature creature, int x, int y) {
        this.world = creature.world();
        x = creature.x; y = creature.y;

        for(int a = x-3; a<=x+3; a++){ for(int b = y - 3; b<=y+3; b++){
            if(a == x - 3 || a == x + 3 || b == y -3 || b == y + 3){
                burn(a, b);
            }
        }}



        creature.doAction("make a wall of fire");
    }

    private void burn(int x, int y){
        if(x < 1 || y < 1 || x >= world.width() - 1 || y >= world.height() - 1){ return; }
        if(!world.tile(x, y).canOverWrite()){ return; }

        world.setTile(x, y, Tile.FIRE_4);
        world.events.add(new EventTrigger("Fire Tile", x, y, x, y));
    }


    @Override
    public String aimType() { return "Self"; }
}
