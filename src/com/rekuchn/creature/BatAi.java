package com.rekuchn.creature;

public class BatAi extends CreatureAi {

    public BatAi(Creature creature) {
        super(creature);
        //creature.powers.add("Poison Strike");
    }

    public void onUpdate(){
        Creature c = adjacentFoe();
        if(c != null){

            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
        } else {
            quickWander();
        }
    }

}
