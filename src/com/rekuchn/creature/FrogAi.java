package com.rekuchn.creature;

public class FrogAi extends CreatureAi {

    Creature player;

    public FrogAi(Creature creature) {

        super(creature);
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }
        creature.powers.add("Memory Strike");
    }

    public void onUpdate(){

        Creature c = adjacentFoe();
        if(c != null){
            int x = c.x - creature.x;
            int y = c.y - creature.y;
            creature.moveBy(x, y);
            return;
        }

        if(canSee(player.x, player.y)){
            pathTo(player.x, player.y);
        } else {
            wander();
        }


    }





}
