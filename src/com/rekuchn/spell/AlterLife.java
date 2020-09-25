package com.rekuchn.spell;


import com.rekuchn.creature.Creature;

public class AlterLife implements Action {

    int number;

    public AlterLife(int number) {
        this.number = number;
    }

    @Override
    public void resolve(Creature creature, int x, int y) {
        if(number == 0){ return; }

        creature.alterLife(number);
        if(number > 0){
            creature.doAction("gain Max HP");
        } else {
            creature.doAction("lose Max HP");
        }
    }

    @Override
    public String aimType() { return "Self"; }
}
