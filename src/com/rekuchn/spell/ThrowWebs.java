package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

public class ThrowWebs implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

        int lastX = x; int lastY = y;
        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.darkGrey, AsciiPanel.white, 0, 0, 0, 0, 6,1 ));

            lastX = p.x; lastY = p.y;
        }

        for(int a=lastX-4; a<=lastX+4; a++){ for(int b=lastY-4; b<=lastY+4; b++) {
            if(a < 0 || b < 0 || a >= world.width() || b >= world.height()){ continue; }

            world.effects().add(new Effect(a, b, AsciiPanel.darkGrey, AsciiPanel.white, 0, 0, 0, 0, 6, 1));

            Creature c = creature.world().creature(a, b);

            if(creature.world().tile(a, b).canOverWrite()){
                creature.world().setTile(a, b, Tile.WEB);
            }
        }}
        creature.doAction("create a spiderweb");





    }

    @Override
    public String aimType() { return "Line"; }
}






