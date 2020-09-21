package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

import java.awt.*;

public class GainStatus implements Action {

    String trait;
    Color color;
    int time;
    boolean decayEachTurn;
    boolean decayEachLevel;
    String action;

    public GainStatus(String trait, Color color, int time) {
        super();
        this.trait = trait;
        this.color = color;
        this.time = time;
        this.decayEachTurn = true;
        this.decayEachLevel = true;
        this.action = "gain " + this.trait;

    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status(trait, color, time, decayEachTurn, decayEachLevel, creature));
        creature.doAction(action);
    }

    @Override
    public String aimType() { return "Self"; }
}
