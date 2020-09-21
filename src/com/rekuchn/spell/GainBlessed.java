package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class GainBlessed implements Action {

    @Override
    public void resolve(Creature creature, int x, int y) {
        creature.addStatus(new Status("Blessed", AsciiPanel.brightYellow, 90, true, true, creature));
        //creature.doAction("drink a potion");
        creature.doAction("gain a blessing against magic damage");
    }

    @Override
    public String aimType() { return "Self"; }
}
