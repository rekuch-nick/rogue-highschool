package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Tile;
import com.rekuchn.service.Cords;

public class ThrowGrease implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();

        int lastX = x; int lastY = y;
        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.black, AsciiPanel.yellow, 0, 0, 0, 0, 6,1 ));

            lastX = p.x; lastY = p.y;
        }

        for(int a=lastX-3; a<=lastX+3; a++){ for(int b=lastY-3; b<=lastY+3; b++) {
            if(a < 0 || b < 0 || a >= world.width() || b >= world.height()){ continue; }
            if(world.random.nextBoolean()){ continue; }

            world.effects().add(new Effect(a, b, AsciiPanel.black, AsciiPanel.yellow, 0, 0, 0, 0, 6, 1));


            if(creature.world().tile(a, b).canOverWrite()){
                creature.world().setTile(a, b, Tile.GREASE);
            }
        }}

        for(int a=lastX-1; a<=lastX+1; a++){ for(int b=lastY-1; b<=lastY+1; b++) {
            if(a < 0 || b < 0 || a >= world.width() || b >= world.height()){ continue; }

            world.effects().add(new Effect(a, b, AsciiPanel.black, AsciiPanel.yellow, 0, 0, 0, 0, 6, 1));


            if(creature.world().tile(a, b).canOverWrite()){
                creature.world().setTile(a, b, Tile.GREASE);
            }
        }}

        creature.doAction("create a patch of grease");





    }

    @Override
    public String aimType() { return "Line"; }
}






