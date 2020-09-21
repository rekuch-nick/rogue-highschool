package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class GainInvisibility implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status("Invisible", AsciiPanel.brightBlue, 140, true, true, creature));
        creature.doAction("become invisible");
    }

    @Override
    public String aimType() { return "Self"; }
}
