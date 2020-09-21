package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Status;
import com.rekuchn.service.Cords;

import java.awt.*;

public class CauseFrail implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();


        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            world.effects().add(new Effect(p.x, p.y, AsciiPanel.magenta, Color.darkGray, 0, 0, 0, 0, 6));
            if(creature.world().creature(p.x, p.y) != null){
                world.creature(p.x, p.y).status.add(new Status("Frail", AsciiPanel.magenta, 20, true, true, world.creature(p.x, p.y)));
                world.creature(p.x, p.y).doAction("become frail");
                world.effects().add(new Effect(p.x, p.y, AsciiPanel.magenta, Color.darkGray, 0, 0, 0, 0, 6));
            }
        }





    }

    @Override
    public String aimType() { return "Line"; }
}






