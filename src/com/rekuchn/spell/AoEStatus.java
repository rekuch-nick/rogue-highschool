package com.rekuchn.spell;


import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;
import com.rekuchn.service.EffectManager;

import java.awt.*;

public class AoEStatus implements Action {

    String trait;
    Color color;
    int time;
    boolean decayEachTurn;
    boolean decayEachLevel;
    String action;

    boolean hostile;
    int range;
    boolean selfToo;

    public AoEStatus(String trait, Color color, int time, boolean hostile, int range) {
        super();
        this.trait = trait;
        this.color = color;
        this.time = time;
        this.decayEachTurn = true;
        this.decayEachLevel = true;
        this.hostile = hostile;
        this.range = range;

        if(hostile) {
            this.action = "cause " + this.trait + " to nearby foes";
        } else {
            this.action = "grant " + this.trait + " to nearby allies";
        }

    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        for(Creature c : creature.world().creatures){
            if(c == creature && !selfToo){ continue; }
            int d = Math.abs(creature.x - c.x) + Math.abs(creature.y - c.y);
            if(d <= range){
                if( (c.hostile == creature.hostile && !hostile) || (c.hostile != creature.hostile && hostile) ){
                    if(!c.isWaifu){
                        c.status.add(new Status(trait, color, time, decayEachTurn, decayEachLevel, c));
                        EffectManager.hilight(c.x, c.y, creature.world(), color);
                    }
                }
            }
        }

        creature.doAction(action);
    }

    @Override
    public String aimType() { return "Self"; }
}
