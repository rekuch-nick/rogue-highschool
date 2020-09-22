package com.rekuchn.spell;


import com.rekuchn.AsciiPanel;
import com.rekuchn.creature.Creature;
import com.rekuchn.model.Status;

public class AlterMight implements Action {

    int number;

    public AlterMight(int number) {
        this.number = number;
    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        if(number == 0){ return; }

        creature.alterMight(number);
        if(number > 0){
            creature.doAction("gain MIGHT");
        } else {
            creature.doAction("lose MIGHT");
        }
    }

    @Override
    public String aimType() { return "Self"; }
}
