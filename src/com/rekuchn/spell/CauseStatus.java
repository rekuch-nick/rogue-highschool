package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.World;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Effect;
import com.rekuchn.model.Status;
import com.rekuchn.service.Cords;

import java.awt.*;

public class CauseStatus implements Action {

    String trait;
    Color color;
    int time;
    boolean decayEachTurn;
    boolean decayEachLevel;
    String action;

    boolean displayLine;
    Color eFG;
    Color eBG;

    public CauseStatus(String trait, Color color, int time, Color eFG, Color eBG) {
        this.trait = trait;
        this.color = color;
        this.time = time;
        this.decayEachTurn = true;
        this.decayEachLevel = true;
        this.displayLine = true;
        this.eFG = eFG;
        this.eBG = eBG;
        this.action = "become " + trait;
    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        World world = creature.world();


        for(Cords p : world.getLineBetween(creature.x, creature.y, x, y)){
            if(p.x == creature.x && p.y == creature.y){ continue; }
            if(displayLine) {
                world.effects().add(new Effect(p.x, p.y, eFG, eBG, 0, 0, 0, 0, 6));
            }
            if(creature.world().creature(p.x, p.y) != null){
                world.creature(p.x, p.y).status.add(new Status(trait, color, time, decayEachTurn, decayEachLevel, world.creature(p.x, p.y)));
                world.creature(p.x, p.y).doAction(action);
                world.effects().add(new Effect(p.x, p.y, eFG, eBG, 0, 0, 0, 0, 6));
            }
        }





    }

    @Override
    public String aimType() { return "Line"; }
}






