package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class GainTelepathy implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
            creature.addStatus(new Status("Telepathy", AsciiPanel.brightCyan, 40, true, true, creature));
            creature.doAction("see nearby minds");
    }

    @Override
    public String aimType() { return "Self"; }
}
