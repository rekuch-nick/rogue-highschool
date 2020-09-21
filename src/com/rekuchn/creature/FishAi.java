package com.rekuchn.creature;

public class FishAi extends CreatureAi {

    Creature player;

    public FishAi(Creature creature) {

        super(creature);
        creature.waterOnly = true;
        creature.swimChance = 100;
        player = null;
        for(Creature c : creature.world().creatures){
            if(c.isPlayer){ player = c; break; }
        }
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
