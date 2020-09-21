package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

import java.awt.*;

public class Disintegrate implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

        int lastX = x; int lastY = y;
        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.brightWhite, Color.pink, 0, 0, 0, 0, 6));

            lastX = p.x; lastY = p.y;
        }

        for(int a=lastX-1; a<=lastX+1; a++){ for(int b=lastY-1; b<=lastY+1; b++) {
            if(a < 0 || b < 0 || a >= world.width() || b >= world.height()){ continue; }

            world.effects().add(new Effect(a, b, AsciiPanel.brightWhite, Color.pink, 0, 0, 0, 0, 6));
            world.burnTile(a, b);
            Creature c = creature.world().creature(a, b);
            if (c != null && !c.isPlayer && !c.isWaifu) {
                creature.attack(world.creature(a, b), "Magic", "Accurate", "Strong");
            }

            if(a < 1 || b < 1 || a >= world.width()-1 || b >= world.height()-1){ continue; }
            if(world.tile(a, b).isWall()){ world.setTile(a, b, Tile.FLOOR); }
        }}





    }

    @Override
    public String aimType() { return "Line"; }
}






