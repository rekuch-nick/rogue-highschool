package com.rekuchn.creature;

import com.rekuchn.model.ObjectFactory;

import java.awt.*;

public class FungusAi extends CreatureAi {
    private ObjectFactory factory;

    public FungusAi(Creature creature, ObjectFactory factory){
        super(creature);
        this.factory = factory;

        creature.hasMind = false;
    }

    public void onUpdate(){
        if (creature.hp > 1 && Math.random() < 0.2) {
            spread();
        }

        creature.moveBy(0, 0);

    }

    public void deathRattle(){

        /*
        for(int x = creature.x-3; x<=creature.x+3; x++){
            for(int y = creature.y-3; y<=creature.y+3; y++) {
                Creature target = creature.world().creature(x, y);
                if(target != null){
                    if(target.hp < target.mhp()){
                        target.hp ++;
                    }
                }
            }
        }
        creature.doAction(Color.orange, "heal nearby creatures 1 HP");
        */
    }

    private void spread(){

        int x = creature.x + (int)(Math.random() * 9) - 4;
        int y = creature.y + (int)(Math.random() * 9) - 4;

        if (!creature.canEnter(x, y)){ return; }

        creature.hp --;
        Creature child = factory.newFungus(creature, creature.hp);
        child.x = x;
        child.y = y;


    }

    public String getDescription(String name) {
        return "The " + name + " is oozing with putrid slime.";
    }

}
