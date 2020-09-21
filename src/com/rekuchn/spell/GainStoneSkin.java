package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

import java.awt.*;

public class GainStoneSkin implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status("Stone Skin", AsciiPanel.yellow, 120, true, true, creature));
        creature.doAction("gain hardened skin");
    }

    @Override
    public String aimType() { return "Self"; }
}
