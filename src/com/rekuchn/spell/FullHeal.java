package com.rekuchn.spell;

import com.rekuchn.creature.Creature;

public class FullHeal implements Action {


    @Override
    public void resolve(Creature creature, int x, int y) {
            creature.hp = creature.mhp();
            //creature.doAction("drink a potion");
            creature.doAction("heal to full HP");
    }

    @Override
    public String aimType() { return "Self"; }
}
