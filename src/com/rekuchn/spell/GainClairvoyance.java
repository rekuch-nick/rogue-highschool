package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class GainClairvoyance implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status("Clairvoyance", AsciiPanel.brightCyan, 300, true, true, creature));
        creature.doAction("gain the power of Clairvoyance");
    }

    @Override
    public String aimType() { return "Self"; }
}
