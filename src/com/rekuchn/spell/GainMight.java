package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class GainMight implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status("Mighty", AsciiPanel.red, 60, true, true, creature));
        //creature.doAction("drink a potion");
        creature.doAction("gain a temporary boost to MIGHT");
    }

    @Override
    public String aimType() { return "Self"; }
}
