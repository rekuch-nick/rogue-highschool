package com.rekuchn.creature;

public class BruteAi extends CreatureAi {

    Creature player;

    public BruteAi(Creature creature) {

        super(creature);
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }
    }

    public void onUpdate(){

        if(creature.world().random.nextBoolean() && creature.world().random.nextBoolean() && adjacentFoe() == null){ return; }

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
