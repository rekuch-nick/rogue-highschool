package com.rekuchn.spell;

import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.EventTrigger;
import com.rekuchn.model.Tile;

public class Radiation implements Action {

    private World world;

    @Override
    public void resolve(Creature creature, int x, int y) {
        this.world = creature.world();
        x = creature.x; y = creature.y;

        for(int a = x-9; a<=x+9; a++){ for(int b = y-9; b<=y+9; b++){
            int d = Math.abs(a - x) + Math.abs(b - y);
            if(d <= 10){
                if(a == x && b == y){ continue; }
                burn(a, b, creature);
            }
        }}



        creature.doAction("create radiation");
    }

    private void burn(int x, int y, Creature creature){
        if(x < 1 || y < 1 || x >= world.width() - 1 || y >= world.height() - 1){ return; }
        if(world.tile(x, y).canOverWrite() || world.tile(x, y) == Tile.WATER){ world.setTile(x, y, Tile.FLOOR); }
        world.effects().add(new Effect(x, y, AsciiPanel.brightYellow, AsciiPanel.brightGreen, 0, 0, 0, 0, 6));

        Creature c = creature.world().creature(x, y);
        if (c != null && !c.isPlayer && !c.isWaifu) {
            creature.attack(world.creature(x, y), "Magic", "Accurate", "Strong");
        }



    }


    @Override
    public String aimType() { return "Self"; }
}
