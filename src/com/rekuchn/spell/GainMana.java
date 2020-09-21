package com.rekuchn.spell;

import com.rekuchn.creature.Creature;

public class GainMana implements Action {


    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.mp = Math.min(creature.mhp(), creature.mp + 20);
        creature.doAction("recover some MP");
    }

    @Override
    public String aimType() { return "Self"; }
}
